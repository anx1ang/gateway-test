package com.zxk.starter;

import com.google.common.collect.Lists;
import com.zxk.entity.FacadeInfo;
import com.zxk.entity.MethodInfo;
import com.zxk.entity.MethodMap;
import org.springframework.beans.BeansException;
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

    public void init(Map<String, Object> serviceCodeMap) throws BeansException {
        System.out.println("开始加载自定义map");
        if (CollectionUtils.isEmpty(serviceCodeMap)) {
            System.out.println("自动以map为空,家加载结束");
            return;
        }
        Map<String, MethodInfo> methodMap = new HashMap<String, MethodInfo>();
        for (String serviceName : serviceCodeMap.keySet()) {
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
        MethodMap.setMethodMap(methodMap);
        System.out.println("自定义map加载完毕");
    }

    public List<String> getFacades(Map<String, Object> serviceCodeMap) throws BeansException {
        System.out.println("开始加载自定义map");
        if (CollectionUtils.isEmpty(serviceCodeMap)) {
            System.out.println("自动以map为空,家加载结束");
            return null;
        }
        List<String> facades = Lists.newArrayList();
        facades.addAll(serviceCodeMap.keySet());
        System.out.println("自定义map加载完毕");
        return facades;
    }
}
