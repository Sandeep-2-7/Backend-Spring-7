package com.eazybytes.jobportal.security.util;


import com.eazybytes.jobportal.constants.ApplicationConstants;
import com.eazybytes.jobportal.entity.JobPortalUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@PropertySource(value = "classpath:jwt.properties")
public class JwtUtil {

    private final Environment env;

    @Value("${jwt.issuer:Job Portal}")
    private String issuer;

    @Value("${jwt.subject:JWT Token}")
    private String subject;

    @Value("${jwt.expiration.hours:1}")
    private int expirationHours;

    @Value("${jwt.prod.expiration.hours:1}")
    private int prodExpirationHours;

    public String generateJWT(Authentication authentication) {

        int expirationHrs = expirationHours;
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        if(profiles.contains("qa"))
            expirationHrs = prodExpirationHours;
        String jwt;
        String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        var fetchedUser = (JobPortalUser)authentication.getPrincipal();

        jwt = Jwts.builder().issuer(issuer).subject(subject)
                .claim("name", fetchedUser.getName())
                .claim("email", fetchedUser.getEmail())
                .claim("mobileNumber", fetchedUser.getMobileNumber())
                .claim("roles", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                .issuedAt(new java.util.Date())
                .expiration(new java.util.Date(new java.util.Date().getTime()+expirationHrs*60*60*1000))
                .signWith(secretKey).compact();
        return  jwt;
    }

}
