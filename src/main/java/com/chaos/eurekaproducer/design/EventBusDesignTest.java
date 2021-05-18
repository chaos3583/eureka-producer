package com.chaos.eurekaproducer.design;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * @author liaopeng
 * @title: EventBusController
 * @projectName eureka-producer
 * @description: TODO
 * @date 2021/5/14上午10:13
 */
public class EventBusDesignTest {

    /**
     * 主题
     */
    public static class ChaosSubject{
        private EventBus eventBus;

        public ChaosSubject() {
            this.eventBus = new AsyncEventBus(Executors.newFixedThreadPool(2));
        }

        //注册观察者
        public void registerObserver(Object object){
            eventBus.register(object);
        }

        //通知观察者
        public void noticeObserver(String userName){
            eventBus.post(userName);
        }
    }


    /**
     * 观察者
     */
    public static class ChaosObserver{
        @Subscribe
        public void  sendMailToChaos(String userName){
            System.out.println(userName);
        }
    }

    public static void main(String[] args) {
        ChaosSubject chaosSubject = new ChaosSubject();
        chaosSubject.registerObserver(new ChaosObserver());
        chaosSubject.noticeObserver("chaos");
    }

}




















