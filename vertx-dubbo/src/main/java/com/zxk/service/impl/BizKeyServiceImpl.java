package com.zxk.service.impl;

import com.zxk.constance.C;
import com.zxk.entity.BizKey;
import com.zxk.exception.GatewayException;
import com.zxk.service.BizKeyService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * 密钥服务
 * <p>
 * Created by wangyi on 2016/11/17.
 */
@Service
public class BizKeyServiceImpl implements BizKeyService {

    private static Logger logger = LoggerFactory.getLogger(BizKeyServiceImpl.class);


    /**
     * 获取密钥列表
     *
     * @param bizCode
     * @return
     */
    @Override
    public BizKey getBizKey(final String bizCode, final String keyGroupName) throws GatewayException {
        String unionKey = C.BIZ_KEY + bizCode + keyGroupName;
       /* return bizKeyMapper.selectByBizCodeAndName(bizCode,keyGroupName);
        if (CollectionUtils.isEmpty(bizKeys)) {
            throw new GatewayException(ExceptionEnums.BIZ_KEY_NULL);
        }
        if(bizKeys.size()!=1){
            throw new GatewayException(ExceptionEnums.BIZ_KEY_MULTIPLE);
        }
        return bizKeys.get(0);*/
        return null;
    }


    /**
     * 通过ID查找明细
     *
     * @param id
     * @return
     */
    @Override
    public BizKey queryById(Integer id) {
        //return bizKeyMapper.selectByPrimaryKey(id);
        return null;
    }

}
