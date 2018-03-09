package com.zxk.utils;

import com.zxk.constance.C;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.zxk.utils.Coder.decryptBASE64;
import static com.zxk.utils.Coder.encryptBASE64;

/**
 * Created by wangyi on 2016/12/11.
 */
public class RSAUtil {

    private static Logger logger = LoggerFactory.getLogger(RSAUtil.class);
    public static final String KEY_ALGORTHM = "RSA";//
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    public static final String PUBLIC_KEY = "RSAPublicKey";//公钥
    public static final String PRIVATE_KEY = "RSAPrivateKey";//私钥
    private static final Provider DEFAULT_PROVIDER = new BouncyCastleProvider();

    public static ConcurrentMap<String, RSAPublicKey> RSA_KEY_MAP;

    static {
        RSA_KEY_MAP = new ConcurrentHashMap<>();
    }

    /**
     * 初始化密钥
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORTHM);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        //公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);

        return keyMap;
    }

    /**
     * 取得公钥，并转化为String类型
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 取得私钥，并转化为String类型
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptBASE64(key.getEncoded());
    }

    public static RsaKeys initKey() throws Exception {
        Map<String, Object> map = getKey();
        RsaKeys rsaKeys = new RsaKeys();
        rsaKeys.setPublicKey(getPublicKey(map));
        rsaKeys.setPrivateKey(getPrivateKey(map));
        return rsaKeys;
    }

    /**
     * 用私钥加密
     *
     * @param data 加密数据
     * @param key  密钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
        //解密密钥
        byte[] keyBytes = decryptBASE64(key);
        //取私钥
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        //对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    /**
     * 用私钥解密 * @param data 	加密数据
     *
     * @param key 密钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
        //对私钥解密
        byte[] keyBytes = decryptBASE64(key);

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    /**
     * 用公钥加密
     *
     * @param data 加密数据
     * @param key  密钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
        //对公钥解密
        byte[] keyBytes = decryptBASE64(key);
        //取公钥
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }

    /**
     * 用公钥解密
     *
     * @param data 加密数据
     * @param key  密钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
        //对私钥解密
        byte[] keyBytes = decryptBASE64(key);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       //加密数据
     * @param privateKey //私钥
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        //解密私钥
        byte[] keyBytes = decryptBASE64(privateKey);
        //构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        //指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        //取私钥匙对象
        PrivateKey privateKey2 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        //用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey2);
        signature.update(data);

        return encryptBASE64(signature.sign());
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       //加密数据
     * @param privateKey //私钥
     * @return
     * @throws Exception
     */
    public static String sign(String data, String privateKey) throws Exception {
        return sign(data.getBytes(C.DEFAULT_CHAREST), privateKey);
    }

    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        try {
            Signature tSignature = Signature.getInstance(SIGNATURE_ALGORITHM, DEFAULT_PROVIDER);
            tSignature.initVerify(getPubKey(publicKey));
            tSignature.update(data);
            return tSignature.verify(Base64.decodeBase64(sign));
        } catch (Exception e) {
            logger.error("公钥验签异常", e);
        }
        return false;
    }

    private static RSAPublicKey getPubKey(String pubKeyStr) {
        RSAPublicKey pubkey = RSA_KEY_MAP.get(pubKeyStr);

        if (pubkey != null) {
            return pubkey;
        }

        try {
            pubKeyStr = StringUtils.split(pubKeyStr, " ")[1];
            byte[] decode = Base64.decodeBase64(pubKeyStr);
            byte[] sshrsa = new byte[]{0, 0, 0, 7, 's', 's', 'h', '-', 'r', 's', 'a'};
            int start_index = sshrsa.length;
            /* Decode the public exponent */
            int len = decodeUInt32(decode, start_index);
            start_index += 4;
            byte[] pe_b = new byte[len];
            for (int i = 0; i < len; i++) {
                pe_b[i] = decode[start_index++];
            }
            BigInteger pe = new BigInteger(pe_b);
            /* Decode the modulus */
            len = decodeUInt32(decode, start_index);
            start_index += 4;
            byte[] md_b = new byte[len];
            for (int i = 0; i < len; i++) {
                md_b[i] = decode[start_index++];
            }
            BigInteger md = new BigInteger(md_b);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            KeySpec ks = new RSAPublicKeySpec(md, pe);
            pubkey = (RSAPublicKey) keyFactory.generatePublic(ks);
            RSA_KEY_MAP.put(pubKeyStr, pubkey);
        } catch (Exception e) {
            logger.error("公钥证书初始化异常", e);
        }
        return pubkey;
    }

    private static int decodeUInt32(byte[] key, int start_index) {
        byte[] test = Arrays.copyOfRange(key, start_index, start_index + 4);
        return new BigInteger(test).intValue();
    }

    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(String data, String publicKey, String sign) throws Exception {
        return verify(data.getBytes(C.DEFAULT_CHAREST), publicKey, sign);
    }

    static class RsaKeys {
        private String publicKey;
        private String privateKey;

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
        }
    }
}
