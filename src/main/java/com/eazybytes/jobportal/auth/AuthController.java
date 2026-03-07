package com.eazybytes.jobportal.auth;

import com.eazybytes.jobportal.dto.LoginRequestDto;
import com.eazybytes.jobportal.dto.LoginResponseDto;
import com.eazybytes.jobportal.dto.UserDto;
import com.eazybytes.jobportal.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController{

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

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

    private ResponseEntity<LoginResponseDto>  errorResponse(HttpStatus status, String message){
        return ResponseEntity.status(status).body(new LoginResponseDto(message,null,null ));
    }
}
