package com.cjcx.aiserver.ai.baidu;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaiduTokenManager {

    private static final Logger logger = LoggerFactory.getLogger(BaiduTokenManager.class);

    private static ScheduledExecutorService scheduledExecutorService;

    private static Map<String, String> tokenMap = new LinkedHashMap<String, String>();

    private static Map<String, ScheduledFuture<?>> futureMap = new HashMap<String, ScheduledFuture<?>>();

    private static int poolSize = 2;

    private static boolean daemon = Boolean.TRUE;

    /**
     * 初始化 scheduledExecutorService
     */
    private static void initScheduledExecutorService() {
        logger.info("daemon:{},poolSize:{}", daemon, poolSize);
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
        BaiduTokenManager.poolSize = poolSize;
    }

    /**
     * 设置线程方式
     *
     * @param daemon daemon
     */
    public static void setDaemon(boolean daemon) {
        BaiduTokenManager.daemon = daemon;
    }

    /**
     * 初始化token 刷新，每29天分钟刷新一次。
     *
     * @param apiKey  access_token
     */
    public static void init(final String apiKey, final String secretKey) {
        init(apiKey, secretKey, 0, 60 * 60 * 12 * 29);
    }

    /**
     * 初始化token 刷新，每29天分钟刷新一次。
     *
     * @param apiKey access_token
     * @param initialDelay 首次执行延迟（秒）
     * @param delay        执行间隔（秒）
     */
    public static void init(final String apiKey, final String secretKey, int initialDelay, int delay) {
        if (scheduledExecutorService == null) {
            initScheduledExecutorService();
        } 
        
        if (futureMap.containsKey(apiKey)) {
            futureMap.get(apiKey).cancel(true);
        }
        //立即执行一次
        if (initialDelay == 0) {
            doRun(apiKey, secretKey);
        }
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                doRun(apiKey, secretKey);
            }
        }, initialDelay == 0 ? delay : initialDelay, delay, TimeUnit.SECONDS);
        futureMap.put(apiKey, scheduledFuture);
        logger.info("apiKey:{}", apiKey);
    }

    private static void doRun(final String apiKey, final String secretKey) {
        try {
            String access_token = BaiduAuthService.getAuth(apiKey, secretKey);
            
            tokenMap.put(apiKey, access_token);
            logger.info("access_token refurbish with apiKey:{}, access_token:{}", apiKey, access_token);
        } catch (Exception e) {
            logger.error("access_token  refurbish error with apiKey:{}", apiKey);
            e.printStackTrace();
        }
    }

    /**
     * 取消 token 刷新
     */
    public static void destroyed() {
        scheduledExecutorService.shutdownNow();
        logger.info("destroyed");
    }

    /**
     * 取消刷新
     *
     * @param apiKey access_token
     */
    public static void destroyed(String apiKey) {
        if (futureMap.containsKey(apiKey)) {
            futureMap.get(apiKey).cancel(true);
            logger.info("destroyed apiKey:{}", apiKey);
        }
    }

    /**
     * 获取 access_token
     *
     * @param apiKey
     * @return access_token
     */
    public static String getToken(String apiKey) {
        return tokenMap.get(apiKey);
    }

    /**
     * access_token 的 jsapi_ticket
     * 适用于单一微信号
     *
     * @return token
     */
    public static String getDefaultToken() {
        Object[] objs = tokenMap.values().toArray();
        return objs.length > 0 ? objs[0].toString() : null;
    }
    
    
    public static void setToken(String apiKey, String access_token) {
    	tokenMap.put(apiKey, access_token);
    }

}

