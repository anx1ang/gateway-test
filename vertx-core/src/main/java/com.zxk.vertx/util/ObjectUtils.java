package com.zxk.vertx.util;

/**
 * author xiaokangzheng
 * date 2018/3/6 下午6:36
 */
public class ObjectUtils {

    public static <T> void requireNonNull(T param, String desc) {
        if (param == null)
            throw new NullPointerException(desc);
    }
}
