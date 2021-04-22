package com.chaos.eurekaproducer.service;

import com.chaos.eurekaproducer.annotation.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liaopeng
 * @title: TestServiceImpl
 * @projectName eureka-producer
 * @description: TODO
 * @date 2021/2/12:25 下午
 */
@Service
public class TestServiceImpl implements ITestService{
    @Override
    public String getString(String str) {

        return str;
    }
}
