package com.chaos.eurekaproducer.controller;

import com.chaos.eurekaproducer.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liaopeng
 * @title: RedisController
 * @projectName eureka-producer
 * @description: TODO
 * @date 2021/5/18下午4:28
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    RedisUtil redisUtil;

    @RequestMapping("/add")
    public void  add(){
        redisUtil.set("夏夜",18);
    }
}
