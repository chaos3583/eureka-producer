package com.chaos.eurekaproducer.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author liaopeng
 * @title: MyRemoteImpl
 * @projectName myProjects
 * @description: TODO
 * @date 2021/1/119:53 下午
 */
public class MyRemoteImpl extends UnicastRemoteObject implements MyRemote{

    protected MyRemoteImpl() throws RemoteException {

    }

    @Override
    public void sayHello(){
        System.out.println("hello");
    }

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        Registry registry = LocateRegistry.createRegistry(1099);
        MyRemoteImpl service = new MyRemoteImpl();
        registry.rebind("sayHelloService",service);
    }

}
