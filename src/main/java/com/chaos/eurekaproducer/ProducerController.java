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
        String jsonStr = "{\n" +
                "    \"_doc\": {\n" +
                "        \"properties\": {\n" +
                "            \"id\": {\n" +
                "                \"type\": \"long\"\n" +
                "            },\n" +
                "             \"groupID\": {\n" +
                "                \"type\": \"long\"\n" +
                "            },\n" +
                "             \"houseId\": {\n" +
                "                \"type\": \"long\"\n" +
                "            },\n" +
                "             \"houseName\": {\n" +
                "                \"type\": \"keyword\"\n" +
                "            },\n" +
                "             \"parentId\": {\n" +
                "                \"type\": \"long\"\n" +
                "            },\n" +
                "            \"businessNo\": {\n" +
                "                \"type\": \"text\"\n" +
                "            },\n" +
                "            \"status\": {\n" +
                "                \"type\": \"integer\"\n" +
                "            },\n" +
                "            \"statusStr\": {\n" +
                "                \"type\": \"keyword\"\n" +
                "            },\n" +
                "            \"barcode\": {\n" +
                "                \"type\": \"text\"\n" +
                "            },\n" +
                "            \"goodsName\": {\n" +
                "                \"type\": \"text\"\n" +
                "            },\n" +
                "            \"goodsDesc\": {\n" +
                "                \"type\": \"keyword\"\n" +
                "            },\n" +
                "            \"standardUnit\": {\n" +
                "                \"type\": \"keyword\"\n" +
                "            },\n" +
                "            \"location\": {\n" +
                "                \"type\": \"keyword\"\n" +
                "            },\n" +
                "            \"totalNum\": {\n" +
                "                \"type\": \"double\"\n" +
                "            },\n" +
                "            \"beforeNum\": {\n" +
                "                \"type\": \"double\"\n" +
                "            },\n" +
                "            \"beforeTotalNum\": {\n" +
                "                \"type\": \"double\"\n" +
                "            },\n" +
                "            \"afterNum\": {\n" +
                "                \"type\": \"double\"\n" +
                "            },\n" +
                "             \"afterTotalNum\": {\n" +
                "                \"type\": \"double\"\n" +
                "            },\n" +
                "             \"batch\": {\n" +
                "                \"type\": \"keyword\"\n" +
                "            },\n" +
                "             \"batchSys\": {\n" +
                "                \"type\": \"text\"\n" +
                "            },\n" +
                "            \"productionDate\": {\n" +
                "                \"type\": \"long\"\n" +
                "            },\n" +
                "             \"shelfLife\": {\n" +
                "                \"type\": \"long\"\n" +
                "            },\n" +
                "            \"isQualified\": {\n" +
                "                \"type\": \"integer\"\n" +
                "            },\n" +
                "            \"action\": {\n" +
                "                \"type\": \"integer\"\n" +
                "            },\n" +
                "            \"actionBy\": {\n" +
                "                \"type\": \"keyword\"\n" +
                "            },\n" +
                "            \"actionTime\": {\n" +
                "                \"type\": \"long\"\n" +
                "            },\n" +
                "            \"createBy\": {\n" +
                "                \"type\": \"keyword\"\n" +
                "            },\n" +
                "            \"createTime\": {\n" +
                "                \"type\": \"long\"\n" +
                "            },\n" +
                "            \"ownerId\": {\n" +
                "                \"type\": \"long\"\n" +
                "            },\n" +
                "            \"ownerName\": {\n" +
                "                \"type\": \"keyword\"\n" +
                "            },\n" +
                "            \"goodsCategory1Id\": {\n" +
                "                \"type\": \"long\"\n" +
                "            },\n" +
                "            \"goodsCategory1Name\": {\n" +
                "                \"type\": \"keyword\"\n" +
                "            },\n" +
                "            \"goodsCategory2Id\": {\n" +
                "                \"type\": \"long\"\n" +
                "            },\n" +
                "            \"goodsCategory2Name\": {\n" +
                "                \"type\": \"keyword\"\n" +
                "            },\n" +
                "            \"goodsCategoryId\": {\n" +
                "                \"type\": \"long\"\n" +
                "            },\n" +
                "            \"goodsCategoryName\": {\n" +
                "                \"type\": \"keyword\"\n" +
                "            },\n" +
                "            \"weight\": {\n" +
                "                \"type\": \"double\"\n" +
                "            },\n" +
                "            \"volume\": {\n" +
                "                \"type\": \"double\"\n" +
                "            },\n" +
                "            \"ratio\": {\n" +
                "                \"type\": \"double\"\n" +
                "            }\n" +
                "            \n" +
                "        }\n" +
                "    }\n" +
                "}";
        String result = esUtil.createIndex(query.getIndex(),jsonStr);
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

    public static void main(String[] args) {
        System.out.println(3&9);
    }

}
