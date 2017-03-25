package com.exzone.lib.util;

import android.text.TextUtils;

import java.nio.charset.Charset;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 作者:李鸿浩
 * 描述:AES加密解密
 * 时间: 2016/10/9.
 */
public class AESUtils {
    private AESUtils() {
        throw new AssertionError();
    }

    /**
     * 加密
     */
    public static String encrypt(String sSrc, String sKey) throws Exception {
        if (TextUtils.isEmpty(sKey)) {
            Logger.e("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            Logger.e("Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes("UTF-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
        IvParameterSpec iv = new IvParameterSpec("1234567890123456".getBytes(Charset.forName("UTF-8")));//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(Charset.forName("UTF-8")));
        return StringUtils.encodeToString(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }


    /**
     * 解密
     */
    public static String decrypt(String sSrc, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                Logger.e("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                Logger.e("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("1234567890123456".getBytes(Charset.forName("UTF-8")));
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64Utils.decode(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                return new String(original,Charset.forName("UTF-8"));
            } catch (Exception e) {
                Logger.e(e.toString());
                return null;
            }
        } catch (Exception ex) {
            Logger.e(ex.toString());
            return null;
        }
    }
}
