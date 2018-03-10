package com.zxk.vertx.server;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import static io.vertx.core.http.HttpHeaders.*;

/**
 * 允许所有的请求域通过。
 * zhangyef@yonyou.com on 2015-11-04.
 */
public class AllowAllCorsHandlerImpl implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext context) {
        HttpServerRequest request = context.request();
        HttpServerResponse response = context.response();

        String origin = request.headers().get(ORIGIN);

        if(origin != null) {
            response.putHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
            response.putHeader(ACCESS_CONTROL_ALLOW_ORIGIN, origin);
        }

        context.next();
    }
}
