package com.zxk.utils;

import com.zxk.constance.C;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * DES工具类
 * <p>
 * Created by wangyi on 2016/12/5.
 */
public class DESUtil {


    /**
     * 加密
     *
     * @param content 待加密内容
     * @param key     加密的密钥
     * @return
     */
    public static byte[] encrypt(String content, String key) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            byte[] result = cipher.doFinal(content.getBytes());
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     *
     * @param content     待加密内容
     * @param key         密钥
     * @param charsetName 字符集
     * @return
     */
    public static String encrypt2Str(String content, String key, String charsetName) {
        try {
            byte[] bytes = encrypt(content, key);
            if (StringUtils.isBlank(charsetName)) charsetName = C.DEFAULT_CHAREST;
            return new String(bytes, charsetName);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param content 待解密内容
     * @param key     解密的密钥
     * @return
     */
    public static String decrypt(byte[] content, String key) throws Exception {
        SecureRandom random = new SecureRandom();
        DESKeySpec desKey = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        byte[] result = cipher.doFinal(content);
        return new String(result);
    }

    /**
     * 解密
     *
     * @param content 待解密内容
     * @param key     解密的密钥
     * @return
     */
    public static String decrypt(String content, String key) throws Exception{
        return decrypt(content.getBytes(C.DEFAULT_CHAREST), key);
    }

    /**
     * 解密
     *
     * @param content     待解密内容
     * @param key         密钥
     * @param charsetName 字符集,默认为UTF-8
     * @return
     */
    public static String decryptByStr(String content, String key, String charsetName) {
        if (StringUtils.isBlank(charsetName)) charsetName = C.DEFAULT_CHAREST;
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            byte[] result = cipher.doFinal(content.getBytes(charsetName));
            return new String(result);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
