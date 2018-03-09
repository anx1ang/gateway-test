package com.zxk.service;

import com.zxk.entity.GatewayDetail;
import com.zxk.exception.GatewayException;

import java.util.List;

/**
 * 网关接口详情
 * <p>
 * Created by wangyi on 2016/11/25.
 */
public interface GatewayDetailService {

    /**
     * 通过接口名称获取接口code
     *
     * @param serviceName
     * @return
     */
    public String getServiceCodeByName(String serviceName) throws GatewayException;

    /**
     * 通过接口名获取服务对象
     *
     * @param serviceName
     * @return
     */
    public GatewayDetail getActiveByName(String serviceName);

    /**
     * 查询服务列表
     *
     * @param gatewayDetail
     * @param pages
     * @param rows
     * @return
     * @throws GatewayException
     */
    public List<GatewayDetail> queryFacade(GatewayDetail gatewayDetail, Integer pages, Integer rows);


    /**
     * 通过ID查找明细
     *
     * @param id
     * @return
     */
    public GatewayDetail queryById(Integer id);

    /**
     * 查询服务是否可用
     *
     * @param id
     * @return
     */
    public boolean check(Integer id);
}
