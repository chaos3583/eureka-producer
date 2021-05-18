package com.chaos.eurekaproducer.service;

import com.chaos.eurekaproducer.domain.User;

import java.util.List;

/**
 * @author liaopeng
 * @title: IUserService
 * @projectName eureka-producer
 * @description: TODO
 * @date 2021/5/14下午6:07
 */
public interface IUserService {

    public Integer insert(User user);

    public int batchInsert(List<User> users);
}
