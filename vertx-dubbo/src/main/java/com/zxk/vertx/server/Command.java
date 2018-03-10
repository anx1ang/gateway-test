package com.zxk.vertx.server;

import com.zxk.entity.RegisterInfo;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * author xiaokangzheng
 * date 2018/3/7 上午10:36
 */
public class Command {

    private RegisterInfo registerMessage;

    private JsonObject requestBody;



    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public Command(RegisterInfo registerMessage, JsonObject requestBody) {
        this.registerMessage = registerMessage;
        this.requestBody = requestBody;
    }

    public void execute(Vertx vertx, DeliveryOptions options,  Handler<AsyncResult<Message<JsonObject>>> messageHandler) {

    }
}
