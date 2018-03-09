package com.zxk.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by fenglei on 2017/3/9.
 */
public enum EncryptTypeEnum {

    AES("AES"),
    DES("DES"),
    _3DES("3DES");

    private String value;

    private EncryptTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static Map<Object, Object> toMap() {
        Map<Object, Object> map = new LinkedHashMap<>();
        for (EncryptTypeEnum encrypt : values()) {
            map.put(encrypt.name(), encrypt.getValue());
        }
        return map;
    }
}
