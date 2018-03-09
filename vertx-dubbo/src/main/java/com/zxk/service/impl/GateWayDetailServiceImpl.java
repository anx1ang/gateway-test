package com.zxk.service.impl;

import com.zxk.constance.C;
import com.zxk.entity.GatewayDetail;
import com.zxk.enums.ExceptionEnums;
import com.zxk.enums.GeneralStatusEnums;
import com.zxk.exception.GatewayException;
import com.zxk.service.GatewayDetailService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * 网关接口详情
 * <p>
 * Created by wangyi on 2016/11/25.
 */
@Service
public class GateWayDetailServiceImpl implements GatewayDetailService {

    public static Logger logger = LoggerFactory.getLogger(GateWayDetailServiceImpl.class);


    /**
     * 通过接口名称获取接口code
     *
     * @param serviceName
     * @return
     */
    @Override
    public String getServiceCodeByName(String serviceName) throws GatewayException {
        GatewayDetail gatewayDetail = getActiveByName(serviceName);
        if (gatewayDetail == null || StringUtils.isBlank(gatewayDetail.getServiceCode())) {
            throw new GatewayException(ExceptionEnums.QUERY_SERVICE_CODE_NULL);
        }
        return gatewayDetail.getServiceCode();
    }

    /**
     * 通过接口名获取服务对象
     *
     * @param serviceName
     * @return
     */
    @Override
    public GatewayDetail getActiveByName(final String serviceName) {
        String unionKey = C.GATEWAY_SERVICE_NAME + serviceName;
        //return gatewayDetailMapper.getByName(serviceName, GeneralStatusEnums.AVAILABLE.status);

        return null;
    }


    /**
     * 查询服务列表
     *
     * @param gatewayDetail
     * @param pages
     * @param rows
     * @return
     * @throws GatewayException
     */
    @Override
    public List<GatewayDetail> queryFacade(GatewayDetail gatewayDetail, Integer pages, Integer rows) {
        if (pages == null || pages == 0) {
            pages = 1;
        }
        if (rows == null || rows == 0) {
            rows = 10;
        }
        int start = (pages - 1) * rows;
        int end = start + rows;
        // return gatewayDetailMapper.selectByParams(gatewayDetail,start,end);
        return null;
    }

    /**
     * 通过ID查找明细
     *
     * @param id
     * @return
     */
    @Override
    public GatewayDetail queryById(Integer id) {
        //gatewayDetailMapper.selectByPrimaryKey(id);
        return null;
    }

    /**
     * 查询服务是否可用
     *
     * @param id
     * @return
     */
    @Override
    public boolean check(Integer id) {
       /* GatewayDetail gatewayDetail = gatewayDetailMapper.selectByPrimaryKey(id);
        if (gatewayDetail != null && GeneralStatusEnums.AVAILABLE.status.equals(gatewayDetail.getStatus())) return true;
       */
       return false;
    }


}
