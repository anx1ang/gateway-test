package com.zxk.service;


import com.zxk.entity.BizInfo;
import com.zxk.exception.GatewayException;

import java.util.List;

/**
 * 业务线接口信息
 *
 * Created by wangyi on 2016/11/17.
 */
public interface BizInfoService {

    /**
     * 查询业务线信息(通过缓存)
     * @param bizCode 业务线编码
     */
    public BizInfo queryBizInfoByCache(String bizCode);

    /**
     * 查询业务线信息
     * @param bizInfo
     * @param pages
     * @param rows
     * @return
     */
    public List<BizInfo> queryBizInfo(BizInfo bizInfo, Integer pages, Integer rows);
    /**
     * 通过ID查找明细
     * @param id
     * @return
     */
    public BizInfo queryById(Integer id);

    /**
     * 查询业务线是否可用
     * @param id
     * @return
     */
    public boolean check(Integer id);
}
