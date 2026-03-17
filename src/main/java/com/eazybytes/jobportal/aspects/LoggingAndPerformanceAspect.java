package com.eazybytes.jobportal.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAndPerformanceAspect {


    @Around("execution(* com.eazybytes.jobportal..*.*(..))")
    public Object logAndMeasureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("Executing method -> {}", methodName);
        log.info("Arguments of the method -> {}", args);
        Object result = joinPoint.proceed();
        Long endTime = System.currentTimeMillis();
        log.info("Executed the method {} successfully", methodName);
        log.info("Execution time of method -> {}", endTime-startTime);
        return result;
    }
}
