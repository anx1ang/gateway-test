package com.zxk.entity;

/**
 * author xiaokangzheng
 * date 2018/3/6 下午7:31
 */
public class VerticleInfo {

    private String verticleName;

    private String className;

    private Object serviceInteface;

    public String getVerticleName() {
        return verticleName;
    }

    public void setVerticleName(String verticleName) {
        this.verticleName = verticleName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Object getServiceInteface() {
        return serviceInteface;
    }

    public void setServiceInteface(Object serviceInteface) {
        this.serviceInteface = serviceInteface;
    }
}
