package com.zxk.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangyi on 2016/11/15.
 */
public class MethodMap {
    public static Map<String,MethodInfo> methodMap = new HashMap<String, MethodInfo>();

    public static Map<String, MethodInfo> getMethodMap() {
        return methodMap;
    }

    public static void setMethodMap(Map<String, MethodInfo> methodMap) {
        MethodMap.methodMap = methodMap;
    }
}
