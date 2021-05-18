package com.chaos.eurekaproducer.mapper;

import com.chaos.eurekaproducer.domain.StoreTransactionLog;
import com.chaos.eurekaproducer.domain.StoreTransactionLogQuery;
import com.chaos.eurekaproducer.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface UserMapper {

    public int insert(User user);

    public int batchInsert(List<User> users);

}
