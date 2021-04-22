package com.chaos.eurekaproducer.es;

import com.chaos.eurekaproducer.domain.StoreTransactionLog;
import com.chaos.eurekaproducer.domain.StoreTransactionLogQuery;
import com.chaos.eurekaproducer.service.IStoreTransactionLogService;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: eureka-producer
 * * @description:
 * * @author: liaopeng
 * * @create: 2020-09-27 15:42
 **/
@RestController
@RequestMapping("/transactionLog")
public class StoreTransactionLogController {

    @Autowired
    private IStoreTransactionLogService storeTransactionLogService;
    /**
     * 同步日志表数据
     * @return
     * @throws IOException
     */
    @RequestMapping("/synchronizeData")
    @ResponseBody
    private void synchronizeData(@RequestBody Query query) throws IOException {
        StoreTransactionLogQuery logQuery = new StoreTransactionLogQuery();

        Long total = storeTransactionLogService.count();
        int pageSize =10000;
        long n = total/pageSize;
        for (Long i = 0L; i.compareTo(n) <= 0; i++) {
            logQuery.setPageNo(i.intValue()*pageSize);
            logQuery.setPageSize(pageSize);
//            logQuery.setPageNo(0);
//            logQuery.setPageSize(50000);
            List<StoreTransactionLog> list = storeTransactionLogService.listData(logQuery);
            List<Map<String,Object>> mapList = new ArrayList<>();
            ESUtil esUtil = new ESUtil();
            for (StoreTransactionLog storeTransactionLog : list) {
                Map<String,Object> map = new HashMap<>();
                BeanMap beanMap = BeanMap.create(storeTransactionLog);
                for (Object o : beanMap.keySet()) {
                    map.put(o+"",beanMap.get(o));
                }
                mapList.add(map);
            }
            esUtil.bulkDate(query.getIndex(),"_doc","id" , mapList);
        }
    }


    /**
     * 循环往数据库表插入数据
     * @return
     * @throws IOException
     */
    @RequestMapping("/batchInsert")
    @ResponseBody
    private int batchInsert() throws IOException {
        StoreTransactionLogQuery query = new StoreTransactionLogQuery();
        query.setPageNo(500);
        query.setPageSize(500);
        List<StoreTransactionLog> list = storeTransactionLogService.listData(query);
//        list = list.subList(0,1000);
        list.forEach(storeTransactionLog -> {
            storeTransactionLog.setId(null);
            storeTransactionLog.setActionBy("chaos");
            storeTransactionLog.setActionTime(20201124000000L);
            storeTransactionLog.setCreateBy("chaos");
            storeTransactionLog.setCreateTime(20201124000000L);
        });
        int i = storeTransactionLogService.batchInsert(list);
        return i;

    }

    /**
     * 循环往ES插入数据
     * @return
     * @throws IOException
     */
    @RequestMapping("/batchInsertEs")
    @ResponseBody
    private int batchInsertEs() throws IOException {
        StoreTransactionLogQuery query = new StoreTransactionLogQuery();
        query.setPageNo(1);
        query.setPageSize(500);
        List<StoreTransactionLog> list = storeTransactionLogService.listData(query);
        list = list.subList(500,1000);
        list.forEach(storeTransactionLog -> {
            storeTransactionLog.setId(null);
            storeTransactionLog.setActionBy("chaos");
            storeTransactionLog.setActionTime(20201124000000L);
            storeTransactionLog.setCreateBy("chaos");
            storeTransactionLog.setCreateTime(20201124000000L);
        });
        int i = storeTransactionLogService.batchInsert(list);


        return i;

    }

    /**
     * 查询数据
     * @return
     * @throws IOException
     */
    @RequestMapping("/listData")
    @ResponseBody
    private Map<String,Object> listData(@RequestBody Query query) throws IOException {
        Map<String,Object> result = new HashMap<>();
        ESUtil esUtil = new ESUtil();
        SearchHits searchHits = esUtil.searchByCondition(query);
        if (searchHits!=null){
            long total = searchHits.getTotalHits();
            List<Map<String, Object>> maps = Arrays.stream(searchHits.getHits()).map(b -> {
                return b.getSourceAsMap();
            }).collect(Collectors.toList());
            result.put("total",total);
            result.put("list",maps);
        }
        return result;
    }
}
