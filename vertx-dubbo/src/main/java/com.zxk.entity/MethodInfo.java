package com.zxk.entity;

import java.lang.reflect.Method;

/**
 * Created by wangyi on 2016/11/15.
 */
public class MethodInfo {

    private String systemSource;

    private Object inteface;

    private Method methodName;

    private Class param;

    private Class resp;

    public Class getParam() {
        return param;
    }

    public void setParam(Class param) {
        this.param = param;
    }

    public Method getMethodName() {
        return methodName;
    }

    public void setMethodName(Method methodName) {
        this.methodName = methodName;
    }

    public Object getInteface() {
        return inteface;
    }

    public void setInteface(Object inteface) {
        this.inteface = inteface;
    }

    public Class getResp() {
        return resp;
    }

    public void setResp(Class resp) {
        this.resp = resp;
    }

    public String getSystemSource() {
        return systemSource;
    }

    public void setSystemSource(String systemSource) {
        this.systemSource = systemSource;
    }
}
