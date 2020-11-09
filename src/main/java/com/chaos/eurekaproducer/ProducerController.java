package com.chaos.eurekaproducer;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

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

        ESUtil esUtil = new ESUtil();
        esUtil.addData("test1","_doc","1","{\n" +
                "    \"groupID\": 953,\n" +
                "    \"houseId\": 24658946\n" +
                "}");
        return "hello "+name+"，this is first message";
    }

    @RequestMapping("/getDocumentById")
    private String getDocumentById() throws IOException {
        ESUtil esUtil = new ESUtil();
        Map<String, Object> documentById = esUtil.getDocumentById("movies", "_doc", "63");
        String result = JSONObject.toJSONString(documentById);
        return result;
    }
}
