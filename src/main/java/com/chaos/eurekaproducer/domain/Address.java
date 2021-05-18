package com.chaos.eurekaproducer.domain;

import java.io.Serializable;

/**
 * @author liaopeng
 * @title: Address
 * @projectName eureka-producer
 * @description: TODO
 * @date 2021/5/11上午10:31
 */
public class Address{

    private String city;

    public Address(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
