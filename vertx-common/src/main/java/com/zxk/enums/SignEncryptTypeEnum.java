package com.zxk.enums;

/**
 * Created by wangyi on 2016/11/19.
 */
public enum  SignEncryptTypeEnum {
    MD5("MD5","MD5Util"),AES("AES","AESUtil");

    private String name;
    private String type;

    private SignEncryptTypeEnum(String name,String type){
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static SignEncryptTypeEnum getNameByType(String type){
        for(SignEncryptTypeEnum signEncryptTypeEnum:SignEncryptTypeEnum.values()){
            if(signEncryptTypeEnum.getType().equals(type))return signEncryptTypeEnum;
        }
        return null;
    }
}
