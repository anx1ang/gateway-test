package com.zxk.starter;

import com.zxk.starter.register.RegisterInfo;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * author xiaokangzheng
 * date 2018/3/7 上午10:36
 */
public class CommandResult {

    private JsonObject datas;

    public JsonObject getDatas() {
        return datas;
    }

    public void setDatas(JsonObject datas) {
        this.datas = datas;
    }
}
