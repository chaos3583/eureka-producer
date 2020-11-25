package com.chaos.eurekaproducer.service;

import com.chaos.eurekaproducer.domain.StoreTransactionLog;
import com.chaos.eurekaproducer.domain.StoreTransactionLogQuery;

import java.util.List;

public interface IStoreTransactionLogService {

    public List<StoreTransactionLog> listData(StoreTransactionLogQuery query);

    int batchInsert(List<StoreTransactionLog> list);

    Long count();
}
