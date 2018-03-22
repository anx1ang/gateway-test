package com.zxk.starter;

import com.zxk.entity.RegisterInfo;
import com.zxk.mongo.RegisterInfoMongoHandler;
import com.zxk.vertx.standard.StandardVertxUtil;
import com.zxk.vertx.util.GlobalDataPool;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Vertx main启动
 *
 * @ProjectName:
 * @Package: com.zc.test
 * @ClassName: StartRunner
 * @Description: Vertx main启动
 * @Author: Neil.Zhou
 * @CreateDate: 2017/9/21 12:37
 * @UpdateUser: Neil.Zhou
 * @UpdateDate: 2017/9/21 12:37
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2017/9/21</p>
 */
public class StartRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartRunner.class);

    private static final int port = 9011;


    public static void main(String[] args) throws Exception {

        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        InitService initService = (InitService) context.getBean("initService");
        RegisterInfoMongoHandler mongoHandler = (RegisterInfoMongoHandler) context.getBean("registerInfoMongoHandler");
        // 设置使用日志类型
        StandardVertxUtil.getStandardVertx(Vertx.vertx(new VertxOptions()));
        JsonObject mongo_client = new JsonObject();
        mongo_client.put("host", initService.getHost());
        mongo_client.put("port", Integer.valueOf(initService.getPort()));
        mongo_client.put("db_name", initService.getDbName());
        GlobalDataPool.INSTANCE.put("mongo_client_at_webserver", mongo_client);

        LOGGER.info("1.获取网关注册信息");
        List<RegisterInfo> registerInfo = mongoHandler.queryNeedRegister();
        LOGGER.info("2.获取网关注册信息成功，数量：{}", registerInfo.size());
        DeployVertxServer.startServer(registerInfo, port);
        DeployVertxServer.startDeploy();
        LOGGER.info("3.启动main执行完毕！");

    }
}
