package com.chaos.eurekaproducer.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author liaopeng
 * @title: RedisMessageListener
 * @projectName myProjects
 * @description: TODO
 * @date 2020/12/225:08 下午
 */
@Component
public class RedisMessageListener implements MessageListener {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * @return the redisTemplate
     */
    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    /**
     * @param redisTemplate the redisTemplate to set
     */
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] body = message.getBody();
        String msgBody = (String) getRedisTemplate().getValueSerializer().deserialize(body);
        System.out.println("消息体："+msgBody);
        String msgPattern = new String(pattern);
        System.out.println("频道："+msgPattern);
    }
}
