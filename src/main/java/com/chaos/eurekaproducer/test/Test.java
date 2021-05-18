package com.chaos.eurekaproducer.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chaos.eurekaproducer.domain.Address;
import com.chaos.eurekaproducer.domain.User;
import org.apache.commons.lang.SerializationUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liaopeng
 * @title: Test
 * @projectName eureka-producer
 * @description: TODO
 * @date 2021/5/11上午10:33
 */
public class Test {
    public static void main(String[] args) throws CloneNotSupportedException {
        Address address = new Address("北京");
        User user = new User("chaos",address);
        System.out.println("拷贝前的user："+user.getName()+","+user.getAddress().getCity());
        String s = JSONObject.toJSONString(user);
        User cUser = JSONObject.parseObject(s, User.class);
        cUser.setName("夏夜");
        address.setCity("上海");
        System.out.println("拷贝后的user："+user.getName()+","+user.getAddress().getCity());
        System.out.println("拷贝新的cUser："+cUser.getName()+","+cUser.getAddress().getCity());
    }


    public static void construstClone(){
        Address address = new Address("北京");
        User user = new User("chaos",address);
        System.out.println("拷贝前的user："+user.getName()+","+user.getAddress().getCity());
        User cUser = new User("夏夜",new Address("上海"));
        System.out.println("拷贝后的user："+user.getName()+","+user.getAddress().getCity());
        System.out.println("拷贝新的cUser："+cUser.getName()+","+cUser.getAddress().getCity());
    }

    //实现Cloneable接口实现的拷贝
    public static void method1() throws CloneNotSupportedException {
//        Address address = new Address("北京");
//        User user = new User("chaos",address);
//        System.out.println("拷贝前的user："+user.getName()+","+user.getAddress().getCity());
//        User cUser = (User)user.clone();
//        cUser.setName("夏夜");
//        address.setCity("上海");
//        System.out.println("拷贝后的user："+user.getName()+","+user.getAddress().getCity());
//        System.out.println("拷贝新的cUser："+cUser.getName()+","+cUser.getAddress().getCity());
//
//        HashMap<String,String> map = new HashMap<>();
//        Object clone = map.clone();
    }
}
