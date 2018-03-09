package com.zxk.utils;

import com.zxk.constance.C;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.Key;

/**
 * 3DES工具类
 * Created by wangyi on 2016/12/6.
 */
public class ThreeDESUtil {

    /**
     * 密钥算法
     * */
    public static final String KEY_ALGORITHM = "DESede";

    /**
     * 加密/解密算法/工作模式/填充方式
     * */
    public static final String CIPHER_ALGORITHM = "DESede/ECB/PKCS5Padding";


    /**
     * 加密数据
     * @param data 待加密数据
     * @param key 密钥
     * @return byte[] 加密后的数据
     * */
    public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 还原密钥
        Key k = toKey(key);
        // 实例化
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        // 初始化，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, k);
        // 执行操作
        return cipher.doFinal(data);
    }

    /**
     * 加密数据
     * @param data 待加密数据
     * @param key 密钥
     * @param charsetName 字符集
     * @return String 加密后的数据
     * */
    public static String encrypt2Str(String data, String key,String charsetName) throws Exception {
        if(StringUtils.isBlank(charsetName))charsetName = C.DEFAULT_CHAREST;
        return new String(encrypt(data.getBytes(charsetName),key.getBytes(charsetName)),charsetName);
    }

    /**
     * 解密数据
     * @param data 待解密数据
     * @param key 密钥
     * @return byte[] 解密后的数据
     * */
    public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 欢迎密钥
        Key k = toKey(key);
        // 实例化
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        // 初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, k);
        // 执行操作
        return cipher.doFinal(data);
    }

    /**
     * 解密数据
     * @param data 待解密数据
     * @param key 密钥
     * @param charsetName 字符集
     * @return byte[] 解密后的数据
     * */
    public static String decryptByStr(String data, String key,String charsetName) throws Exception {
        if(StringUtils.isBlank(charsetName))charsetName = C.DEFAULT_CHAREST;
       return new String(decrypt(data.getBytes(charsetName),key.getBytes(charsetName)),charsetName);
    }


    /**
     * 转换密钥
     *
     * @param key 二进制密钥
     * @return Key 密钥
     * */
    public static Key toKey(byte[] key) throws Exception {
        // 实例化Des密钥
        DESedeKeySpec dks = new DESedeKeySpec(key);
        // 实例化密钥工厂
        SecretKeyFactory keyFactory = SecretKeyFactory
                .getInstance(KEY_ALGORITHM);
        // 生成密钥
        SecretKey secretKey = keyFactory.generateSecret(dks);
        return secretKey;
    }


}
