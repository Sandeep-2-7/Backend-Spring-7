package com.eazybytes.jobportal.auth;

import com.eazybytes.jobportal.constants.ApplicationConstants;
import com.eazybytes.jobportal.dto.LoginRequestDto;
import com.eazybytes.jobportal.dto.LoginResponseDto;
import com.eazybytes.jobportal.dto.RegisterRequestDto;
import com.eazybytes.jobportal.dto.UserDto;
import com.eazybytes.jobportal.entity.JobPortalUser;
import com.eazybytes.jobportal.entity.Roles;
import com.eazybytes.jobportal.repository.JobPortalUserRepository;
import com.eazybytes.jobportal.repository.RoleRepository;
import com.eazybytes.jobportal.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController{

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final JobPortalUserRepository jobPortalUserRepository;
    private final RoleRepository roleRepository;
    private final CompromisedPasswordChecker compromisedPasswordChecker;


    @PostMapping(value = "/login/public", version = "1.0")
    public ResponseEntity<LoginResponseDto>  login(@RequestBody LoginRequestDto loginReq){
        try{
            var resultAuth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.username(), loginReq.password()));
            String jwtToken = jwtUtil.generateJWT(resultAuth);
            UserDto user = new UserDto();
            JobPortalUser loggedInUser = (JobPortalUser)resultAuth.getPrincipal();
            BeanUtils.copyProperties(loggedInUser,user);
            user.setRole(loggedInUser.getRole().getName());
            user.setUserId(loggedInUser.getId());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new LoginResponseDto(HttpStatus.OK.getReasonPhrase(),
                            user, jwtToken));
        } catch (BadCredentialsException ex) {
            return errorResponse(HttpStatus.UNAUTHORIZED,
                    "Invalid username or password");
        } catch (AuthenticationException ex) {
            return errorResponse(HttpStatus.UNAUTHORIZED,
                    "Authentication failed");
        } catch (Exception ex) {
            return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred");
        }

    }

    @PostMapping(value = "/register/public", version = "1.0")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDto registerReq){

        JobPortalUser jpUser = new JobPortalUser();
        BeanUtils.copyProperties(registerReq,jpUser);
        jpUser.setPasswordHash(passwordEncoder.encode(registerReq.getPassword()));
        Roles role = roleRepository.findRolesByName(ApplicationConstants.ROLE_JOB_SEEKER).orElseThrow(() -> new IllegalArgumentException("Role not found"+ApplicationConstants.ROLE_JOB_SEEKER));
        jpUser.setRole(role);
        jobPortalUserRepository.save(jpUser);

        return ResponseEntity.ok().body("User Registered Successfully");
    }


    private ResponseEntity<LoginResponseDto>  errorResponse(HttpStatus status, String message){
        return ResponseEntity.status(status).body(new LoginResponseDto(message,null,null ));
    }
}
