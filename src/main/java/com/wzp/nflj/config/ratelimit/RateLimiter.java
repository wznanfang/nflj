package com.wzp.nflj.config.ratelimit;

import com.wzp.nflj.config.ratelimit.RateLimit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author zp.wei
 * @date 2023/6/14 13:54
 */
@Aspect
@Component
public class RateLimiter {

    @Autowired
    private RedisTemplate redisTemplate;


    @Around("@annotation(rateLimit)")
    public Object limit(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        String key = "rate:limit:" + joinPoint.getSignature().getName();
        int maxLimit = rateLimit.limit();
        int seconds = rateLimit.time();
        long currentTime = LocalDateTime.now().getSecond();
        long startTime = (currentTime / seconds) * seconds;
        long endTime = startTime + seconds;
        //获取时间段内的请求量
        long allRequests = redisTemplate.opsForZSet().count(key, startTime, endTime);
        if (allRequests > maxLimit) {
            throw new RuntimeException("超过请求限制!");
        }
        //移除过期的记录
        long oldestAllowedTime = currentTime - seconds;
        redisTemplate.opsForZSet().removeRangeByScore(key, 0, oldestAllowedTime);
        //新增一条记录
        redisTemplate.opsForZSet().add(key, currentTime, currentTime);
        redisTemplate.expire(key, seconds + 1, TimeUnit.SECONDS);
        return joinPoint.proceed();
    }


}
