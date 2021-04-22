package com.chaos.eurekaproducer.design;

/**
 * @author liaopeng
 * @title: RealSubject
 * @projectName eureka-producer
 * @description: TODO
 * @date 2021/4/209:26 下午
 */
public class RealSubject implements Subject{
    @Override
    public void print() {
        System.out.println("subject");
    }
}
