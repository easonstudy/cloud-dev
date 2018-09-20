package com.cjcx.wechat.open.weixin;

import com.cjcx.wechat.open.utils.RequestUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * JssdkManager jssdk 自动刷新
 *
 * @author LiYi
 */
public class JssdkManager {

    private static final Logger logger = LoggerFactory.getLogger(JssdkManager.class);

    private static ScheduledExecutorService scheduledExecutorService;

    private static Map<String, String> jssdkMap = new LinkedHashMap<String, String>();

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
        JssdkManager.poolSize = poolSize;
    }

    /**
     * 设置线程方式
     *
     * @param daemon daemon
     */
    public static void setDaemon(boolean daemon) {
        JssdkManager.daemon = daemon;
    }

    /**
     * 初始化token 刷新，每118分钟刷新一次。
     *
     * @param access_token  access_token
     */
    public static void init(final String access_token) {
        init(access_token, 0, 60 * 118);
    }

    /**
     * 初始化token 刷新，每118分钟刷新一次。
     *
     * @param access_token access_token
     * @param initialDelay 首次执行延迟（秒）
     * @param delay        执行间隔（秒）
     */
    public static void init(final String access_token, int initialDelay, int delay) {
        if (scheduledExecutorService == null) {
            initScheduledExecutorService();
        }
        if (futureMap.containsKey(access_token)) {
            futureMap.get(access_token).cancel(true);
        }
        //立即执行一次
        if (initialDelay == 0) {
            doRun(access_token);
        }
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                doRun(access_token);
            }
        }, initialDelay == 0 ? delay : initialDelay, delay, TimeUnit.SECONDS);
        futureMap.put(access_token, scheduledFuture);
        logger.info("appid:{}", access_token);
    }

    private static void doRun(final String access_token) {
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + access_token + "&type=jsapi";
        try {
            String rs = RequestUtil.doGet(url, null);
            JSONObject obj_content = new JSONObject(rs);
            String jsapi_ticket = obj_content.getString("ticket");


            jssdkMap.put(access_token, jsapi_ticket);
            logger.info("JSAPI TICKET refurbish with access_token:{}", access_token);
        } catch (Exception e) {
            logger.error("JSAPI TICKET  refurbish error with access_token:{}", access_token);
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
     * @param access_token access_token
     */
    public static void destroyed(String access_token) {
        if (futureMap.containsKey(access_token)) {
            futureMap.get(access_token).cancel(true);
            logger.info("destroyed appid:{}", access_token);
        }
    }

    /**
     * 获取 jsapi_ticket
     *
     * @param access_token access_token
     * @return token
     */
    public static String getJsapiTicket(String access_token) {
        return jssdkMap.get(access_token);
    }

    /**
     * access_token 的 jsapi_ticket
     * 适用于单一微信号
     *
     * @return token
     */
    public static String getDefaultJsapiTicket() {
        Object[] objs = jssdkMap.values().toArray();
        return objs.length > 0 ? objs[0].toString() : null;
    }

}
