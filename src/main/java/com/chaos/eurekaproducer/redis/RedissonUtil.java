package com.chaos.eurekaproducer.redis;

import org.redisson.Redisson;
import org.redisson.RedissonLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * @author liaopeng
 * @title: RedissonUtil
 * @projectName myProjects
 * @description: TODO
 * @date 2021/1/2211:33 上午
 */
public class RedissonUtil {

    private RedissonClient redissonClient;

    static RedissonClient redisson;
//    public RedissonUtil(){
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setPassword("123456a").setDatabase(0);
//        redisson = Redisson.create(config);
//    }

    static {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setPassword("123456a").setDatabase(0);
        config.setLockWatchdogTimeout(1);//开启看门狗模式，每隔1s钟检查锁是否到期
        redisson = Redisson.create(config);
    }



    public static void lock(String key,Long time){
        RLock lock = redisson.getLock(key);
        try {
            lock.lock(time, TimeUnit.MILLISECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void unlock(String key){
        RLock lock = redisson.getLock(key);
        lock.unlock();
    }

    public static Boolean isHeldByCurrentThread(String key){
        RLock lock = redisson.getLock(key);
        return lock.isHeldByCurrentThread();
    }

}
