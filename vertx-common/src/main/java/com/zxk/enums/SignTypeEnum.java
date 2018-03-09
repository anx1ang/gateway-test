package com.zxk.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by fenglei on 2017/3/9.
 */
public enum SignTypeEnum {

    MD5("MD5"),
    SHA1("SHA1"),
    SHA256("SHA256"),
    RSA("RSA");

    private String value;

    private SignTypeEnum(String value) {
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
        for (SignTypeEnum sign : values()) {
            map.put(sign.name(), sign.getValue());
        }
        return map;
    }
}
