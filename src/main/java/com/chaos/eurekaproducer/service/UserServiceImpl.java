package com.chaos.eurekaproducer.service;

import com.chaos.eurekaproducer.domain.User;
import com.chaos.eurekaproducer.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author liaopeng
 * @title: UserServiceImpl
 * @projectName eureka-producer
 * @description: TODO
 * @date 2021/5/14下午6:07
 */
@Service
public class UserServiceImpl implements IUserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,rollbackFor = Exception.class)
    public Integer insert(User user) {
        int insert = userMapper.insert(user);
        return insert;
    }

    @Override
    public int batchInsert(List<User> users) {
        return userMapper.batchInsert(users);
    }
}
