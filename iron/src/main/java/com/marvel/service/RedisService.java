package com.marvel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by gaoyang on 18/6/23.
 */
@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * 加锁
     *
     * @param locaName       锁的key
     * @param acquireTimeout 获取超时时间
     * @param timeout        锁的超时时间
     * @return 锁标识
     */
    public boolean lockWithTimeout(String locaName,
                                   long acquireTimeout, long timeout) {
        try {
            // 锁名，即key值
            String lockKey = "lock:" + locaName;

            // 获取锁的超时时间，超过这个时间则放弃获取锁
            long end = System.currentTimeMillis() + acquireTimeout;

            String nowTime = String.valueOf(System.currentTimeMillis());

            while (System.currentTimeMillis() < end) {
                if (redisTemplate.opsForValue().setIfAbsent(lockKey, nowTime)) {
                    redisTemplate.expire(lockKey, timeout, TimeUnit.MILLISECONDS);
                    // 返回value值，用于释放锁时间确认
                    return true;
                }
                // 返回-1代表key没有设置超时时间，为key设置一个超时时间
                if (redisTemplate.getExpire(lockKey) == -1) {
                    redisTemplate.expire(lockKey, timeout, TimeUnit.MILLISECONDS);
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 释放锁
     * @param lockName 锁的key
     * @return
     */
    public void releaseLock(String lockName) {
        String lockKey = "lock:" + lockName;
        redisTemplate.delete(lockKey);
    }
}
