package com.zxk.service.impl;

import com.zxk.constance.C;
import com.zxk.entity.BizInfo;
import com.zxk.enums.ExceptionEnums;
import com.zxk.enums.GeneralStatusEnums;
import com.zxk.exception.GatewayException;
import com.zxk.service.BizInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * 业务线接口信息
 * <p>
 * Created by wangyi on 2016/11/17.
 */
@Service
public class BizInfoServiceImpl implements BizInfoService {

    private static final Logger logger = LoggerFactory.getLogger(BizInfoServiceImpl.class);

    /**
     * 查询业务线信息(通过缓存)
     *
     * @param bizCode 业务线编码
     */
    @Override
    public BizInfo queryBizInfoByCache(final String bizCode) {
        String unionKey = C.BIZ_INFO + bizCode;
        //return bizInfoMapper.selectByBizCode(bizCode, GeneralStatusEnums.AVAILABLE.status);
        return null;
    }


    /**
     * 查询业务线信息
     *
     * @param bizInfo
     * @param pages
     * @param rows
     * @return
     */
    @Override
    public List<BizInfo> queryBizInfo(BizInfo bizInfo, Integer pages, Integer rows) {
        if (pages == null || pages == 0) {
            pages = 1;
        }
        if (rows == null || rows == 0) {
            rows = 10;
        }
        //return bizInfoMapper.queryByParam(bizInfo, (pages - 1) * rows, rows);
        return null;
    }

    /**
     * 通过ID查找明细
     *
     * @param id
     * @return
     */
    @Override
    public BizInfo queryById(Integer id) {
        //bizInfoMapper.selectByPrimaryKey(id);
        return null;
    }

    /**
     * 查询业务线是否可用
     *
     * @param id
     * @return
     */
    @Override
    public boolean check(Integer id) {
        // BizInfo bizInfo = bizInfoMapper.selectByPrimaryKey(id);
        //if (bizInfo != null && GeneralStatusEnums.AVAILABLE.status.equals(bizInfo.getStatus())) return true;
        return false;
    }
}
