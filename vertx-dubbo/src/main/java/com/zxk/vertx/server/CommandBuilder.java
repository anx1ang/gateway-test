package com.zxk.vertx.server;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.ext.web.RoutingContext;

/**
 * author xiaokangzheng
 * date 2018/3/7 上午10:34
 */
public class CommandBuilder {

    private static final int TIME_OUT = 60000;


    public static DeliveryOptions createDeliveryOptions(RoutingContext context) {
        return new DeliveryOptions().setSendTimeout(TIME_OUT);
    }
}
