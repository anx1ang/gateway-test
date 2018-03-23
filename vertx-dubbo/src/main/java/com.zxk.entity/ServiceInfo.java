package com.zxk.entity;

/**
 * author xiaokangzheng
 * date 2018/3/22 下午7:47
 */
public class ServiceInfo {
    private SystemSourceInfo systemSourceInfo;

    private RegisterInfo registerInfo;

    public SystemSourceInfo getSystemSourceInfo() {
        return systemSourceInfo;
    }

    public void setSystemSourceInfo(SystemSourceInfo systemSourceInfo) {
        this.systemSourceInfo = systemSourceInfo;
    }

    public RegisterInfo getRegisterInfo() {
        return registerInfo;
    }

    public void setRegisterInfo(RegisterInfo registerInfo) {
        this.registerInfo = registerInfo;
    }
}
