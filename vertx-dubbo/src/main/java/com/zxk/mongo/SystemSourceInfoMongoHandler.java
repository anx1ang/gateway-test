package com.zxk.mongo;

import com.zxk.entity.SystemSourceInfo;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;


/**
 * author xiaokangzheng
 * date 2018/3/22 下午7:58
 */
public class SystemSourceInfoMongoHandler extends BaseMongoHandle<SystemSourceInfo> {

    public SystemSourceInfoMongoHandler(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }

    public List<SystemSourceInfo> queryAllSystemSource() {
        Query query = new Query();
        return mongoTemplate.find(query, SystemSourceInfo.class);
    }
}
