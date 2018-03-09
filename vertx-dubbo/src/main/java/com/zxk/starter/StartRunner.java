package com.zxk.starter;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zxk.mongo.MongoDAO;
import com.zxk.starter.register.RegisterInfo;
import com.zxk.vertx.standard.StandardVertxUtil;
import com.zxk.vertx.util.GlobalDataPool;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

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


    public static void main(String[] args) throws Exception {

        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        InitService initService = (InitService) context.getBean("initService");

        // 设置使用日志类型
        StandardVertxUtil.getStandardVertx(Vertx.vertx(new VertxOptions()));

        JsonObject mongo_client = new JsonObject();
        mongo_client.put("host", initService.getHost());
        mongo_client.put("port", Integer.valueOf(initService.getPort()));
        mongo_client.put("db_name", initService.getDbName());
        GlobalDataPool.INSTANCE.put("mongo_client_at_webserver", mongo_client);

        MongoDAO mongoDAO = MongoDAO.create(StandardVertxUtil.getStandardVertx());
        mongoDAO.find(new JsonObject(), result -> {
            if (result.succeeded()) {
                List<RegisterInfo> registerInfo = JSON.parseArray(result.result().toString(), RegisterInfo.class);

                DeployVertxServer.startServer(registerInfo);
                try {
                    DeployVertxServer.startDeploy();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
