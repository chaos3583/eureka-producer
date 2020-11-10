package com.chaos.eurekaproducer;

import lombok.Data;

@Data
public class Query {

    //索引
    private String index;

    //id
    private String id;

    //数据json串
    private String jsonStr;

    //类型，默认"_doc",es7.X后只支持"_doc"
    private String type="_doc";

}
