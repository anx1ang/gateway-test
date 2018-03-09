package com.zxk.starter;

import io.vertx.core.json.JsonObject;

/**
 * author xiaokangzheng
 * date 2018/3/7 上午10:36
 */
public class CommandReq extends JsonObject {

    private final static String METHOD = "method";

    private final static String REQUESTBODY = "requestBody";

    public CommandReq(String method, JsonObject requestBody) {
        put(METHOD, method);
        put(REQUESTBODY, requestBody);
    }

    public String getMethod() {
        return getString(METHOD);
    }

    public JsonObject getRequestBody() {
        return getJsonObject(REQUESTBODY);
    }

    public static CommandReq buildCommand(String method, JsonObject requestBody) {
        return new CommandReq(method, requestBody);
    }

    public static CommandReq buildCommand(JsonObject jsonObject) {
        return buildCommand(jsonObject.getString(METHOD), jsonObject.getJsonObject(REQUESTBODY));
    }
}
