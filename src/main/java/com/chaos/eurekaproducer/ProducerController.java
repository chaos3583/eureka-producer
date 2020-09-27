package com.chaos.eurekaproducer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: eureka-producer
 * * @description:
 * * @author: liaopeng
 * * @create: 2020-09-27 15:42
 **/
@RestController
public class ProducerController {

    @RequestMapping("/hello")
    public String hello(@RequestParam String name) {
        return "hello "+name+"，this is first message";
    }
}
