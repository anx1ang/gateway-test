package com.zxk.vertx.server;

import com.alibaba.fastjson.JSON;
import com.zxk.entity.HttpRequestDto;
import com.zxk.entity.RegisterInfo;
import com.zxk.enums.BooleanEnum;
import com.zxk.enums.EncryptTypeEnum;
import com.zxk.enums.ExceptionEnums;
import com.zxk.enums.SignTypeEnum;
import com.zxk.exception.GatewayException;
import com.zxk.mongo.MongoDAO;
import com.zxk.utils.DateUtil;
import com.zxk.utils.LogUtil;
import com.zxk.utils.TraceUtil;
import com.zxk.utils.VerfyUtil;
import com.zxk.vertx.standard.StandardVertxUtil;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import static com.zxk.vertx.util.ConstantUtil.REQUEST_DETAIL;

/**
 * 针对每个注册请求,新建一个Dispatcher.
 * <p/>
 * <p>
 * zhangyef@yonyou.com on 2015-12-02.
 */
public class Dispatcher implements Handler<RoutingContext> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private Vertx vertx;

    private static final String appPrefix = "dubboServer.";

    private MongoDAO mongoDAO;

    private static final String signType = SignTypeEnum.MD5.getValue();

    private static final String encryptType = EncryptTypeEnum.AES.getValue();

    /**
     * Rest API的注册信息.
     */
    private RegisterInfo registerInfo;

    private Dispatcher(Vertx vertx, RegisterInfo regInfo) {
        this.vertx = vertx;
        this.registerInfo = regInfo;
        mongoDAO = MongoDAO.create(StandardVertxUtil.getStandardVertx());
    }

    public static Dispatcher create(Vertx vertx, RegisterInfo regInfo) {
        return new Dispatcher(vertx, regInfo);
    }

    @Override
    public void handle(RoutingContext routingContext) {
        logWhenReceivingRequest(routingContext);

        logger.info("Using [Command] mode to deliver HTTP request.");

        travelWithCommand(registerInfo, routingContext);

    }


    private void logWhenReceivingRequest(RoutingContext routingContext) {
        if (!logger.isInfoEnabled()) {
            return;
        }

        logger.info("WebServer recieved HTTP request from client.");
        logger.info("-------- HTTP request content --------");
        HttpServerRequest request = routingContext.request();

        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null) {
            logger.info("client information: " + userAgent);
        }

        String methodName = request.method().name();
        String absUri = request.absoluteURI();
        logger.info(methodName + " " + absUri);
        try {
            String body = routingContext.getBodyAsJson().encodePrettily();
            logger.info(body);
        } catch (Exception e) {
            logger.info(routingContext.getBody().toString());
        }
        logger.info("-------------------------------");
    }

    /**
     * 根据注册信息, 将请求路由到指定位置. 使用传递消息, 从而规范消息格式.
     *
     * @param registerMessage RestAPI注册信息ID.
     * @param context         路由上下文.
     */
    private void travelWithCommand(RegisterInfo registerMessage, RoutingContext context) {
        logger.debug("Using [Command] mode to deliver primitive register message: " + registerMessage);
        logger.info("WebServer is delivering request to target address:" + registerMessage.getAddress());
        DeliveryOptions options = CommandBuilder.createDeliveryOptions(context);

        String address = appPrefix + registerMessage.getClassName();
        logger.info("Command ready send a message " + address);
        JsonObject jsonObject = context.getBodyAsJson();
        HttpServerResponse response = context.response();
        CommandReq commandReq = null;
        HttpRequestDto httpRequestDto = null;
        String requestBody = null;
        try {
            httpRequestDto = validate(jsonObject);
            requestBody = beforeHandler(httpRequestDto);
        } catch (GatewayException e) {
            response.setStatusCode(200);
            response.end(e.toString());
            return;
        }
        buildRequestInfo(httpRequestDto);
        logger.info("收到业务线请求:{}", context);
        commandReq = CommandReq.buildCommand(getRequestPath(context), httpRequestDto.getRequestNo(), requestBody);
        vertx.eventBus().<JsonObject>send(address, commandReq, options, result -> {

            response.putHeader("content-type", "application/json; charset=utf-8");
            if (result.succeeded()) {
                JsonObject resultBody = result.result().body();
                logger.info("WebServer ready send a message success，result= " + resultBody.toString());
                response.setStatusCode(200);
                response.end(resultBody.toString());
            } else {
                response.setStatusCode(200);
                response.end("处理失败");
            }
        });
    }

    private HttpRequestDto validate(JsonObject req) throws GatewayException {
        if (req == null) {
            throw new GatewayException(ExceptionEnums.REQUEST_NULL);
        }
        HttpRequestDto requestDto = JSON.parseObject(req.toString(), HttpRequestDto.class);
        if (requestDto == null) {
            throw new GatewayException(ExceptionEnums.E10004);
        }
        String bizCode = requestDto.getBizCode();
        String serviceCode = requestDto.getServiceCode();

        if (StringUtils.isBlank(bizCode)) {
            throw new GatewayException(ExceptionEnums.REQ_BIZCODE_NULL);
        }
        if (StringUtils.isBlank(serviceCode)) {
            throw new GatewayException(ExceptionEnums.REQ_SERVICECODE_NULL);
        }
        if (StringUtils.isBlank(requestDto.getContext())) {
            throw new GatewayException(ExceptionEnums.REQ_CONTEXT_NULL);
        }
        if (BooleanEnum.TRUE.check(registerInfo.getNeedSign())) {
            if (StringUtils.isBlank(requestDto.getSign())) {
                throw new GatewayException(ExceptionEnums.REQ_SIGN_NULL);
            }
        }
        return requestDto;
    }

    private String beforeHandler(HttpRequestDto req) throws GatewayException {
        if (BooleanEnum.FALSE.check(registerInfo.getNeedSign())) {
            return req.getContext();
        }
        if (!VerfyUtil.verfySign(signType, req.getContext(), req.getSign(), registerInfo.getSignKey())) {
            String logSignKey = registerInfo.getSignKey();
            if (StringUtils.isNotBlank(logSignKey) && logSignKey.length() >= 2) {
                logSignKey = logSignKey.substring(logSignKey.length() - 2);
            }
            logger.error("验签失败,SignType:{0},Context:{1},Sign:{2},SignKey:{3},bizKey:{4}",
                    signType, req.getContext(), req.getSign(), logSignKey, registerInfo.getEncryptKey());
            throw new GatewayException(ExceptionEnums.UNSIGN_ERROR);
        }
        return VerfyUtil.decrypt(encryptType, req.getContext(), registerInfo.getEncryptKey());
    }

    private void buildRequestInfo(HttpRequestDto httpRequestDto) {
        httpRequestDto.setRequestTime(new Date());
        String requestNo = TraceUtil.getTraceId();
        httpRequestDto.setRequestNo(requestNo);
        mongoDAO.insert(REQUEST_DETAIL.concat(DateUtil.formatToMonth(new Date())), new JsonObject(Json.encode(httpRequestDto)), ret -> {
            if (ret.succeeded()) {

            }
        });
    }

    private String getRequestPath(RoutingContext context) {
        HttpServerRequest request = context.request();
        String absUri = request.absoluteURI();
        return absUri.substring(absUri.lastIndexOf("/") + 1, absUri.length());
    }

}
