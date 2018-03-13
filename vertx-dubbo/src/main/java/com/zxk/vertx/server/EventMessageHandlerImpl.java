package com.zxk.vertx.server;

import com.zxk.entity.RegisterInfo;
import com.zxk.starter.StartRunner;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by better/zhangye on 15/9/22.
 */
public class EventMessageHandlerImpl implements EventMessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartRunner.class);


    private static final String METHOD = "POST";

    private Vertx vertx;

    private Router router;

    public EventMessageHandlerImpl(Vertx vertx) {
        this.vertx = vertx;
        this.router = ShareableUtil.getRegRouter(vertx);
    }

    /**
     * 建立REST API请求到EventBus的桥。 使得注册的REST API请求，可以经过转换变为事件总线上的消息，并将消息发送给应用。
     * 当应用处理完消息后，对消息进行回复。回复消息经过转化后，变为标准的HTTP响应，返回给发起REST API请求的客户端。
     *
     * @return this
     */
    @Override
    public EventMessageHandler bridge(List<RegisterInfo> registerInfos) {
        for (RegisterInfo registerInfo : registerInfos) {
            LOGGER.info("添加一个API接口: " + registerInfo.getUri());
            // 单独注册
            String url = registerInfo.getUri().replace(".", "/");
            url = url + "/" + registerInfo.getAction();
            // 如果没有前导斜线,则自动添加.
            url = url.startsWith("/") ? url : "/" + url;
            Route route = addRoute(METHOD, url);
            LOGGER.info("开始监听 REST API 地址[" + url + "]");
            route.handler(dispatch(registerInfo));
        }
        return this;
    }


    private Handler<RoutingContext> dispatch(RegisterInfo registerMessage) {
        return Dispatcher.create(vertx, registerMessage);
    }

    private Route addRoute(String method, String path) {
        if (method == null || method.isEmpty()) {
            return router.route(path);
        }

        String methodName = method.toUpperCase();
        HttpMethod httpMethod = HttpMethod.valueOf(methodName);

        return router.route(httpMethod, path);
    }


    @Override
    public void handle(RoutingContext event) {
        router.handleContext(event);
    }
}
