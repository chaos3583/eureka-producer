package com.chaos.eurekaproducer.service;


import com.chaos.eurekaproducer.ESUtil;
import com.chaos.eurekaproducer.domain.StoreTransactionLog;
import com.chaos.eurekaproducer.domain.StoreTransactionLogQuery;
import com.chaos.eurekaproducer.mapper.StoreTransactionLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StoreTransactionLogServiceImpl  implements IStoreTransactionLogService{

    @Autowired
    private StoreTransactionLogMapper storeTransactionLogMapper;

    @Override
    public List<StoreTransactionLog> listData(StoreTransactionLogQuery query) {
        List<StoreTransactionLog> list = storeTransactionLogMapper.listData(query);

        return list;
    }

    @Override
    public int batchInsert(List<StoreTransactionLog> list) {
        return storeTransactionLogMapper.batchInsert(list);
    }

    @Override
    public Long count() {
        return storeTransactionLogMapper.count();
    }
}
