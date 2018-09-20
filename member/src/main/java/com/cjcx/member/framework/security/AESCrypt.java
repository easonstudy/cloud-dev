package com.cjcx.member.framework.security;

import com.cjcx.member.framework.utils.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.security.SecureRandom;


public class AESCrypt {

    public static final String key = "_xo.m3";

    public static String encrypt(String cleartext) throws Exception {
        return encrypt(key, cleartext);
    }
    public static String encrypt(String seed, String cleartext) throws Exception {
        byte[] b = seed.getBytes("UTF-8");
        byte[] rawKey = getRawKey(seed.getBytes("UTF-8"));
        //byte[] rawKey = {0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x38,0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x38};
        byte[] result = encrypt(rawKey, cleartext.getBytes("UTF-8"));
        return new String(Base64.encode(result));
    }


    public static String decrypt(String encrypted) throws Exception {
        return decrypt(key, encrypted);
    }
    public static String decrypt(String seed, String encrypted) throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes("UTF-8"));
        //byte[] rawKey = {0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x38,0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x38};
        byte[] enc = Base64.decode(encrypted);
        byte[] result = decrypt(rawKey, enc);
        return new String(result);
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        // SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法  
        SecureRandom sr = null;
        sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
        kgen.init(256, sr); //256 bits or 128 bits,192bits  
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static void main(String[] args) {
        String key = "";
        String originalText = "abdfs:q21";
        try {
            System.out.println("加密文本为" + originalText);
            String encryptingCode = AESCrypt.encrypt( originalText);
            System.out.println("加密结果为 " + encryptingCode);
            System.out.println("urlencode加密结果为 " + URLEncoder.encode(encryptingCode,"UTF-8") );
            String decryptingCode = AESCrypt.decrypt( encryptingCode);
            System.out.println("解密结果为 " + decryptingCode);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
