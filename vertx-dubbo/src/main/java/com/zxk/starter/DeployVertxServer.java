package com.zxk.starter;

import com.zxk.entity.RegisterInfo;
import com.zxk.vertx.server.WebServerVerticle;
import com.zxk.vertx.standard.StandardVertxUtil;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * 开始注册vertx相关服务
 *
 * @ProjectName: vertx-core
 * @Package: org.rayeye.vertx
 * @ClassName: DeployVertxServer
 * @Description: Describes the function of the class
 * @Author: Neil.Zhou
 * @CreateDate: 2017/9/21 10:15
 * @UpdateUser: Neil.Zhou
 * @UpdateDate: 2017/9/21 10:15
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2017/9/21</p>
 */
public class DeployVertxServer {

    private static final String CONFIG_HTTP_PORT_KEY = "http.port";

    private static final Logger LOGGER = LoggerFactory.getLogger(DeployVertxServer.class);

    public static void startServer(List<RegisterInfo> registerInfo, Integer port) {
        LOGGER.info("Start Deploy WebServerVerticle....,prot={}", port);

        JsonObject deployConfig = new JsonObject();
        deployConfig.put(CONFIG_HTTP_PORT_KEY, port);
        DeploymentOptions options = new DeploymentOptions().setConfig(deployConfig);
        StandardVertxUtil.getStandardVertx().deployVerticle(new WebServerVerticle(registerInfo), options);
    }

    public static void startDeploy() {
        LOGGER.info("Start Deploy....");
        //StandardVertxUtil.getStandardVertx().deployVerticle(new RouterRegistryHandlersFactory(port));
        new RegistryHandlersFactory().registerVerticle();
    }
}
