package com.chaos.eurekaproducer;

import com.chaos.eurekaproducer.domain.StoreTransactionLog;
import com.chaos.eurekaproducer.domain.StoreTransactionLogQuery;
import com.chaos.eurekaproducer.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

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

}
