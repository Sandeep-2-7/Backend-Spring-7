package com.eazybytes.jobportal.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import java.util.List;

@Configuration
@EnableWebSecurity
public class JobPortalSecurityConfig {
//
//    private final PathConfig pathConfig;
//
//    List<String>  publicPath = pathConfig.publicPath();
//    List<String>  securedPath = pathConfig.securedPath();

    @Qualifier("publicpath")
    private final List<String> publicPaths;

    @Qualifier("securedpath")
    private final List<String> securedPaths;

    public JobPortalSecurityConfig(
            @Qualifier("publicpath") List<String> publicPaths,
            @Qualifier("securedpath") List<String> securedPaths
    ) {
        this.publicPaths = publicPaths;
        this.securedPaths = securedPaths;
    }


    @Bean
    SecurityFilterChain customSecurityFilterChain(HttpSecurity http) {
//        http.authorizeHttpRequests((requests) -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)requests.anyRequest()).authenticated());
        http.csrf(csrf->csrf.disable())
                .authorizeHttpRequests(requests -> {
                            publicPaths.forEach(path -> requests.requestMatchers(path).permitAll());
                            securedPaths.forEach(path -> requests.requestMatchers(path).authenticated());
                            requests.anyRequest().denyAll();
                        }
//                .requestMatchers("/api/companies/public").permitAll()
//                .requestMatchers("/api/contacts/public").permitAll()
//                                requests.requestMatchers(RegexRequestMatcher.regexMatcher(".*public$")).permitAll()
                );
        http.formLogin(fl->fl.disable());
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
