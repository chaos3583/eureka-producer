package com.chaos.eurekaproducer.design;

/**
 * @author liaopeng
 * @title: DesignController
 * @projectName eureka-producer
 * @description: TODO
 * @date 2021/4/209:32 下午
 */
public class DesignController {

    public static void main(String[] args) {
        Subject proxy = new JdkDynamicProxy(new RealSubject()).getProxy();
        proxy.print();
    }
}
