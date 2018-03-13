package com.zxk.mongo;

import com.zxk.entity.RegisterInfo;
import com.zxk.enums.StatusEnum;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * author xiaokangzheng
 * date 2018/3/13 下午6:16
 */
public class RegisterInfoMongoHandler extends BaseMongoHandle<RegisterInfo> {


    public RegisterInfoMongoHandler(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }

    public List<RegisterInfo> queryNeedRegister() {
        Query query = new Query();
        Criteria criteria = where("status").is(StatusEnum.YES.getCode());
        query.addCriteria(criteria);
        return mongoTemplate.find(query, RegisterInfo.class);
    }

}
