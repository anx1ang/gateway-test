package com.zxk.entity;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangyi on 2016/11/15.
 */
public class MethodMap {
    private static Map<String, MethodInfo> methodMap = new HashMap<String, MethodInfo>();

    private static List<String> facadeInfo = Lists.newArrayList();

    public static Map<String, MethodInfo> getMethodMap() {
        return methodMap;
    }

    public static void setMethodMap(Map<String, MethodInfo> methodMap) {
        MethodMap.methodMap = methodMap;
    }

    public static List<String> getFacadeInfo() {
        return facadeInfo;
    }

    public static void setFacadeInfo(List<String> facadeInfo) {
        MethodMap.facadeInfo = facadeInfo;
    }
}
