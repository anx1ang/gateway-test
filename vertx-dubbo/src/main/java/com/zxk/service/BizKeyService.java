package com.zxk.service;


import com.zxk.entity.BizKey;
import com.zxk.exception.GatewayException;

import java.util.List;

/**
 * 密钥服务
 *
 * Created by wangyi on 2016/11/17.
 */
public interface BizKeyService {

    /**
     * 获取密钥
     * @param bizCode
     * @return
     */
    public BizKey getBizKey(String bizCode, String keyGroupName) throws GatewayException;


    /**
     * 通过ID查找明细
     * @param id
     * @return
     */
    public BizKey queryById(Integer id);
}
