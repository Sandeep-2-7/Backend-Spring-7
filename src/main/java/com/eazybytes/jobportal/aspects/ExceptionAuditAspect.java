package com.eazybytes.jobportal.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class ExceptionAuditAspect {

    @AfterThrowing(pointcut = "execution(* com.eazybytes.jobportal..*.*(..))",
                   throwing = "ex")
    public void logAfterException(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.error("Exception occured in the method {}", methodName);
        log.error("Arguments are : {}", Arrays.toString(args));
        log.error("Exception type: {}", ex.getClass().getSimpleName());
        log.error("Exception message: {}", ex.getMessage());

    }
}
