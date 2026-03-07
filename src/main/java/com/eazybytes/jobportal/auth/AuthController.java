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

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JobPortalUserRepository jobPortalUserRepository;

    @PostMapping(value = "/login/public", version = "1.0")
    public ResponseEntity<LoginResponseDto>  login(@RequestBody LoginRequestDto loginReq){
        try{
            var resultAuth = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.username(), loginReq.password()));
            String jwtToken = jwtUtil.generateJWT(resultAuth);
            UserDto user = new UserDto();
            return new ResponseEntity<>(new LoginResponseDto("Ay yooo Login Successful", user,jwtToken), HttpStatus.OK);
        }
        catch (BadCredentialsException badCredException){
            return errorResponse(HttpStatus.UNAUTHORIZED,"Ay yooo "+ loginReq.username()+" check ur credentials");
        }
        catch (AuthenticationException authException){
            return errorResponse(HttpStatus.UNAUTHORIZED,"Ay yooo "+loginReq.username()+" Authentication failed");
        }
        catch (Exception ex){
            return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR," loginReq.username() "+"Internal Server Error Yoo");
        }
    }

    @PostMapping(value = "/register/public", version = "1.0")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDto registerReq){
        Optional<JobPortalUser> existingUser = jobPortalUserRepository.readJobPortalUserByEmailOrMobileNumber(registerReq.getEmail(),registerReq.getMobileNumber());
        if(existingUser.isPresent()){
            Map<String , String> errors = new HashMap<>();
            JobPortalUser user = existingUser.get();
            if(user.getEmail().equals(registerReq.getEmail()))
                errors.put("Email Already Exists","Check pls");
            if (user.getMobileNumber().equals(registerReq.getMobileNumber()))
                errors.put("Mobile Number Already Exists","Check pls");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
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
