package com.chaos.eurekaproducer.mapper;

import com.chaos.eurekaproducer.domain.StoreTransactionLog;
import com.chaos.eurekaproducer.domain.StoreTransactionLogQuery;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface StoreTransactionLogMapper {

    /** 直接操作生产数据*/
    /**数据归档*/
    long separateStorage(StoreTransactionLog query);
    int insert(StoreTransactionLog storeTransactionLog);
    int batchInsert(List<StoreTransactionLog> list);
    /**分表查询 */
    List<StoreTransactionLog> selectSubTableDataList(StoreTransactionLog query);
    List<StoreTransactionLog> selectSubTableStatisticalData(StoreTransactionLog query);

    List<StoreTransactionLog> listData(StoreTransactionLogQuery query);

    Long count();
}
