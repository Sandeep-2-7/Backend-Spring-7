package com.eazybytes.jobportal.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.Arrays;
import java.util.Collections;
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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public UserDetailsService userDetailsService() {

        var pass1 = passwordEncoder().encode("Sandeep123");
        var pass2 = passwordEncoder().encode("Admin123");
        System.out.println(pass1);
        System.out.println(pass2);

        var user1 = User.builder().username("Sandeep").password("$2a$10$3mCXKgAKbrA7/LUmErqoPOWvY/e73A2OBmSlX2YAsC3w9H.oVJJZe").roles("USER").build();

        var user2 = User.builder().username("Admin").password("$2a$10$CTpVx67ojjp2c6L3tru.wOVrmWcpvpjSi0V9xtAyXBmffPoYcViCK").roles("ADMIN").build();

        return new InMemoryUserDetailsManager(user1, user2);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

