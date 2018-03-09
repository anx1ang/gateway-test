package com.zxk.utils;

import com.zxk.constance.C;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * @Remark see class name
 * @Author pangyiyang
 * @Date 16/5/17 下午2:48
 */
public class AESNotifyUtil {

    private static final String AESTYPE = "AES/ECB/PKCS5Padding";


    public static String decryptURL(String encryptData,String bizCode) {
        byte[] decrypt = null;
        String str = "";
        try {
            Key key = null;
            if(C.BIZ_CODE_LAIFENQI.equals(bizCode)){
                key = generateKey(C.LAIFENQI_KEY);
            }else if(C.BIZ_CODE_QUFENQI.equals(bizCode)){
                key = generateKey(C.QUFENQI_KEY);
            }else if(C.BIZ_CODE_CHUNMIAN.equals(bizCode)){
                key = generateKey(C.CHUNMIAN_KEY);
            }else if(C.BIZ_CODE_QUHUISHOU.equals(bizCode)){
                key = generateKey(C.QUHUISHOU_KEY);
            }
            Cipher cipher = Cipher.getInstance(AESTYPE);
            cipher.init(Cipher.DECRYPT_MODE, key);
//            encryptData = URLDecoder.decode(encryptData, "utf-8");
            decrypt = cipher.doFinal(Base64.decodeBase64(encryptData.getBytes("utf-8")));
            str = new String(decrypt,"utf-8").trim();
        } catch (Exception e) {
            throw new RuntimeException("解密失败");
        }
        return str;
    }

    private static Key generateKey(String key) throws Exception {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), C.DEFAULT_MDTYPE);
            return keySpec;
        } catch (Exception e) {
            throw new RuntimeException("获取SecretKeySpec 失败");
        }

    }

    public static void main(String[] args) {
        //同步
        String s = "FEcWkW8W2%2FwQ72EclA7AnRkmqRnArwognzJ3U%2Bg%2FNWp1I%2BsPqAyX1Pe2JS8XWU2lUUMee2fRkclpLHZnCTsPGRL%2BoiWt2Lp9cNPlvyW%2F1e6OgUnJMbRFMRaPAnKYcmtHQzWJfqRDRDzxwsQ0hs2HjOnYbFXEtlvbaFU8oN92uEwvqUpyy7tKSUJmvmR4QrWyJh6qO1%2B7SVSkUp5GAtjB7LQqwaJGUGzU6qEG3gk2LBtX2yG4C0QYFI0V91ufQw7Phoz6LH01u%2Bh%2BLV6W5SVbwyuKPAdbebjHD8VTGMZo%2FgWoKqKxedN3k%2BYWaMMF41yHZ19S2Wb8EcX9wun3E2a2%2Bu6VIUZQLIVsxwjjGJ8n6PgrMEr0sjex1CliMe8UtBLiQcddTdzJRaKPvGFP8K7QQg%3D%3D";
        s = AESNotifyUtil.decryptURL(s , C.BIZ_CODE_LAIFENQI);
        System.out.println(s);
    }

}
