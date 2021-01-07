package com.chaos.eurekaproducer.redis;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.internal.guava.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadFactory;

/**
 * @author liaopeng
 * @title: RedisConfig
 * @projectName myProjects
 * @description: TODO
 * @date 2020/12/223:58 下午
 */
@Configuration
@EnableCaching // 开启缓存支持（无此注解，可能无法读取redis配置文件）
public class RedisConfig/* extends CachingConfigurerSupport*/ {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private LettuceConnectionFactory lettuceConnectionFactory;//效率高，线程安全

//    @Resource
//    private JedisConnectionFactory jedisConnectionFactory;//效率低，线程不安全


    /**
     * 自定义缓存key的生成策略。默认的生成策略是看不懂的(乱码内容) 通过Spring 的依赖注入特性进行自定义的配置注入并且此类是一个配置类可以更多程度的自定义配置
     * 根据类名+方法名+所有参数的值生成唯一的一个key
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuffer sb = new StringBuffer();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }


    // 缓存管理器
    @Bean
    public CacheManager cacheManager() {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(lettuceConnectionFactory);
        @SuppressWarnings("serial")
        Set<String> cacheNames = new HashSet<String>() {
            {
                add("codeNameCache");
            }
        };
        builder.initialCacheNames(cacheNames);
        return builder.build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        //  配置 Jackson2JsonRedisSerializer 序列化器，在配置 redisTemplate需要用来做k,v的序列化器
        //此种序列化方式结果清晰、容易阅读、存储字节少、速度快，所以推荐更换
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
                Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        RedisSerializer<?> stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);// key序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);// value序列化
        redisTemplate.setHashKeySerializer(stringSerializer);// Hash key序列化
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);// Hash value序列化
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 发布订阅消息监听配置
     * @param accessTokenChangeEvent
     * @return
     */
    @Bean
    public MessageListenerAdapter accessTokenChangeListenerAdapter(RedisMessageListener accessTokenChangeEvent) {
        //监听适配器。这里需要配置具体处理订阅消息的具体方法，即：accessTokenChangeEvent（该类中实现具体处理订阅消息的代码）
        MessageListenerAdapter messageListener = new MessageListenerAdapter(accessTokenChangeEvent);
        //自定义序列化方式
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        messageListener.setSerializer(jsonRedisSerializer);

        return messageListener;
    }

    /**
     * 发布订阅消息监听配置
     * @param redisConnectionFactory
     * @param accessTokenChangeListenerAdapter
     * @return
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory redisConnectionFactory,
            @Qualifier("accessTokenChangeListenerAdapter") MessageListenerAdapter accessTokenChangeListenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        //订阅频道。和发布消息的频道key保持一致，这里的key值为：access_token_event
        ChannelTopic accessTokenTopic = new ChannelTopic("channel1");
        //向监听容器中新增订阅频道和订阅者。
        container.addMessageListener(accessTokenChangeListenerAdapter, accessTokenTopic);

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(2);
        taskExecutor.setMaxPoolSize(2);
        taskExecutor.setQueueCapacity(1000);
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("redis-message-listener-container-pool-%d")
                .setUncaughtExceptionHandler((thread, exception) ->
                        logger.error(thread.toString(), exception)
                ).build();

        taskExecutor.setThreadFactory(threadFactory);
        taskExecutor.initialize();
        container.setTaskExecutor(taskExecutor);

        return container;
    }
}
