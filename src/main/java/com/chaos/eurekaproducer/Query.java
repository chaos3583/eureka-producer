package com.chaos.eurekaproducer;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Query {

    //索引
    private String index;

    //id
    private String id;

    //数据json串
    private String jsonStr;

    private Integer pageNo;

    private Integer size;

    //类型，默认"_doc",es7.X后只支持"_doc"
    private String type="_doc";

    //精确匹配条件
    private Map<String,Object> matchCondition ;

    //模糊匹配条件
    private Map<String,Object> wildcardCondition ;

    //范围条件
    private Map<String,Object> rangeCondition ;

    //多条件匹配，输入一个条件匹配多个字段，精确匹配
    private Map<String,Object> multiCondition ;

    //多条件匹配，输入一个条件匹配多个字段，模糊匹配
    private Map<String,Object> moreLikeCondition ;



    //排序条件
    private List<String> sortCondition ;

}
