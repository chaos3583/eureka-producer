package com.chaos.eurekaproducer.redis;

import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * @author liaopeng
 * @title: BloomFilter
 * @projectName myProjects
 * @description: 使用redisson构造布隆过滤器
 * @date 2021/1/510:27 下午
 */
public class BloomFilter {
    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        config.useSingleServer().setPassword("123456a");
        //构造Redisson
        RedissonClient redisson = Redisson.create(config);
        RBloomFilter<Object> bloomFilter = redisson.getBloomFilter("phoneList");

        //初始化布隆过滤器，100000000L，误差率3%
        bloomFilter.tryInit(100000000L,0.03);
        bloomFilter.add("10086");
        System.out.println("10086："+bloomFilter.contains("10086"));
        System.out.println("10085："+bloomFilter.contains("10085"));
    }
}
