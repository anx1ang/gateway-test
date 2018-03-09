package com.zxk.enums;

/**
 * Created by wangyi on 2016/11/9.
 */
public enum WhiteIpStatus {
    DISABLE((byte)0),ENABLE((byte)1);

    private byte status;

    private WhiteIpStatus(Byte status){
        this.status = status;
    }

    public byte getStatus() {
        return status;
    }
}
