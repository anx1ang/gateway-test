package com.zxk.utils;



import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by qufenqi-xun on 16/5/17.
 * <p>
 * Http HttpUtil
 */
public class HttpUtil extends HttpRequest {

    public final static String DEFAULT_CHARSET = "UTF-8";

    public final static String ACCEPT = "*/*,text/html";

    public static HttpResponse get(String url, int timeout,
                                   Map<String, String> query) {
        return httpMethod("GET", url, timeout, null, query, null, null);
    }

    public static HttpResponse post(String url, int timeout,
                                    Map<String, String> query,
                                    Map<String, Object> form) {
        return httpMethod("POST", url, timeout, null, query, form, null);
    }

    public static HttpResponse post(String url, int timeout,
                                    Map<String, String> query,
                                    String body) {
        return httpMethod("POST", url, timeout, null, query, null, body.getBytes());
    }

    public static HttpResponse httpMethod(String method, String url,int timeout,String content){
        return httpMethod(method,url,timeout,null,null,null,content.getBytes());
    }

    public static HttpResponse httpMethod(String method, String url,
                                          int timeout,
                                          Map<String, String> header,
                                          Map<String, String> query,
                                          Map<String, Object> form,
                                          byte[] body) {
        boolean isPost = "PUT".equalsIgnoreCase(method)
                || "POST".equalsIgnoreCase(method)
                || "OPTIONS".equalsIgnoreCase(method);
        String host = URI.create(url).getHost();
        int port = URI.create(url).getPort();
        if (port > 0) host = String.format("%s:%d", host, port);
        HttpRequest httpRequest = httpMethod(method, url)
                .timeout(timeout).connectionTimeout(timeout)
                .charset(DEFAULT_CHARSET);
        if (header != null && !header.isEmpty()) {
            setHeader(httpRequest,header);
        }
        if (query != null && !query.isEmpty()) {
            httpRequest.query(query);
        }
        if (isPost && body != null && body.length > 0) {
            String type = httpRequest.contentType();
            httpRequest.body(body, type);
        } else if (isPost && form != null && !form.isEmpty()) {
            httpRequest.form(form);
        }
        return httpRequest
                .header("host", host, true)
                .connectionKeepAlive(true)
                .send();
    }

    protected static HttpRequest httpMethod(String method, String url) {
        return new HttpRequest()
                .method(method)
                .set(url);
    }

    protected static void setHeader(HttpRequest httpRequest, Map<String, String> header) {
        for (Map.Entry<String, String> entry : header.entrySet()) {
            String headerName = entry.getKey().toLowerCase();
            if (!"host".equalsIgnoreCase(headerName)&&!"origin".equalsIgnoreCase(headerName)&&!"referer".equalsIgnoreCase(headerName))
                httpRequest.header(headerName, entry.getValue(), true);
        }
        String encoding = httpRequest.contentEncoding();
        if (encoding == null || encoding.isEmpty()) encoding = DEFAULT_CHARSET;
        String accept = httpRequest.accept();
        if (accept == null || accept.isEmpty()) accept = ACCEPT;
        httpRequest.accept(ACCEPT + ";charset=" + encoding + ";" + accept);
    }

    public static Map<String,String> parseQuery(String query) {

        Map<String,String> queryMap = new HashMap<String,String>();

        int ndx, ndx2 = 0;
        while (true) {
            ndx = query.indexOf('=', ndx2);
            if (ndx == -1) {
                if (ndx2 < query.length()) {
                    queryMap.put(query.substring(ndx2), "");
                }
                break;
            }
            String name = query.substring(ndx2, ndx);

            ndx2 = ndx + 1;

            ndx = query.indexOf('&', ndx2);

            if (ndx == -1) {
                ndx = query.length();
            }

            String value = query.substring(ndx2, ndx);


            queryMap.put(name, value);

            ndx2 = ndx + 1;
        }

        return queryMap;
    }

    public static String getKeyGroup(String url){
        int start = url.indexOf("keyGroupName");
        if(start==-1) return null;
        int end = url.indexOf("&",start);
        if(end==-1)end = url.length();
        return url.substring(start+13,end);
    }

}
