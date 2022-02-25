package com.wzp.nflj.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wzp.nflj.config.BaseConfig;
import com.wzp.nflj.model.SysLog;
import com.wzp.nflj.repository.SysLogRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 统一日志切面处理
 *
 * @author zp.wei
 * @date 2022/2/25 15:35
 */
@Aspect
@Component
@Order(1)
public class SysLogAspect {

    @Resource
    private SysLogRepository sysLogRepository;


    @Pointcut("execution(public * com.wzp.nflj.controller.back.*.*(..))")
    public void sysLog() {
    }

    @Before("sysLog()")
    public void doBefore(JoinPoint joinPoint) {
    }

    @AfterReturning(value = "sysLog()", returning = "ret")
    public void doAfterReturning(Object ret) {
    }

    @Around("sysLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //记录请求信息
        SysLog sysLog = new SysLog();
        Object result = joinPoint.proceed();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method.isAnnotationPresent(ApiOperation.class)) {
            ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
            sysLog.setDescription(apiOperation.value());
        }
        long endTime = System.currentTimeMillis();
        sysLog.setUsername(new BaseConfig().getUsername());
        sysLog.setIp(IpUtil.getRealIp(request));
        sysLog.setMethod(request.getMethod());
        sysLog.setParameter(String.valueOf(JSONObject.toJSON(joinPoint.getArgs())));
        sysLog.setResult(String.valueOf(result));
        sysLog.setSpendTime(endTime - startTime);
        sysLog.setStartTime(startTime);
        sysLog.setUrl(request.getRequestURL().toString());
        sysLogRepository.save(sysLog);
        return result;
    }


}
