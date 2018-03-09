package com.zxk.info;

import com.google.common.collect.Lists;
import com.zxk.entity.VerticleInfo;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * author xiaokangzheng
 * date 2018/3/6 下午6:54
 */
public class VerticleInfoServiceImpl {

    @Resource(name = "serviceCodeMap")
    private Map<String, Object> serviceCodeMap;

    public List<VerticleInfo> getAllFacede() {
        List<VerticleInfo> verticleInfos = Lists.newArrayList();
        for (String serviceName : serviceCodeMap.keySet()) {
            Object serviceInteface = serviceCodeMap.get(serviceName);
            VerticleInfo verticleInfo = new VerticleInfo();
            verticleInfo.setVerticleName(serviceName);
            verticleInfo.setClassName(serviceInteface.getClass().getName());
            verticleInfo.setServiceInteface(serviceInteface);
        }
        return verticleInfos;
    }
}
