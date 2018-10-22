package com.lottery.center.common.cqssc;


import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.*;

/**
 * 采集线程 管理
 */
@Slf4j
public class CollectManager {

    private Queue queue;

    private static ScheduledExecutorService scheduledExecutorService;

    private static int poolSize = 2;

    private static boolean daemon = Boolean.TRUE;

    public CollectManager(){

    }

    public CollectManager(Queue queue){
        this.queue = queue;
    }



    /**
     * 初始化 scheduledExecutorService
     */
    private static void initScheduledExecutorService() {
        log.info("daemon:{},poolSize:{}", daemon, poolSize);
        scheduledExecutorService = Executors.newScheduledThreadPool(poolSize, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable arg0) {
                Thread thread = Executors.defaultThreadFactory().newThread(arg0);
                //设置守护线程
                thread.setDaemon(daemon);
                return thread;
            }
        });
    }

    /**
     * 设置线程池
     *
     * @param poolSize poolSize
     */
    public static void setPoolSize(int poolSize) {
        CollectManager.poolSize = poolSize;
    }

    /**
     * 设置线程方式
     *
     * @param daemon daemon
     */
    public static void setDaemon(boolean daemon) {
        CollectManager.daemon = daemon;
    }


    /**
     * 启动
     * @param initialDelay  首次执行延迟（秒）
     * @param delay         执行间隔（秒）
     */
    public static void bootstarp(int initialDelay, int delay){
        if (scheduledExecutorService == null) {
            initScheduledExecutorService();
        }

        //线程执行完再执行
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(new CollectThread(), initialDelay == 0 ? delay : initialDelay, delay, TimeUnit.SECONDS);
    }


    public static void main(String[] args) {
        //开3个线程
        //每个线程3s访问一次获取结果返回，放入队列 执行完再次去请求,
        //结果 收集  集合    放入缓存
        //输出最新集合数据   一个线程输出结果
    }


}
