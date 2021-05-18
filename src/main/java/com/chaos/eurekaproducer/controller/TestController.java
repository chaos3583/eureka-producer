package com.chaos.eurekaproducer.controller;

import com.chaos.eurekaproducer.design.Subject;
import com.chaos.eurekaproducer.domain.StoreTransactionLog;
import com.chaos.eurekaproducer.domain.StoreTransactionLogQuery;
import com.chaos.eurekaproducer.redis.RedisUtil;
import com.chaos.eurekaproducer.redis.RedissonUtil;
import com.chaos.eurekaproducer.service.IStoreTransactionLogService;
import com.chaos.eurekaproducer.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Optional;

/**
 * @author liaopeng
 * @title: TestController
 * @projectName myProjects
 * @description: TODO
 * @date 2020/12/225:12 下午
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    IStoreTransactionLogService storeTransactionLogService;

    @Autowired
    ITestService testService;
    /**
     * 循环往数据库表插入数据
     * @return
     * @throws IOException
     */
    @RequestMapping("/redisPublish")
    @ResponseBody
    private void redisPublish() throws IOException {
        redisUtil.publishMessage("channel1","hello liaopeng");
        boolean aaa = redisUtil.set("aaa", 1);
        System.out.println(aaa);
    }


    static volatile int total = 100;
    static String key = "stock";

    @RequestMapping("/initStock")
    public void initStock(){
        total=100;
    }


    /**
     * @return
     * @throws IOException
     */
    @RequestMapping("/lockTest")
    @ResponseBody
    private void lockDecreaseStock() throws Exception {
        RedissonUtil.lock(key,500L);//获取锁，并设置超时时间
        if (total>0){
            total--;
        }
        Thread.sleep(1000);
        System.out.println("========减完库存后，当前库存========"+total);

        if (RedissonUtil.isHeldByCurrentThread(key)){
            RedissonUtil.unlock(key);
        }
    }

    /**
     * @return
     * @throws IOException
     */
    @RequestMapping("/aopTest")
    private void aopTest() throws Exception {
//        System.out.println("调用方拿到的返回值String："+storeTransactionLogService.testString("chaos"));
//        System.out.println("调用方拿到的返回值count："+storeTransactionLogService.count());
        System.out.println(testService.hashCode());
        System.out.println("testService.getString"+testService.getString("夏夜"));

    }


}
