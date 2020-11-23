package com.chaos.eurekaproducer.service;


import com.chaos.eurekaproducer.domain.StoreTransactionLog;
import com.chaos.eurekaproducer.mapper.StoreTransactionLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StoreTransactionLogServiceImpl  implements IStoreTransactionLogService{

    @Autowired
    private StoreTransactionLogMapper storeTransactionLogMapper;

    @Override
    public List<StoreTransactionLog> listData() {
        List<StoreTransactionLog> listData = storeTransactionLogMapper.listData();
        return listData;
    }
}
