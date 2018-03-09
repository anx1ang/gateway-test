package com.zxk.vertx.factory;

import com.zxk.vertx.standard.SingleVertxRouter;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;

/**
 * router 扫描注册器
 *
 * @ProjectName: vertx-core
 * @Package: org.rayeye.vertx.verticle
 * @ClassName: RouterRegistryHandlersFactory
 * @Description: router 扫描注册器
 * @Author: Neil.Zhou
 * @CreateDate: 2017/9/21 0:26
 * @UpdateUser: Neil.Zhou
 * @UpdateDate: 2017/9/21 0:26
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2017</p>
 */
public class RouterRegistryHandlersFactory extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(RouterRegistryHandlersFactory.class);
    protected Router router;
    HttpServer server;
    // 默认http server端口
    public static volatile int port=8080;

    /** Constructs a new RouterRegistryHandlersFactory. */
    public RouterRegistryHandlersFactory(int port) {
        this.router= SingleVertxRouter.getRouter();
        if(port>0){
            this.port=port;
        }
    }
    public RouterRegistryHandlersFactory(Router router) {
        Objects.requireNonNull(router, "The router is empty.");
        this.router=router;
    }
    public RouterRegistryHandlersFactory(Router router,int port) {
        this.router=router;
        if(port>0){
            this.port=port;
        }
    }

    /**
     * 重写启动verticle
     * @method      start
     * @author      Neil.Zhou
     * @param future
     * @return      void
     * @exception
     * @date        2017/9/21 0:33
     */
    @Override
    public void start(Future<Void> future) throws Exception {
        LOGGER.trace("To start listening to port {} ......",port);
        super.start();
        HttpServerOptions options = new HttpServerOptions().setMaxWebsocketFrameSize(1000000).setPort(port);
        server = vertx.createHttpServer(options);
        router.mountSubRouter("/api", apiRouter());
        server.requestHandler(router::accept);
        server.listen(result -> {
            if (result.succeeded()) {
                future.complete();
            } else {
                future.fail(result.cause());
            }
        });
    }

    private Router apiRouter() {
        Router router = Router.router(vertx);
        router.route().consumes("application/json");
        router.route().produces("application/json");

        router.route().handler(context -> {
            context.response().headers().add(CONTENT_TYPE, "application/json");
            context.next();
        });
        router.get("/status").handler(context->{
            HttpServerResponse response = context.response();
            String host = context.request().getHeader("Host");
            response.putHeader("content-type", "application/json; charset=utf-8");

            response.setChunked(true);
            response.setStatusCode(200);
            response.write("Server Host\t: " + host + "\n");
            response.end("Server Status\t: alive");
        });

        return router;
    }
    /**
     * 重写停止verticle
     * @method      start
     * @author      Neil.Zhou
     * @param future
     * @return      void
     * @exception
     * @date        2017/9/21 0:33
     */
    @Override
    public void stop(Future<Void> future) {
        if (server == null) {
            future.complete();
            return;
        }
        server.close(result -> {
            if (result.failed()) {
                future.fail(result.cause());
            } else {
                future.complete();
            }
        });
    }

}
