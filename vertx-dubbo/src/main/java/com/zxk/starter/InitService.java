package com.zxk.starter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zxk.entity.FacadeInfo;
import com.zxk.entity.MethodInfo;
import com.zxk.entity.MethodMap;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangyi on 2016/11/15.
 */
@Component
public class InitService {

    @Autowired
    private String host;
    @Autowired
    private String port;
    @Autowired
    private String dbName;

    @Resource
    private Map<String, Object> serviceCodeMap;

    public void init() throws BeansException {
        System.out.println("开始加载自定义map");
        if (CollectionUtils.isEmpty(serviceCodeMap)) {
            System.out.println("自动以map为空,家加载结束");
            return;
        }
        Map<String, MethodInfo> methodMap = Maps.newHashMap();
        List<String> facadeInfo = Lists.newArrayList();

        for (String serviceName : serviceCodeMap.keySet()) {
            facadeInfo.add(serviceName);
            Object serviceInteface = serviceCodeMap.get(serviceName);
            Method[] methods = serviceInteface.getClass().getDeclaredMethods();
            for (Method method : methods) {
                String methodName = method.getName();
                if (methodName.equals("$echo")) continue;
                Class[] methodParams = method.getParameterTypes();
                Class resp = method.getReturnType();
                MethodInfo methodInfo = new MethodInfo();
                methodInfo.setInteface(serviceInteface);
                methodInfo.setMethodName(method);
                methodInfo.setParam(methodParams[0]);
                methodInfo.setResp(resp);
                methodMap.put(serviceName + "." + methodName, methodInfo);
                System.out.println("已加载:" + serviceName + "." + methodName);
            }
        }
        MethodMap.setFacadeInfo(facadeInfo);
        MethodMap.setMethodMap(methodMap);
        System.out.println("自定义map加载完毕");
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public Map<String, Object> getServiceCodeMap() {
        return serviceCodeMap;
    }

    public void setServiceCodeMap(Map<String, Object> serviceCodeMap) {
        this.serviceCodeMap = serviceCodeMap;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDbName() {
        return dbName;
    }
}
