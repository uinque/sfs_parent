package com.hk.commons.utils.thread;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池配置
 * Created by linhy on 2017/4/19.
 */
public class ThreadPoolConfig {

    private Integer corePoolSize;								//池中所保存的线程数，包括空闲线程
    private Integer maximumPoolSize;							//池中允许的最大线程数
    private Integer keepAliveTime = 5;							//当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间（默认:5秒）
    private TimeUnit unit = TimeUnit.SECONDS;					//keepAliveTime参数的时间单位，默认：秒
    private Integer waitQueueSize = 3000;						//等待队列长度（>0则为有界队列；=0则为无长度队列；<0则为无界队列）
    private String threadNamePrefix;							//线程名称前缀
    private RejectedExecutionHandler rejectedExecutionHandler;	//写入被拒绝的任务处理策略

    public ThreadPoolConfig() {}

    public ThreadPoolConfig(Integer threadCount) {
        this(threadCount, null, null);
    }

    public ThreadPoolConfig(Integer threadCount, Integer waitQueueSize, String threadNamePrefix) {
        this(threadCount, threadCount, null, null, waitQueueSize
                , threadNamePrefix, RejectedExecutionHandlerPolicy.Default);
    }

    public ThreadPoolConfig(Integer corePoolSize, Integer maximumPoolSize,
                            Integer keepAliveTime, TimeUnit unit, Integer waitQueueSize,
                            String threadNamePrefix, RejectedExecutionHandlerPolicy policy) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit
                , waitQueueSize, threadNamePrefix, policy.getHandler());
    }

    public ThreadPoolConfig(Integer corePoolSize, Integer maximumPoolSize,
                            Integer keepAliveTime, TimeUnit unit, Integer waitQueueSize,
                            String threadNamePrefix, RejectedExecutionHandler rejectedExecutionHandler) {
        super();
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        if(keepAliveTime != null && keepAliveTime > 0) {
            this.keepAliveTime = keepAliveTime;
        }
        if(unit != null) {
            this.unit = unit;
        }
        if(waitQueueSize != null) {
            this.waitQueueSize = waitQueueSize;
        }
        this.threadNamePrefix = threadNamePrefix;
        this.rejectedExecutionHandler = rejectedExecutionHandler;
    }

    /**
     * 等待队列长度（>0则为有界队列；=0则为无长度队列；<0则为无界队列）
     * @return
     */
    public BlockingQueue<Runnable> getWorkQueue() {
        return this.getWaitQueueSize() > 0 ? new ArrayBlockingQueue<Runnable>(this.getWaitQueueSize()) :
                this.getWaitQueueSize() < 0 ? new LinkedBlockingQueue<Runnable>() : new SynchronousQueue<Runnable>();
    }

    /**
     * 获取线程工厂
     * @return
     */
    public ThreadFactory getThreadFactory() {
        return StringUtils.isBlank(threadNamePrefix) ?
                Executors.defaultThreadFactory() : new CustomPrefixThreadFactory(threadNamePrefix);
    }

    /**
     * 自定义前缀线程工厂
     * @author chenzj
     */
    static class CustomPrefixThreadFactory implements ThreadFactory {
        static final AtomicInteger poolNumber = new AtomicInteger(1);
        final ThreadGroup group;
        final AtomicInteger threadNumber = new AtomicInteger(1);
        final String namePrefix;

        CustomPrefixThreadFactory(String threadNamePrefix) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null)? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = StringUtils.join(
                    new Object[]{threadNamePrefix, "pool", poolNumber.getAndIncrement(), "thread-"}, "-");
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }
    public void setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }
    public Integer getCorePoolSize() {
        return corePoolSize;
    }
    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }
    public Integer getMaximumPoolSize() {
        return maximumPoolSize;
    }
    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }
    public Integer getKeepAliveTime() {
        return keepAliveTime;
    }
    public void setKeepAliveTime(Integer keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }
    public Integer getWaitQueueSize() {
        return waitQueueSize;
    }
    public void setWaitQueueSize(Integer waitQueueSize) {
        this.waitQueueSize = waitQueueSize;
    }
    public RejectedExecutionHandler getRejectedExecutionHandler() {
        return rejectedExecutionHandler;
    }
    public void setRejectedExecutionHandler(
            RejectedExecutionHandler rejectedExecutionHandler) {
        this.rejectedExecutionHandler = rejectedExecutionHandler;
    }
    public TimeUnit getUnit() {
        return unit;
    }
    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

}
