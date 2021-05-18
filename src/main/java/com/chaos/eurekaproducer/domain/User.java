package com.chaos.eurekaproducer.domain;

import java.io.Serializable;

/**
 * @author liaopeng
 * @title: User
 * @projectName eureka-producer
 * @description: TODO
 * @date 2021/5/11上午10:31
 */
public class User{

    private Long id;
    private String name;

    private Integer age;
    private Address address;

    public User(){

    }

    public User(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
