package com.zxk.mongo;


import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xiaowei on 17/11/29.
 */
public abstract class BaseMongoHandle<T> {


    public MongoTemplate mongoTemplate;

    public BaseMongoHandle(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 添加记录
     */
    public void insert(T record) {
        mongoTemplate.insert(record);
    }

    /**
     * 批量插入
     *
     * @param records
     * @param cls
     */
    public void batchInsert(List<T> records, Class cls) {
        mongoTemplate.insert(records, cls);
    }

    /**
     * 删除集合
     *
     * @param collectionName
     */
    public void dropCollection(String collectionName) {
        if (isCollectionExists(collectionName)) mongoTemplate.dropCollection(collectionName);
    }

    public boolean isCollectionExists(String collectionName) {
        return mongoTemplate.collectionExists(collectionName);
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
