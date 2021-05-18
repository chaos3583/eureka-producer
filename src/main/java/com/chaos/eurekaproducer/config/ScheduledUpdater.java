package com.chaos.eurekaproducer.config;

import org.apache.ibatis.annotations.Update;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author liaopeng
 * @title: ScheduledUpdater
 * @projectName eureka-producer
 * @description: TODO
 * @date 2021/4/22上午11:40
 */
public class ScheduledUpdater {
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private long initialDelayInSeconds;
    private long periodInSeconds;
    private Updater updater;

    public ScheduledUpdater(long initialDelayInSeconds, long periodInSeconds, Updater updater) {
        this.initialDelayInSeconds = initialDelayInSeconds;
        this.periodInSeconds = periodInSeconds;
        this.updater = updater;
    }

    public void run(){
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                updater.update();
            }
        },this.initialDelayInSeconds,this.periodInSeconds, TimeUnit.SECONDS);
    }
}
