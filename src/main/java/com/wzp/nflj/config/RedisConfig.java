package com.wzp.nflj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 采用 JdkSerializationRedisSerializer 进行序列化和反序列化
 *
 * @Author: zp.wei
 * @DATE: 2020/10/13 13:57
 */
@Configuration
public class RedisConfig {


    /**
     * 该方式采用 JdkSerializationRedisSerializer 进行序列化和反序列化
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        RedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
        //配置序列化(解决乱码的问题)
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                //设置默认缓存1天
                .entryTtl(Duration.ofDays(1L))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(jdkSerializationRedisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jdkSerializationRedisSerializer))
                .disableCachingNullValues();
        //设置缓存空间
        Set<String> cacheNames = new HashSet<>();
        cacheNames.add("nflj");
        // 对每个缓存空间应用不同的配置
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        configMap.put("nflj", config);
        RedisCacheManager cacheManager = RedisCacheManager
                .builder(factory)
                .cacheDefaults(config)
                // 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名
                .initialCacheNames(cacheNames)
                // 再初始化相关的配置
                .withInitialCacheConfigurations(configMap)
                .build();
        return cacheManager;
    }


}
