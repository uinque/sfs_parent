package com.hk.commons.utils.thread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 写入被拒绝的任务处理策略
 * 例如：执行shutdown还继续往队列内放线程任务
 * Created by linhy on 2017/4/19.
 */
public enum  RejectedExecutionHandlerPolicy {

    Default(null),
    /**
     * 丢弃任务
     */
    Discard(new ThreadPoolExecutor.DiscardPolicy()),
    /**
     * 抛RejectedExecutionException异常
     */
    Abort(new ThreadPoolExecutor.AbortPolicy()),
    /**
     * 强行执行
     */
    CallerRuns(new ThreadPoolExecutor.CallerRunsPolicy()),
    /**
     * 丢弃并重新添加队列进入重试
     */
    DiscardOldest(new ThreadPoolExecutor.DiscardOldestPolicy());

    private RejectedExecutionHandler handler;	//策略处理器
    RejectedExecutionHandlerPolicy(RejectedExecutionHandler handler) {
        if(handler == null) {
            //default选项默认使用AbortPolicy策略（与JDK1.6一致）
            handler = new ThreadPoolExecutor.AbortPolicy();
        }
        this.handler = handler;
    }
    public RejectedExecutionHandler getHandler() {
        return handler;
    }
}
