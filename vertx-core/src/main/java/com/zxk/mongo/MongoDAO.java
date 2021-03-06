package com.zxk.mongo;

import com.zxk.vertx.util.ConstantUtil;
import com.zxk.vertx.util.GlobalDataPool;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * zhangyef@yonyou.com on 2015-10-28.
 */
public class MongoDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDAO.class);

    private MongoClient client;

    private String RESTFUL_API_REG_INFO = ConstantUtil.RESTFUL_API_REG_INFO;

    private String SESSIONINFO = "sessionInfo";

    private boolean isConfigured = false;


    private static MongoDAO instance = null;

    private MongoDAO(Vertx vertx) {
        LOGGER.info("开始初始化mongo连接");
        if (GlobalDataPool.INSTANCE.containsKey(ConstantUtil.CONFIG_WEBSERVER_NAME_KEY)) {
            String webServerName =
                    GlobalDataPool.INSTANCE.<String>get(ConstantUtil.CONFIG_WEBSERVER_NAME_KEY);
            RESTFUL_API_REG_INFO = RESTFUL_API_REG_INFO + "_at_" + webServerName;
        }
        JsonObject config = GlobalDataPool.INSTANCE.<JsonObject>get("mongo_client_at_webserver");
        LOGGER.info("mongo连接配置 ,config={}", config);
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
            LOGGER.info("创建mongo连接失败，e={}", e);
            isConfigured = false;
        }
    }

    public static MongoDAO create(Vertx vertx) {
        if (instance == null) {
            instance = new MongoDAO(vertx);
        }
        return instance;
    }

    public void insert(String collection, JsonObject data, Handler<AsyncResult<String>> resultHandler) {
        // 如果没有配置mongoClient
        if (!isConfigured) {
            if (resultHandler != null) {
                resultHandler.handle(Future
                        .failedFuture("No MongoClient configured, unable to insert data."));
            }
            return;
        }
        client.insert(collection, data, resultHandler);
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

    public void find(String collection, JsonObject query, Handler<AsyncResult<List<JsonObject>>> resultHandler) {
        LOGGER.info("mongoDB查询入口，isConfigured={},query={}", isConfigured, query);
        if (!isConfigured) {
            if (resultHandler != null) {
                resultHandler.handle(Future
                        .failedFuture("No MongoClient configured, unable to query data."));
            }
            return;
        }
        try {
            LOGGER.info("mongoDB开始查询，collection={}", collection);
            client.find(collection, query, resultHandler);
            LOGGER.info("mongoDB查询结束,resultHandler");
        } catch (Exception e) {
            LOGGER.info("查询数据库异常，e={}", e);
            resultHandler.handle(Future
                    .failedFuture("query db exception ,e=" + e.getMessage()));

        }
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
