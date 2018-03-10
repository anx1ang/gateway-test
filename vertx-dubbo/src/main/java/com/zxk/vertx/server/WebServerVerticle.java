package com.zxk.vertx.server;

import com.zxk.entity.RegisterInfo;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;

import java.util.List;

import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;

/**
 * 默认端口为8081.
 */
public class WebServerVerticle extends AbstractVerticle {

    // verticle服务名
    public static final String CONFIG_WEBSERVER_NAME_KEY = "webserver_name";
    private static final String CONFIG_HTTP_PORT_KEY = "http.port";
    private static final String CONFIG_AUTHENTICATION = "auth.enabled";
    private static final int MAX_FRAME_SIZE = 1500000;
    private static final String eventBusRouteURL = "/eventbus/*";
    private static final String clientLogPath = "../logs/client-log/";


    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private Router mainRouter;
    private HttpServer server;
    private JsonObject config;

    // 默认开启授权.
    private AuthSwitch authSwitch;
    private AuthConfig authConfig;

    private List<RegisterInfo> registerInfos;

    public WebServerVerticle(List<RegisterInfo> registerInfos) {
        this.registerInfos = registerInfos;
    }

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        config = context.config();

        // 如果有mongo_client配置,放入上下文当中.

        // For Debug - 默认关闭授权, 允许所有请求通过.
        boolean authEnabled = config.getBoolean(CONFIG_AUTHENTICATION, true);

        authSwitch = new AuthSwitch(authEnabled);
        authConfig = new AuthConfig();

        mainRouter = createMainRouter(vertx);
    }

    private Router createMainRouter(Vertx vertx) {
        return ShareableUtil.getMainRouter(vertx);
    }

    @Override
    public void start(Future<Void> future) {
        HttpServerOptions options = createOptions().setMaxWebsocketFrameSize(MAX_FRAME_SIZE);
        server = vertx.createHttpServer(options);
        server.requestHandler(configMainRouter()::accept);

        server.listen(result -> {
            if (result.succeeded()) {

                //startEventBusAPI();

                logger.info("WebServer state query address: " + options.getHost() + ":" + options.getPort()
                        + "/api/status");
                logger.info("WebServer start succeeded. Listening [" + options.getHost() + ":"
                        + options.getPort() + "]");

                future.complete();
            } else {
                future.fail(result.cause());
            }
        });
    }

    /**
     * 提供事件总线接口。 - 查询WebServer配置信息。
     */
    private void startEventBusAPI() {
        String webServerName = config.getString(CONFIG_WEBSERVER_NAME_KEY);
        String configAddress = null;
        if (webServerName != null) {
            configAddress = webServerName + ".configuration.get";
        } else {
            configAddress = "DefaultWebServerName.configuration.get";
        }

        logger.info("WebServer address of configuration inquiry: " + configAddress);
        vertx.eventBus().<JsonObject>consumer(configAddress, msg -> {
            msg.reply(config);
        });

        String updateConfigAddress = webServerName + ".configuration.put";
        logger.info("WebServer address of configuration modification: " + updateConfigAddress);

        vertx.eventBus().<JsonObject>consumer(updateConfigAddress, msg -> {

            JsonObject config = msg.body();

            if (logger.isInfoEnabled()) {
                logger.info("WebServer configuration's modification request: " + config);
            }

            Boolean enabled = config.getBoolean("auth.enabled");
            if (enabled != null) {
                if (enabled) {
                    if (logger.isInfoEnabled()) {
                        logger.info("WebServer's configuration has been modified: " + " turn on auth.");
                    }
                    authSwitch.turnOn();
                } else {
                    if (logger.isInfoEnabled()) {
                        logger.info("WebServer's configuration has been modified: " + " turn off auth.");
                    }
                    authSwitch.turnOff();
                }
            }

            String sessionQueryAddress = config.getString("auth.session_query_address");
            if (sessionQueryAddress != null) {
                authConfig.setSessionQueryAddress(sessionQueryAddress);
            }

            String loginUrl = config.getString("auth.login_url");
            if (loginUrl != null) {
                authConfig.setLoginUrl(loginUrl);
            }

            JsonArray securityUrls = config.getJsonArray("auth.security_urls");
            if (securityUrls != null) {
                authConfig.setSecurityUrls(securityUrls);
            }

            msg.reply(new JsonObject().put("errCode", 0).put("errMsg", "Success"));
        });
    }

    @Override
    public void stop(Future<Void> future) {
        if (server == null) {
            future.complete();
            logger.info("WebServer shut down succeeded.");
            return;
        }
        server.close(result -> {
            if (result.failed()) {
                logger.info("WebServer shut down failed. " + result.cause().getMessage());
                future.fail(result.cause());
            } else {
                logger.info("WebServer shut down succeeded.");
            }
        });
    }

    private HttpServerOptions createOptions() {
        HttpServerOptions options = new HttpServerOptions();
        // 从配置中读取端口号，如果未配置端口号，则默认为8081.
        // options.setHost("localhost");
        options.setPort(config().getInteger(CONFIG_HTTP_PORT_KEY, 8081));
        return options;
    }

    private Router configMainRouter() {

        // 解决跨域问题
        handlerCORS();

        // 配置错误处理
        mainRouter.route().failureHandler(ErrorHandler.create(true));

        // 处理消息体, BodyHandler需要放在SessionHandler之前.
        mainRouter.route().handler(BodyHandler.create());

    /* Session / cookies for users */
        mainRouter.route().handler(CookieHandler.create());

        SessionStore sessionStore = LocalSessionStore.create(vertx);
        SessionHandler sessionHandler = SessionHandler.create(sessionStore);
        mainRouter.route().handler(sessionHandler);

        // 处理API注册
        mainRouter.route("/api/*").handler(apiMessageHandler());

    /* API */
        mainRouter.mountSubRouter("/api", apiRouter());


        return mainRouter;
    }


    private void handlerCORS() {
        mainRouter.route().handler(
                CorsHandler.create("*").allowedMethod(HttpMethod.GET).allowedMethod(HttpMethod.POST)
                        .allowedMethod(HttpMethod.PUT).allowedMethod(HttpMethod.DELETE)
                        .allowedMethod(HttpMethod.OPTIONS).allowedHeader("X-PINGARUNER")
                        .allowedHeader("Content-Type"));

        mainRouter.route().handler(new AllowAllCorsHandlerImpl());
    }


    private EventMessageHandler apiMessageHandler() {
        if (registerInfos != null) {
            EventMessageHandler handler = EventMessageHandler.create(vertx);
            handler.bridge(registerInfos);
            return handler;
        } else {
            return null;
        }
    }

    private Router apiRouter() {

        Router router = Router.router(vertx);
        router.route().consumes("application/json");
        router.route().produces("application/json");

        router.route().handler(context -> {
            context.response().headers().add(CONTENT_TYPE, "application/json");
            context.next();
        });

    /* status / application status : no token needed */
        router.get("/status").handler(this::status);

        return router;
    }

    private void status(RoutingContext context) {
        HttpServerResponse response = context.response();
        String host = context.request().getHeader("Host");
        response.putHeader("content-type", "application/json; charset=utf-8");

        response.setChunked(true);
        response.setStatusCode(200);
        response.write("Server Host\t: " + host + "\n");
        response.end("Server Status\t: alive");
    }
}
