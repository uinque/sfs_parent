package com.hk.commons.utils.thread;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池相关封装
 * Created by linhy on 2017/4/19.
 */
public class ThreadUtils {

    /**
     * 建一个线程池（FixedThread）
     * 默认超过5秒回收空闲线程
     * 默认等待队列长度为3000的有界队列
     * @param threadCount 最大线程数
     * @return 线程池
     */
    public static ThreadPoolExecutor buildThreadPool(int threadCount) {
        return buildThreadPool(new ThreadPoolConfig(threadCount));
    }

    /**
     * 建一个线程池（FixedThread）
     * 默认超过5秒回收空闲线程
     * @param threadCount 线程数
     * @param waitQueueSize 等待队列长度
     * @param threadNamePrefix 线程名前缀
     * @return
     */
    public static ThreadPoolExecutor buildThreadPool(int threadCount, int waitQueueSize, String threadNamePrefix) {
        return buildThreadPool(new ThreadPoolConfig(threadCount, waitQueueSize, threadNamePrefix));
    }

    /**
     * 根据配置创建线程池
     * @param config
     * @return
     */
    public static ThreadPoolExecutor buildThreadPool(ThreadPoolConfig config) {
        return new ThreadPoolExecutor(
                config.getCorePoolSize(), config.getMaximumPoolSize()
                , config.getKeepAliveTime(), config.getUnit(), config.getWorkQueue()
                , config.getThreadFactory(), config.getRejectedExecutionHandler());
    }
}
