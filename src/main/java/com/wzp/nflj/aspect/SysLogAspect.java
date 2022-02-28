package com.wzp.nflj.aspect;

import com.alibaba.fastjson.JSONObject;
import com.wzp.nflj.config.BaseConfig;
import com.wzp.nflj.model.SysLog;
import com.wzp.nflj.repository.SysLogRepository;
import com.wzp.nflj.util.IpUtil;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


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


    @Pointcut("execution(public * com.wzp.nflj.controller.*.*.*(..))")
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
        String urlStr = request.getRequestURL().toString();
        sysLog.setIp(IpUtil.getRealIp(request));
        sysLog.setMethod(request.getMethod());
        sysLog.setParameter(String.valueOf(JSONObject.toJSON(joinPoint.getArgs())));
        sysLog.setResult(String.valueOf(JSONObject.toJSON(result)));
        sysLog.setSpendTime(endTime - startTime);
        sysLog.setStartTime(startTime);
        sysLog.setUrl(urlStr);
        String urlPath = request.getServletPath();
        if (!urlPath.contains("login")) {
            sysLog.setUsername(new BaseConfig().getUsername());
        }
        sysLogRepository.save(sysLog);
        return result;
    }


}
