package com.zxk.mongo;

import com.zxk.vertx.util.ConstantUtil;
import com.zxk.vertx.util.GlobalDataPool;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;
import java.util.Objects;

/**
 * zhangyef@yonyou.com on 2015-10-28.
 */
public class MongoDAO {

    private MongoClient client;

    private String RESTFUL_API_REG_INFO = "ApiRegInfo";

    private String SESSIONINFO = "sessionInfo";

    private boolean isConfigured = false;


    private static MongoDAO instance = null;

    private MongoDAO(Vertx vertx) {
        if (GlobalDataPool.INSTANCE.containsKey(ConstantUtil.CONFIG_WEBSERVER_NAME_KEY)) {
            String webServerName =
                    GlobalDataPool.INSTANCE.<String>get(ConstantUtil.CONFIG_WEBSERVER_NAME_KEY);
            RESTFUL_API_REG_INFO = RESTFUL_API_REG_INFO + "_at_" + webServerName;
        }

        JsonObject config = GlobalDataPool.INSTANCE.<JsonObject>get("mongo_client_at_webserver");


        if (config != null) {
            // Objects.requireNonNull(mongo_client.getString("host"), "mongo_client配置中没有包含host字段.");
            // Objects.requireNonNull(mongo_client.getInteger("port"), "mongo_client配置中没有包含port字段.");
            isConfigured = true;
        } else {
            isConfigured = false;
            client = null;
            return;
        }

        try {
            client = MongoClient.createShared(vertx, config);
        } catch (Exception e) {
            isConfigured = false;
        }
    }

    public static MongoDAO create(Vertx vertx) {
        if (instance == null) {
            instance = new MongoDAO(vertx);
        }
        return instance;
    }

    public void insert(JsonObject data, Handler<AsyncResult<String>> resultHandler) {
        // 如果没有配置mongoClient
        if (!isConfigured) {
            if (resultHandler != null) {
                resultHandler.handle(Future
                        .failedFuture("No MongoClient configured, unable to insert data."));
            }
            return;
        }
        client.insert(RESTFUL_API_REG_INFO, data, resultHandler);
    }


    public void findOne(JsonObject query, Handler<AsyncResult<JsonObject>> resultHandler) {
        if (!isConfigured) {
            if (resultHandler != null) {
                resultHandler.handle(Future
                        .failedFuture("No MongoClient configured, unable to query data."));
            }
            return;
        }
        client.findOne(RESTFUL_API_REG_INFO, query, new JsonObject(), resultHandler);
    }

    public void find(JsonObject query, Handler<AsyncResult<List<JsonObject>>> resultHandler) {
        if (!isConfigured) {
            if (resultHandler != null) {
                resultHandler.handle(Future
                        .failedFuture("No MongoClient configured, unable to query data."));
            }
            return;
        }
        client.find(RESTFUL_API_REG_INFO, query, resultHandler);
    }

    /**
     * 移除符合条件的所有用户数据.
     *
     * @param condition
     * @param resultHandler
     */
    public void delete(JsonObject condition, Handler<AsyncResult<Void>> resultHandler) {
        if (!isConfigured) {
            if (resultHandler != null) {
                resultHandler.handle(Future
                        .failedFuture("No MongoClient configured, unable to delete data."));
            }
            return;
        }
        Handler<AsyncResult<Void>> removeHandler = (ret) -> resultHandler.handle(ret);
        client.remove(RESTFUL_API_REG_INFO, condition, removeHandler);
    }

    public void deleteById(String id, Handler<AsyncResult<Void>> resultHandler) {
        if (!isConfigured) {
            if (resultHandler != null) {
                resultHandler.handle(Future
                        .failedFuture("No MongoClient configured, unable to delete data by _id."));
            }
            return;
        }
        delete(new JsonObject().put("_id", id), resultHandler);
    }
}
