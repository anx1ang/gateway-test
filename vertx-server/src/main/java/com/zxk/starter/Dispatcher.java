package com.zxk.starter;

import com.zxk.starter.register.RegisterInfo;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

/**
 * 针对每个注册请求,新建一个Dispatcher.
 * <p/>
 *
 * zhangyef@yonyou.com on 2015-12-02.
 */
public class Dispatcher implements Handler<RoutingContext> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private Vertx vertx;

    private static final String appPrefix = "dubboServer.";

    /**
     * Rest API的注册信息.
     */
    private RegisterInfo registerInfo;

    private Dispatcher(Vertx vertx, RegisterInfo regInfo) {
        this.vertx = vertx;
        this.registerInfo = regInfo;
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
        CommandReq commandReq = CommandReq.buildCommand(getRequestPath(context), jsonObject);
        vertx.eventBus().<JsonObject>send(address, commandReq, options, result -> {
            HttpServerResponse response = context.response();
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

    private String getRequestPath(RoutingContext context) {
        HttpServerRequest request = context.request();
        String absUri = request.absoluteURI();
        return absUri.substring(absUri.lastIndexOf("/") + 1, absUri.length());
    }

}
