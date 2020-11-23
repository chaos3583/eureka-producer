package com.chaos.eurekaproducer;

import com.alibaba.fastjson.JSONObject;
import com.chaos.eurekaproducer.domain.StoreTransactionLog;
import com.chaos.eurekaproducer.service.IStoreTransactionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: eureka-producer
 * * @description:
 * * @author: liaopeng
 * * @create: 2020-09-27 15:42
 **/
@RestController
public class StoreTransactionLogController {

    @Autowired
    private IStoreTransactionLogService storeTransactionLogService;
    /**
     * 查询数据
     * @return
     * @throws IOException
     */
    @RequestMapping("/listData")
    @ResponseBody
    private List<StoreTransactionLog> listData() throws IOException {
        List<StoreTransactionLog> list = storeTransactionLogService.listData();
        return list;
    }

}
