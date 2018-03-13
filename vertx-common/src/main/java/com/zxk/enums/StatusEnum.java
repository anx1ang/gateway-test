package com.zxk.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by fenglei on 2017/3/9.
 */
public enum StatusEnum {

    YES(1, "可用"),
    ;

    private String value;
    private Integer code;

    private StatusEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public int getCode() {
        return code;
    }

    public static Map<Object, Object> toMap() {
        Map<Object, Object> map = new LinkedHashMap<>();
        for (StatusEnum status : values()) {
            map.put(status.code,status.value);
        }
        return map;
    }
}
