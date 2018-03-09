package com.zxk.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存工具
 *
 * Created by wangyi on 2016/11/17.
 */
public class GuavaCacheUtils {
    private static Logger logger = LoggerFactory.getLogger(GuavaCacheUtils.class) ;

    /**
     * 数据库路由缓存，存储5分钟
     */
    private static Cache<Object,Object> dbCache = CacheBuilder.newBuilder().maximumSize(2000).expireAfterWrite(5, TimeUnit.SECONDS).build();

    private static Cache<Object,Object> ssCache = CacheBuilder.newBuilder().maximumSize(5000).build();

    /**
     * 数据库缓存
     * @param k
     * @param cable
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <K,V> V getDbCache(K k,Callable<V> cable){
        try {
            return (V) dbCache.get(k, cable);
        } catch (Throwable t) {
            logger.error("GuavaCacheUtil | getDbCache | error", t);
        }
        return null;
    }

    /**
     * 安全存储缓存
     * @param k
     * @param cable
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <K,V> V getSSCache(K k,Callable<V> cable){
        try {
            return (V) ssCache.get(k, cable);
        } catch (Throwable t) {
            logger.error("GuavaCacheUtil | getSSCache | error", t);
        }
        return null;
    }
}
