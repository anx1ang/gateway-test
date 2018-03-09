package com.zxk.utils;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * AES工具
 *
 * Created by wangyi on 2016/11/19.
 */
public class AESUtil {

    private static final String AESTYPE = "AES/ECB/PKCS5Padding";
    private static final String DEFAULT_CHARSET = "UTF-8";  //编码
    private static final String ALGORITHM = "AES"; //算法

    /**
     * 加密
     *
     * @param plainText
     * @param key       盐值
     * @return
     */
    public static String encrypt(String plainText, String key) throws Exception {
        return encrypt(plainText, key, DEFAULT_CHARSET);
    }

    /**
     * 加密
     *
     * @param plainText
     * @param keyStr    盐值
     * @param chartset  编码
     * @return
     */
    public static String encrypt(String plainText, String keyStr, String chartset) throws Exception {
        Key key = new SecretKeySpec(keyStr.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(AESTYPE);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypt = cipher.doFinal(plainText.getBytes(chartset));
        return new String(Base64.encodeBase64(encrypt),chartset);
    }

    /**
     * 解密
     *
     * @param encryptData
     * @param keyStr      盐值
     * @return
     */
    public static String decrypt(String encryptData, String keyStr) throws Exception {
        Key key = new SecretKeySpec(keyStr.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(AESTYPE);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypt = cipher.doFinal(Base64.decodeBase64(encryptData));
        return new String(decrypt,DEFAULT_CHARSET).trim();
    }

}
