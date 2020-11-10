package com.chaos.eurekaproducer;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: eureka-producer
 * * @description:
 * * @author: liaopeng
 * * @create: 2020-09-27 15:42
 **/
@RestController
public class ProducerController {

    /**
     * 索引创建
     * @param query
     * @return
     * @throws IOException
     */
    @RequestMapping("/createIndex")
    @ResponseBody
    private String createIndex(@RequestBody Query query) throws IOException {
        ESUtil esUtil = new ESUtil();
        String jsonStr = "{\n    \"_doc\": {\n\"properties\": {\n\"name\": {\n\"type\": \"keyword\"\n},\n\"age\": {\n\"type\": \"long\"\n}\n}\n}\n}";
        String result = esUtil.createIndex(query.getIndex(),query.getJsonStr());
        return result;
    }

    /**
     * 删除索引
     * @param query
     * @return
     * @throws IOException
     */
    @RequestMapping("/deleteIndex")
    @ResponseBody
    private String deleteIndex(@RequestBody Query query) throws IOException {
        ESUtil esUtil = new ESUtil();
        String result = esUtil.deleteIndex(query.getIndex());
        return result;
    }


    /**
     * 新增数据
     * @param query
     * @return
     * @throws IOException
     */
    @RequestMapping("/addData")
    @ResponseBody
    private String addData(@RequestBody Query query) throws IOException {
        ESUtil esUtil = new ESUtil();
//        String jsonStr = "{\"name\": \"廖鹏\",\"age\": \"20\"}";
//        String result = esUtil.addData1(index,null);
//        String result = esUtil.addData2(index,jsonStr,null);
        Map<String,Object> map = new HashMap<>();
        map.put("name","夏夜");
        map.put("age",50);
        String result = esUtil.addData2(query.getIndex(), query.getJsonStr(), query.getId());
        return result;
    }


    /**
     * 根据id查询文档
     * @return
     * @throws IOException
     */
    @RequestMapping("/getDocumentById")
    @ResponseBody
    private String getDocumentById(@RequestBody Query query) throws IOException {
        ESUtil esUtil = new ESUtil();
        Map<String, Object> documentById = esUtil.getDocumentById(query.getIndex(),query.getType(),query.getId());
        String result = JSONObject.toJSONString(documentById);
        return result;
    }
}
