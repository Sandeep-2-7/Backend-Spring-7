package com.eazybytes.jobportal.aspects;

import com.eazybytes.jobportal.dto.LoginResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoginSuccessAuditAspect {

    @AfterReturning(pointcut = "execution(* com.eazybytes.jobportal.auth.AuthController.login(..))",
                    returning = "response")
    public void logSuccessfulLogin(JoinPoint joinPoint, Object response) {
        if(!(response instanceof ResponseEntity<?> responseEntity))
            return;

        Object body = responseEntity.getBody();
        if(!(body instanceof LoginResponseDto loginResponseDto))
            return;

        if(loginResponseDto.user()!=null){
            String userName = loginResponseDto.user().getName();
            String email = loginResponseDto.user().getEmail();
            log.info("Login Successful for User: {}, Email: {}", userName, email);
        }
    }
}
