package com.zxk.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by fenglei on 2017/3/10.
 */
public enum IsValidateIpEnum {
    FALSE("false", "否"),
    ;

    private String value;
    private String desc;

    private IsValidateIpEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static Map<Object, Object> toMap() {
        Map<Object, Object> map = new LinkedHashMap<>();
        for (IsValidateIpEnum validateIp : values()) {
            map.put(validateIp.value,validateIp.desc);
        }
        return map;
    }
}
