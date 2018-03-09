package com.zxk.starter;

import com.google.common.collect.Lists;
import com.zxk.entity.VerticleInfo;
import com.zxk.starter.register.RegisterInfo;
import com.zxk.vertx.standard.StandardVertxUtil;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
        Map<String, Object> serviceCodeMap = (Map<String, Object>) context.getBean("serviceCodeMap");
        initService.init(serviceCodeMap);
        // 设置使用日志类型
        StandardVertxUtil.getStandardVertx(Vertx.vertx(new VertxOptions()));

        // 设置扫描器 api、handler(service)
        List<RegisterInfo> registerInfos = Lists.newArrayList();
        RegisterInfo registerInfoTest = new RegisterInfo();
        registerInfoTest.setAction("create");
        registerInfoTest.setAddress("zxktest");
        registerInfoTest.setUri("userAccountFacade");
        registerInfoTest.setClassName("com.qudian.pay.account.api.facade.UserAccountFacade");
        registerInfos.add(registerInfoTest);
        DeployVertxServer.startServer(registerInfos);
        DeployVertxServer.startDeploy(initService.getFacades(serviceCodeMap));
    }
}
