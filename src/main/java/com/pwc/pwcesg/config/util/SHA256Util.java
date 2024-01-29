package com.pwc.pwcesg.config.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SHA256Util {
    /**
     *
     * 로그설정
     *
     * @alias 로그설정
     */
    protected static Logger logger = LogManager.getLogger(SHA256Util.class);

    /**
     * SHA-256 암호화 함
     *
     * @param source String 원본
     * @param salt String SALT 값
     * @return
     */
    public static String getEncrypt(String source, String salt) {
        return getEncrypt(source, salt.getBytes());
    }

    /**
     * SHA-256 암호화 함
     *
     * @param source String 원본
     * @param salt byte[] SALT 값
     * @return result String
     */
    public static String getEncrypt(String source, byte[] salt) {
        String result = "";

        byte[] a = source.getBytes();
        byte[] bytes = new byte[a.length + salt.length];

        System.arraycopy(a, 0, bytes, 0, a.length);
        System.arraycopy(salt, 0, bytes, a.length, salt.length);

        try {
            // 암호화 방식 지정 메소드
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(bytes);

            byte[] byteData = md.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xFF) + 256, 16).substring(1));
            }

            result = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.error("getSHA256Enc NoSuchAlgorithmException");
        }

        return result;
    }
}