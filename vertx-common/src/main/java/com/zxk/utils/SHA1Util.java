package com.zxk.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;

/**
 * MD5工具
 *
 * Created by wangyi on 2016/11/19.
 */
public class SHA1Util {

    private static final String DEFAULT_CHARSET = "UTF-8";  //编码

    /**
     * 签名字符串
     *
     * @param text 需要签名的字符串
     * @param key  密钥
     * @return 签名结果
     */
    public static String sign(String text, String key) {
        return sign(text, key, DEFAULT_CHARSET);
    }

    /**
     * 签名字符串
     *
     * @param text 需要签名的字符串
     * @param key  密钥
     * @return 签名结果
     */
    public static String sign(String text, String key, String charset) {
        return DigestUtils.sha1Hex(getContentBytes(text + key, charset));
    }

    /**
     * 签名字符串
     *
     * @param text          需要签名的字符串
     * @param sign          签名结果
     * @param key           密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String input_charset) {
        text = text + key;
        String mysign = DigestUtils.sha1Hex(getContentBytes(text, input_charset));
        if (mysign.equals(sign)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    public static void main(String[] args) throws Exception{
        String content = "zb4yb0QeZaMspQ709eIOJ7Sw+DMURF3zLF8TRpvkwsCN2CZtWWohkJROvqC1S4r/3RoNH64WNieYDfKGzt+wZUD8u8aKORy6rTqzIYeOaovf89PrnlrtlaC3hiMTGzjsEtL6xFAHcGMg9mgz+PASwnGWSVzr/TuNPC8d2mQqmzirU7hZoYYfEFFYeINOprpfn34Jxwb1nOpbZCOANy+R4akSeGd82z+vfZIpKpp8FXM0pnVxt2rtXnuA7MmcWb3jQfk4dOPn0isg6kVh5qxnfbCxPRkH7Tvkd3A4AD5MKiBPcij2Y9pqtc4ppxRP1nk14PY1brhkdO/ATJDvZti02rnKJeFYL6NP2SWNsykhy4bsotKLP4bjx3NxJPH4ZFfVX9V0BwiCz4P1qSpE96u0RE/4PlOWtCL5PcfV0ckLa/v1gSXGQj+akIrt0oZEisFlgASsof6HWZFoPBxQrV8DRvhNA4hLVeA7eAhwY2bfiMeiITEVMVzfNPZ/lTHDWe/apWLCbmRkoXAeIGSfcTyhRqUyVSp9I7/LZ/AXt+F2CN/5p37353z4bmrsywAvWn4R17+8ycawzWdgrYBulkwRHTToDt1Ko9UEXA627scPdiruyerM0boIgBcYhKkzknRHiS+MX3CGCe2KqQxar858IIbDlZ+JVJ7YxU60NVLoyUuK0YcapZaW6wLe1xlHfNYp1a60jjTdN8lLxnJPYCa/9Mhb9WFnJZVBZG2QKyicLCs3llSTyfSJIf4B3u4EldtbnC0Vm85Qp+A9eNCZunBkV7ACNDVFESZA0GljjKC5xuI2OUVpeI5ao75d+jlKyN78+yBhCHNdm8vH3wM5VNyNI3BbKEPVhs/VMC8cFHdoMyZG7P1Nt+oadmKn0bsL59DDPswjQKJaanwF/WrBeM4l0vz55cHU10mGjy2xJgloars=";
        String pri = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALydlCX6h6WPJ5sFY4RrjiK/9lYDRkc+oJM6Nc74OygJIftX21nX83+TFL2i3xIKWI52czYyBgleMbq4PYAYC41tCmgKdgvmp0Ubv/9z3Q2M9bYyXkXhfSZOr1hnynK0IKCZqwYNt+RI8PN7TYp6UnUOhiwyUMcQNbYkMkOMApBBAgMBAAECgYEAhCVzEljAiMIPYZEY9EHWx4nusLyClI/QONZjtKo96ALeCBa52+xs8Ui/E031M9KD0Ow0NBhAGnggJ03F6OTv0eOloWccmuazrvc6ofp2oGiST5JLK8rbUpdftfkGpb+m9FwCpvfDZUp8dLWtR655bVxZPmqW7qhB4dFu0wL0jUECQQD6yA2SLZefH+dRV/GmceRQpvNWs4Q1Y0AuPQiNir6pNSJ51MZ621y4LeEUdWViUJhninQRnTYrvmt+l8S5I0xtAkEAwIpcASi1NAXUVGZaLeRU+TWU9UEDVtMmc6nKcLbYYaWZcxgiCGwZ8VFzIlhTruTIFI8M61k3HFnMHIjJE/7GpQJBAPd5rbLAHSzhTeM+1u/62mUgq2e21VPd8rrubd9HuKjAD5qMd6VLje8PD1uiEf94Kz2aKrorGwcF7YxKds6AX+0CQGwDssW7rpw2wP1wRhsEw17jXmOQS21EG4g7sqQ1D4MIQeO9oCoVayBkTzDvWFL/afeOoPLPJAm1nrpLSnL7NTkCQBQPrORlIKaNF78bKuy7i+CWC7KfRjlxXhBsXG+6JBDmEwvWu4EE5ExOsuerjT7vfvp5XDM+tZxtY5/qhpTN4nc=";
        System.out.println(RSAUtil.sign(content.getBytes(),pri));
    }
}
