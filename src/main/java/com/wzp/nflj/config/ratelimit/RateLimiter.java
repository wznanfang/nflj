package com.wzp.nflj.config.ratelimit;

import com.wzp.nflj.util.RedisUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author zp.wei
 * @date 2023/6/14 13:54
 */
@Aspect
@Component
public class RateLimiter {

    @Resource
    private RedisUtil redisUtil;


    @Around("@annotation(rateLimit)")
    public Object limit(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        String key = "rate:limit:" + joinPoint.getSignature().getName();
        int maxLimit = rateLimit.limit();
        int seconds = rateLimit.time();
        long currentTime = LocalDateTime.now().getSecond();
        long startTime = (currentTime / seconds) * seconds;
        long endTime = startTime + seconds;
        //获取时间段内的请求量
        long allRequests = redisUtil.setCount(key, startTime, endTime);
        if (allRequests > maxLimit) {
            throw new RuntimeException("超过请求限制!");
        }
        redisUtil.setRemoveRangeByScore(key, 0L, currentTime - seconds);
        redisUtil.setAdd(key, currentTime, currentTime);
        redisUtil.setKeyTime(key, seconds + 1, TimeUnit.SECONDS);
        return joinPoint.proceed();
    }


}
