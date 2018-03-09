package com.zxk.starter;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zxk.entity.MethodInfo;
import com.zxk.entity.MethodMap;
import com.zxk.enums.ExceptionEnums;
import com.zxk.exception.GatewayException;
import com.zxk.starter.register.RegisterInfo;
import com.zxk.vertx.result.ResultOb;
import com.zxk.vertx.standard.StandardVertxUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * @param errorObj
     * @param result
     * @return org.rayeye.vertx.result.ResultOb
     * @throws
     * @method setResult
     * @author Neil.Zhou
     * @version
     * @date 2017/9/20 18:34
     */
    private ResultOb setResult(Throwable errorObj, ResultOb result) {
        try {
            JsonObject error = new JsonObject(errorObj.getMessage());
            if (error.containsKey("httpStatus")) {
                result.setCode(error.getInteger("httpStatus", 500));
            }
            /**** 服务正忙 默认是服务异常，为了友好性和调试方便，将错误信息解析后放到msg信息中 ***/
            if (error.containsKey("code")) {
                result.setData(error.getString("code", "服务正忙,稍后再试.").concat("[").concat(error.getString("detailMsg")).concat("]"));
            }
            if (error.containsKey("message")) {
                result.setMsg(error.getString("message", "服务正忙,稍后再试."));
            }
        } catch (Exception ex) {
            Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher matcher = p.matcher(errorObj.getMessage());
            if (matcher.find()) {
                result.setMsg(errorObj.getMessage());
                result.setData(errorObj.getLocalizedMessage());
            } else {
                result.setMsg("服务正忙,稍后再试.");
                result.setData(errorObj.getLocalizedMessage());
            }
        }
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
        LOGGER.info(busAddress + "收到一条消息：");
        ResultOb result = new ResultOb();
        return msg -> {
            JsonObject message = null;
            try {
                CommandReq commandReq = CommandReq.buildCommand(msg.body());
                MethodInfo methodInfo = getMethod(busAddress, commandReq.getMethod());
                Object params = null;
                params = JSONObject.parseObject(commandReq.getRequestBody().toString(), methodInfo.getParam());
                Object responseMessage = methodInfo.getMethodName().invoke(methodInfo.getInteface(), params);
                if (responseMessage != null) {
                    message = new JsonObject(Json.encode(responseMessage));
                } else {
                    result.setCode(500);
                    result.setMsg("获取服务结果为空");
                    msg.reply(new JsonObject(Json.encode(result)));
                }
                msg.reply(message);
            } catch (JSONException je) {
                LOGGER.error("jSON字符串转换对象异常", je);
                result.setCode(500);
                result.setMsg("服务正忙,稍后再试.");
            } catch (Exception e) {
                result.setCode(500);
                LOGGER.error("未知异常", e);
                msg.reply(new JsonObject(Json.encode(result)));
            }
        };
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
