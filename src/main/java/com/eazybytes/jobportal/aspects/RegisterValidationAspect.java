package com.eazybytes.jobportal.aspects;

import com.eazybytes.jobportal.dto.RegisterRequestDto;
import com.eazybytes.jobportal.entity.JobPortalUser;
import com.eazybytes.jobportal.exception.RegistrationValidationException;
import com.eazybytes.jobportal.repository.JobPortalUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class RegisterValidationAspect {


    private final CompromisedPasswordChecker compromisedPasswordChecker;
    private final JobPortalUserRepository jobPortalUserRepository;

    @Before("""
            execution(* com.eazybytes.jobportal.auth.AuthController.registerUser(..))
            """)
    public void validBeforeRegistration(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        RegisterRequestDto request = (RegisterRequestDto) args[0];
        log.info("Validating user register request");
        Map<String, String> errors = new HashMap<>();
        CompromisedPasswordDecision decision = compromisedPasswordChecker.check(request.getPassword());
        if (decision.isCompromised()) {
            errors.put("password", "Please enter a strong password");
        }

        Optional<JobPortalUser> existingUser = jobPortalUserRepository.readJobPortalUserByEmailOrMobileNumber(request.getEmail(),request.getMobileNumber());

        if(existingUser.isPresent()){
            JobPortalUser user = existingUser.get();
            if(user.getEmail().equals(request.getEmail()))
                errors.put("Email Already Exists","Check pls");
            if (user.getMobileNumber().equals(request.getMobileNumber()))
                errors.put("Mobile Number Already Exists","Check pls");
        }

        if(!errors.isEmpty()){
            log.error("Registration Validation failed {}", errors);
            throw new RegistrationValidationException(errors);
        }

        log.info("Registration validation successful");
    }
}
