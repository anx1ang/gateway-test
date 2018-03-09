package com.zxk.starter;

import com.zxk.entity.MethodMap;
import com.zxk.info.VerticleInfoServiceImpl;
import com.zxk.starter.register.RegisterInfo;
import com.zxk.vertx.address.EventBusAddress;
import com.zxk.vertx.standard.StandardVertxUtil;
import io.vertx.core.DeploymentOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * 处理器注册工厂
 *
 * @ProjectName: vertx-core
 * @Package: org.rayeye.vertx.http
 * @ClassName: RegistryHandlersFactory
 * @Description: 处理器注册工厂
 * @Author: Neil.Zhou
 * @CreateDate: 2017/9/20 23:38
 * @UpdateUser: Neil.Zhou
 * @UpdateDate: 2017/9/20 23:38
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2017</p>
 */
public class RegistryHandlersFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeployVertxServer.class);

    private VerticleInfoServiceImpl verticleInfoService = new VerticleInfoServiceImpl();

    public static final String BASE_ROUTER = "dubboServer";

    private List<String> facadeInfos;

    public RegistryHandlersFactory() {
        Objects.requireNonNull(facadeInfos, "The router package address scan is empty.");
        this.facadeInfos = MethodMap.getFacadeInfo();
    }

    /**
     * verticle 服务注册
     *
     * @return void
     * @throws
     * @method registerVerticle
     * @author Neil.Zhou
     * @date 2017/9/21 0:23
     */
    public void registerVerticle() {
        LOGGER.info("Register Service Verticle...");
        String busAddressPrefix = "";
        for (String facadeName : facadeInfos) {
            try {
                busAddressPrefix = facadeName;
                if (busAddressPrefix.startsWith("/")) {
                    busAddressPrefix = busAddressPrefix.substring(1, busAddressPrefix.length());
                }
                if (!BASE_ROUTER.endsWith("/")) {
                    busAddressPrefix = BASE_ROUTER + "/" + busAddressPrefix;
                } else {
                    busAddressPrefix = BASE_ROUTER + busAddressPrefix;
                }

                if (busAddressPrefix.endsWith("/")) {
                    busAddressPrefix = busAddressPrefix.substring(0, busAddressPrefix.length() - 1);
                }
                if (busAddressPrefix.startsWith("/")) {
                    busAddressPrefix = busAddressPrefix.substring(1, busAddressPrefix.length());
                }
                /***** 每一个方法都部署一个verticle *****/
                LOGGER.info("[Method] The register processor address is {}", EventBusAddress.positiveFormate(busAddressPrefix));
                StandardVertxUtil.getStandardVertx().deployVerticle(new VerticleHandlerFactory(EventBusAddress.positiveFormate(busAddressPrefix)), new DeploymentOptions());

            } catch (Exception e) {
                LOGGER.error("The {} Verticle register Service is fail，{}", e, e.getMessage());
            }
        }
    }
}
