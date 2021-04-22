package com.chaos.eurekaproducer.service;


import com.chaos.eurekaproducer.annotation.Transaction;
import com.chaos.eurekaproducer.domain.StoreTransactionLog;
import com.chaos.eurekaproducer.domain.StoreTransactionLogQuery;
import com.chaos.eurekaproducer.mapper.StoreTransactionLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
        Long count = storeTransactionLogMapper.count();
        System.out.println(count);
        return count;
    }

    @Override
    public String testString(String str) {
        System.out.println(str);
        return str;
    }


}
