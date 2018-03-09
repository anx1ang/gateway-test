package com.zxk.utils;

import com.zxk.constance.C;
import com.zxk.enums.ExceptionEnums;
import com.zxk.exception.GatewayException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 验证服务
 *
 * Created by wangyi on 2016/12/11.
 */
public class VerfyUtil {

    private static Logger logger = LoggerFactory.getLogger(VerfyUtil.class);

    /**
     * 验签
     *
     * @param signType
     * @param context
     * @param sign
     * @param key
     * @return
     * @throws GatewayException
     */
    public static boolean verfySign(String signType, String context, String sign, String key) throws GatewayException {
        if(StringUtils.isBlank(sign) || StringUtils.isBlank(context))return false;
        try {
            if("MD5".equals(signType)){
                return sign.equals(MD5Util.sign(context,key));
            }
            if("SHA1".equals(signType)){
                return sign.equals(SHA1Util.sign(context,key));
            }
            if("SHA256".equals(signType)){
                return sign.equals(SHA256Util.sign(context,key));
            }
            if("RSA".equals(signType)){
//                context = "{\"orderNo\":\"6728189\",\"organizationId\":\"BANK001\",\"partnerId\":\"QUDIAN001\",\"ts\":1503047581306}";
//                sign = "ZP2+PtGPv0nQLWDJ5Ky3ib/5pijw58Ja8QfgAW9Garuq8oUFvNNa2CtlmAibRQf5q8eDb8iCYdCPsqgMArt7XUpqgV+keFpSES39gNytbOcNz/yNsV2P15ZyR41qIeONDcb9xlb8fA1nS7Ed3l9tuS/vvEtPKkDgC0LVN5JPMsc/fThc7JVB6NvG0BU8Ex2rkCQe6VQoXYwG0Ry3qUXqrnjr8kNPs3LbNzNnHBVCL1ZANaBAhUVkkmBKwa8phy42BvzMP2ucggz23IsvPB9jaO8OJf6lspoeVssX8ynnGWI5xoikBIvGhQmTaAn1TjxlQV3J4PW9JtZCVgZDnEA0JQ==";
//                key = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC/q2uDZffupzgq8Z8gN7s4ZGahrrPamdc/uLb6QpeVm1nBptdCRVNRxyORwvQwBjQ9DsgChjNB0kToy8i1Jquu9tMceKSi3tW3MzZ2mmla0GC9xSKTcy3lOZFjMH6Iyw+KWQqfqNeKKaeFlRHZ3wT2+5+4zrI18X3x70+jDNXXfG1Ra9lE9TIR8LAh90Ugkdkqh7wmWSd1rGAwRzHxT0DJgyde1UBuyx4wa3wMx3IhT5Oqr5BK2MHjeL9dhzXf3SLHY6Dh83wbup6XoR6wOxQbZsor2pZ8iSxSoAzE18puFH8kXAybV+gk/yZk+8WNkqn1q99/lLkOIQyJWrEy+YxF gaoxiang@office";
                return RSAUtil.verify(context,key,sign);
            }
            logger.info("不支持的加签方式{}",signType);
        }catch (Exception e){
            logger.error("验签异常,signType="+signType+",context="+context+",sign="+sign+",key="+key,e);
            throw new GatewayException(ExceptionEnums.UNSIGN_ERROR);
        }
        return false;
    }

    /**
     * 加签
     *
     * @param signType
     * @param context
     * @param key
     * @return
     * @throws GatewayException
     */
    public static String sign(String signType, String context, String key) throws GatewayException {
        if(StringUtils.isBlank(context))return null;
        try {
            if("MD5".equals(signType)){
                return MD5Util.sign(context,key);
            }
            if("SHA1".equals(signType)){
                return SHA1Util.sign(context,key);
            }
            if("SHA256".equals(signType)){
                return SHA256Util.sign(context,key);
            }
            logger.info("不支持的加签方式{}",signType);
        }catch (Exception e){
            logger.error("验签异常,signType="+signType+",context="+context+",key="+key,e);
            throw new GatewayException(ExceptionEnums.UNSIGN_ERROR);
        }
        return null;
    }

    /**
     * 解密
     *
     * @param encryptType
     * @param context
     * @param key
     * @return
     * @throws GatewayException
     */
    public static String decrypt(String encryptType, String context, String key) throws GatewayException {
        if(StringUtils.isBlank(context) || StringUtils.isBlank(key))return null;
        try {
            if("AES".equals(encryptType)){
                return AESUtil.decrypt(context,key);
            }
            if("DES".equals(encryptType)){
                return DESUtil.decrypt(context,key);
            }
            if("3DES".equals(encryptType)){
                return ThreeDESUtil.decryptByStr(context,key, C.DEFAULT_CHAREST);
            }
            if("BASE64".equals(encryptType)){
                return Base64Util.decode(context,"UTF8");
            }
            if("BASE64_GBK".equals(encryptType)){
                return Base64Util.decode(context,"GBK");
            }
            if ("TEXT".equals(encryptType)) {
                return context;
            }
        }catch (Exception e){
            logger.error("解密异常,encryptType="+encryptType+",context="+context+",key="+key,e);
            throw new GatewayException(ExceptionEnums.DECRYPT_ERROR);
        }
        return null;
    }

    /**
     * 加密
     *
     * @param encryptType
     * @param context
     * @param key
     * @return
     * @throws GatewayException
     */
    public static String encrypt(String encryptType, String context, String key) throws GatewayException {
        if(StringUtils.isBlank(context) || StringUtils.isBlank(key))return null;
        try {
            if("AES".equals(encryptType)){
                return AESUtil.encrypt(context,key);
            }
            if("DES".equals(encryptType)){
                return DESUtil.encrypt2Str(context,key,C.DEFAULT_CHAREST);
            }
            if("3DES".equals(encryptType)){
                return ThreeDESUtil.encrypt2Str(context,key, C.DEFAULT_CHAREST);
            }
            if("BASE64".equals(encryptType)){
                return Base64Util.encode(context,"UTF8");
            }
            if("BASE64_GBK".equals(encryptType)){
                return Base64Util.encode(context,"GBK");
            }
            if ("TEXT".equals(encryptType)) {
                return context;
            }
        }catch (Exception e){
            logger.error("加密异常,encryptType="+encryptType+",context="+context+",key="+key,e);
            throw new GatewayException(ExceptionEnums.DECRYPT_ERROR);
        }
        return null;
    }
}
