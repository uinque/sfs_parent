package com.hk.commons.utils;

import java.security.MessageDigest;
import java.util.Date;
import java.util.Random;

/**
 * Created by linhy on 2017/4/20.
 */
public class SecurityUtils {

    /** 盐值 */
    private static String salt = "homeking";

    /**
     * 默认生成六位的密码
     *
     * @return
     */
    public static String createPasswd() {
        return SecurityUtils.createPasswd(6);
    }

    /**
     * 创建一个密码
     *
     * @param pwd_len 密码长度
     * @return
     */
    public static String createPasswd(int pwd_len) {
        // 35是因为数组是从0开始的，26个字母+10个数字
        final int maxNum = 36;
        int i; // 生成的随机数
        int count = 0; // 生成的密码的长度
        char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
                't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random(new Date().getTime());
        while (count < pwd_len) {
            // 生成随机数，取绝对值，防止生成负数，
            i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count++;
            }
        }
        return pwd.toString();
    }

    /**
     * 进行SHA256加密，生成32位加密的密文
     *
     * @param passwd
     * @return
     */
    public final static String SHA256(String passwd) {

        char hexDigits[] = { 'A', '6', '7', 'B', '0', '1', '2', '3', '4', '5', 'C', '8', '9', 'D', 'E', 'F' };
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA-256");
            mdTemp.update(MD5((salt + passwd + salt)).getBytes());
            byte[] md = mdTemp.digest(MD5(passwd).getBytes());
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            String temp = new String(str);
            return temp;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 进行md5加密，生成32位加密的密文
     *
     * @param passwd
     * @return
     */
    public final static String MD5(String passwd) {
        passwd = salt + passwd + salt;
        char hexDigits[] = { 'A', '6', '7', 'B', '0', '1', '2', '3', '4', '5', 'C', '8', '9', 'D', 'E', 'F' };
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update((salt + passwd + salt).getBytes());
            byte[] md = mdTemp.digest((passwd).getBytes());
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            String temp = new String(str);
            return temp;
        } catch (Exception e) {
            return null;
        }
    }
}
