package com.zxk.enums;

/**
 * Created by wangyi on 2016/11/17.
 */
public enum  GeneralStatusEnums {
    AVAILABLE((byte)0),UNAVAILABLE((byte)1);

    public final Byte status;

    GeneralStatusEnums(Byte status){
        this.status = status;
    }
}
