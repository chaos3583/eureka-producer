package com.chaos.eurekaproducer.controller;

import com.chaos.eurekaproducer.utils.ThreadPoolUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author liaopeng
 * @title: ThreadTestController
 * @projectName eureka-producer
 * @description: TODO
 * @date 2021/5/8上午10:20
 */
@RestController
@RequestMapping("/thread")
public class ThreadTestController {

    @RequestMapping("/test")
    @ResponseBody
    private int test() throws IOException {

        ThreadPoolUtils.fixedThreadPool(new Runnable() {
            @Override
            public void run() {
                int m=0;
                for (int i = 0; i <2000; i++) {
                     m++;
                }
                System.out.println("当前线程名："+Thread.currentThread().getName());
            }
        });
        return 1;
    }
}
