package com.zxk.enums;

/**
 * author xiaokangzheng
 * date 2018/3/11 上午12:02
 */
public enum BooleanEnum {

    FALSE(0, "false"),
    TRUE(1, "true"),;

    private Integer value;
    private String desc;


    private BooleanEnum(Integer value, String desc) {

        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public boolean check(Integer value) {
        return getValue().equals(value);
    }
}
