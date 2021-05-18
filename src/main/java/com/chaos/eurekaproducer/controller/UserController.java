package com.chaos.eurekaproducer.controller;

import com.chaos.eurekaproducer.domain.User;
import com.chaos.eurekaproducer.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liaopeng
 * @title: UserController
 * @projectName eureka-producer
 * @description: TODO
 * @date 2021/5/14下午6:04
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 测试多线程中事务一致性问题
     * TODO
     */
    @RequestMapping("/insert")
    public void insertUsers(){
        User user = new User();
        user.setName("夏夜");
        user.setAge(18);
        userService.insert(user);

    }
}
