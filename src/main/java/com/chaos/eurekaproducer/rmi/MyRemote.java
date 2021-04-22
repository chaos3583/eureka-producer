package com.chaos.eurekaproducer.rmi;

import java.rmi.Remote;

/**
 * @author liaopeng
 * @title: MyRemote
 * @projectName myProjects
 * @description: TODO
 * @date 2021/1/119:52 下午
 */
public interface MyRemote extends Remote {

    public void sayHello();
}
