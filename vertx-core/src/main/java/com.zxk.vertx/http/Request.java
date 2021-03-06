package com.zxk.vertx.http;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

/**
 * <pre>
 * @File：Request
 * @Version：1.0.0
 * @Author：Neil.Zhou
 * @CreateDate：2016/9/30 11:52
 * @Modify：
 * @ModifyDate：2016/9/30
 * @Descript：描述
 * </pre>
 */
public class Request {
    String url;
    String uri;
    String path;
    String host;
    //RouteMethod method;
    String method;
    String sendMethod;//接收方法

    JsonObject params;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getSendMethod() {
        return sendMethod;
    }

    public void setSendMethod(String sendMethod) {
        this.sendMethod = sendMethod;
    }

    public Request(String url, String uri, String path, String host, String method, JsonObject params) {
        this.url = url;
        this.path = path;
        this.host = host;
        this.uri=uri;

        this.method = method;
        this.params=params;
        if(path!=null){
            String[] suffix= path.split("/+");
            if(suffix!=null&&suffix.length>=1){
                sendMethod=suffix[suffix.length-1];
            }
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public JsonObject getParams() {
        return params;
    }

    public void setParams(JsonObject params) {
        this.params = params;
    }

    @Override
    public String toString(){
        return Json.encode(this);
    }
}
