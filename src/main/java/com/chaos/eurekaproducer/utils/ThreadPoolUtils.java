package com.chaos.eurekaproducer.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author liaopeng
 * @title: ThreadPoolUtils
 * @projectName eureka-producer
 * @description: TODO
 * @date 2021/5/8上午10:55
 */
public class ThreadPoolUtils {
    static ExecutorService fixedThreadpool;

    static {
        if (fixedThreadpool==null){
            System.out.println("处理器数量："+Runtime.getRuntime().availableProcessors());
            fixedThreadpool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        }
    }

    public static void fixedThreadPool(Runnable runnable){
        fixedThreadpool.submit(runnable);
    }
}
