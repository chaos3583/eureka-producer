package com.chaos.eurekaproducer.service;

import com.chaos.eurekaproducer.domain.StoreTransactionLog;

import java.util.List;

public interface IStoreTransactionLogService {

    public List<StoreTransactionLog> listData();
}
