package com.hk.commons.utils;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * Created by linhy on 2017/4/20.
 */
public class RandomUtils {

    private static SecureRandom random = new SecureRandom();

    /**
     * 封装JDK自带的UUID, 通过Random数字生成,中间有-分割
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 封装JDK自带的UUID, 通过Random数字生成,中间无-分割
     */
    public static String uuid2() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 使用SecureRandom随机生成Long.
     */
    public static long randomLong() {
        return random.nextLong();
    }

    /**
     * 生成int类型随机数
     * @param max
     * @return
     */
    public static int randomInt(int max) {
        return random.nextInt(max);
    }

}
