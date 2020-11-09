package com.chaos.eurekaproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages={"com.chaos"})
@SpringBootApplication
@EnableDiscoveryClient
//@EnableElasticsearchRepositories(basePackages = "com.chaos")//解决注入dao失败的问题
public class EurekaProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaProducerApplication.class, args);
    }

}
