package com.cjcx.member.framework.utils;

import com.cjcx.member.framework.config.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class JedisUtils {
    protected static final Logger logger = LoggerFactory.getLogger(JedisUtils.class);
    private static JedisPool jedisPool = null;

    private static Lock lock = new ReentrantLock();

    private JedisUtils() {
    }

    public static Jedis getJedis() {
        if (jedisPool == null) {
            lock.lock();
            try {
                if (jedisPool == null) {
                    jedisPool = SpringContextHolder.getBean("jedisPool");
                }
            } finally {
                lock.unlock();
            }
        }
        logger.debug("Jedis Pool:" + jedisPool);
        return jedisPool.getResource();
    }

    public static String setex(String key, String value, int timers) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.setex(key, timers, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseResource(jedis);
        }
        return result;
    }

    public static boolean exists(String key) {
        Jedis jedis = null;
        boolean result = false;
        try {
            jedis = getJedis();
            result = jedis.exists(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseResource(jedis);
        }
        return result;
    }

    public static String get(String key) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseResource(jedis);
        }
        return result;
    }

    public static Long del(String key) {
        Jedis jedis = null;
        Long result = 0L;
        try {
            jedis = getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseResource(jedis);
        }
        return result;
    }


    /**
     * 释放Jedis
     *
     * @param jedis
     */
    public static void releaseResource(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

}
