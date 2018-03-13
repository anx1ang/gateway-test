package com.zxk.starter;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zxk.entity.MethodInfo;
import com.zxk.entity.MethodMap;
import com.zxk.enums.ExceptionEnums;
import com.zxk.exception.GatewayException;
import com.zxk.vertx.result.ResultOb;
import com.zxk.vertx.server.CommandReq;
import com.zxk.vertx.standard.StandardVertxUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * verticle 处理器工厂
 * 支持集群、单例
 *
 * @ProjectName: vertx-core
 * @Package: org.rayeye.vertx.verticle
 * @ClassName: VerticleHandlerFactory
 * @Description: verticle 处理器工厂
 * @Author: Neil.Zhou
 * @CreateDate: 2017/9/20 18:22
 * @UpdateUser: Neil.Zhou
 * @UpdateDate: 2017/9/20 18:22
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2017</p>
 */
public class VerticleHandlerFactory extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerticleHandlerFactory.class);

    private String busAddress;

    public VerticleHandlerFactory(String eventBusAddress) {
        this.busAddress = eventBusAddress;
    }

    /**
     * 针对verticle处理返回信息解析，主要针对错误信息识别成json数据，用户明文返回出去
     * 依赖与org.util中的exception
     *
     * @return org.rayeye.vertx.result.ResultOb
     * @throws
     * @method setResult
     * @author Neil.Zhou
     * @version
     * @date 2017/9/20 18:34
     */
    private ResultOb setResult(GatewayException excption) {
        ResultOb result = new ResultOb();
        result.setCode(excption.errorCode);
        result.setMsg(excption.getMessage());
        return result;
    }

    /**
     * 消息处理handler
     *
     * @param
     * @return io.vertx.core.Handler<io.vertx.core.eventbus.Message<io.vertx.core.json.JsonObject>>
     * @throws
     * @method msgHandler
     * @author Neil.Zhou
     * @date 2017/9/20 18:37
     */
    private Handler<Message<JsonObject>> msgHandler() {
        return msg -> {
            LOGGER.info(busAddress + ", 收到一条消息：" + msg.body());
            ResultOb result = new ResultOb();
            JsonObject message = null;
            try {
                CommandReq commandReq = CommandReq.buildCommand(msg.body());
                Object responseMessage = invokeDubbo(commandReq);
                message = new JsonObject(Json.encode(responseMessage));
                msg.reply(message);
            } catch (GatewayException gwException) {
                LOGGER.error("业务异常,e={}", gwException);
                result = setResult(gwException);
            } catch (Exception e) {
                LOGGER.error("未知异常", e);
                result = setResult(new GatewayException(ExceptionEnums.QUERY_SERVICE_CODE_NULL));
            }
            msg.reply(new JsonObject(Json.encode(result)));
        };
    }

    private Object invokeDubbo(CommandReq commandReq) throws Exception {
        try {
            MethodInfo methodInfo = getMethod(busAddress, commandReq.getMethod());
            if (methodInfo == null) {
                throw new GatewayException(ExceptionEnums.INVOKE_UNIMPL_METHOD);
            }
            Object params = JSONObject.parseObject(commandReq.getRequestBody(), methodInfo.getParam());
            return methodInfo.getMethodName().invoke(methodInfo.getInteface(), params);
        } catch (JSONException je) {
            LOGGER.error("jSON字符串转换对象异常", je);
            throw new GatewayException(ExceptionEnums.PARAM_PARSE_OBJECT_ERROR);
        } catch (InvocationTargetException ite) {
            LOGGER.error("调用第三方接口异常", ite);
            throw new GatewayException(ExceptionEnums.INVOKE_METHOD_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GatewayException(ExceptionEnums.INVOKE_METHOD_ERROR);
        }
    }

    /**
     * 注册事件驱动并
     *
     * @param
     * @return void
     * @throws Exception
     * @method start
     * @author Neil.Zhou
     * @version
     * @date 2017/9/20 18:43
     * @see #start()
     */
    @Override
    public void start() throws Exception {
        super.start();
        StandardVertxUtil.getStandardVertx().eventBus().<JsonObject>consumer(busAddress).handler(msgHandler());
        LOGGER.info("Verticle部署成功：" + busAddress);
    }

    private static MethodInfo getMethod(String busAddress, String method) {
        String className = busAddress.substring(busAddress.indexOf(".") + 1, busAddress.length());
        String methodName = className.concat(".").concat(method);
        return MethodMap.getMethodMap().get(methodName);
    }

}
