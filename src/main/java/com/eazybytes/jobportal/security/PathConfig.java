package com.eazybytes.jobportal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PathConfig {

    @Bean(value = "publicPaths")
    public List<String> publicPaths(){
        return List.of(
                "/api/auth/login/public",
                "/api/companies/public",
                "/api/auth/register/public",
                "/api/csrf-token/public",
                "/api/contacts/public",
                "/api/swagger-ui.html",
                "/swagger-ui/**",
                "/api/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/webjars/**");
    }

    @Bean(value = "securedPaths")
    public List<String> securedPaths(){
        return List.of("/api/**");
    }

    @Bean(value = "adminPaths")
    public List<String> adminPaths(){
        return List.of("/api/contacts/admin",
                       "/api/contacts/sort/admin",
                       "/api/contacts/page/admin",
                       "/api/contacts/${id}/status/admin");
    }

}
