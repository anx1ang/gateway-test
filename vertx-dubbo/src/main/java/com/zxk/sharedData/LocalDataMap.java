package com.zxk.sharedData;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zxk.entity.MethodInfo;
import com.zxk.entity.SystemSourceInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by wangyi on 2016/11/15.
 */
public class LocalDataMap {
    private static Map<String, MethodInfo> methodMap = Maps.newHashMap();

    private static Map<String, SystemSourceInfo> systemSourceInfoMap = Maps.newHashMap();

    private static List<String> facadeInfo = Lists.newArrayList();

    public static Map<String, MethodInfo> getMethodMap() {
        return methodMap;
    }

    public static void setMethodMap(Map<String, MethodInfo> methodMap) {
        LocalDataMap.methodMap = methodMap;
    }

    public static List<String> getFacadeInfo() {
        return facadeInfo;
    }

    public static void setFacadeInfo(List<String> facadeInfo) {
        LocalDataMap.facadeInfo = facadeInfo;
    }

    public static Map<String, SystemSourceInfo> getSystemSourceInfoMap() {
        return systemSourceInfoMap;
    }

    public static void setSystemSourceInfoMap(Map<String, SystemSourceInfo> systemSourceInfoMap) {
        LocalDataMap.systemSourceInfoMap = systemSourceInfoMap;
    }
}
