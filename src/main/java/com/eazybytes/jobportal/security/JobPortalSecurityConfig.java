package com.eazybytes.jobportal.security;

import com.eazybytes.jobportal.security.filter.JwtTokenValidatorFilter;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
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
@RequiredArgsConstructor
public class JobPortalSecurityConfig {
//
//    private final PathConfig pathConfig;
//
//    List<String>  publicPath = pathConfig.publicPath();
//    List<String>  securedPath = pathConfig.securedPath();

    @Qualifier("publicPaths")
    private final List<String> publicPaths;

    @Qualifier("securedPaths")
    private final List<String> securedPaths;

    @Qualifier("adminPaths")
    private final List<String> adminPaths;

    @Qualifier("employerPaths")
    private final List<String> employerPaths;

    //    public JobPortalSecurityConfig(
    //            @Qualifier("publicPaths") List<String> publicPaths,
    //            @Qualifier("securedPaths") List<String> securedPaths
    //    ) {
    //        this.publicPaths = publicPaths;
    //        this.securedPaths = securedPaths;
    //    }

    @Bean
    SecurityFilterChain customSecurityFilterChain(HttpSecurity http) {
//        http.authorizeHttpRequests((requests) -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)requests.anyRequest()).authenticated());
        http.csrf(csrfConfig-> csrfConfig.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(requests -> {
                            publicPaths.forEach(path -> requests.requestMatchers(path).permitAll());
                            adminPaths.forEach(path -> requests.requestMatchers(path).hasRole("ADMIN"));
                            employerPaths.forEach(path -> requests.requestMatchers(path).hasRole("EMPLOYER"));
                            securedPaths.forEach(path -> requests.requestMatchers(path).authenticated());

                            requests.anyRequest().denyAll();
                        }
//                .requestMatchers("/api/companies/public").permitAll()
//                .requestMatchers("/api/contacts/public").permitAll()
//                                requests.requestMatchers(RegexRequestMatcher.regexMatcher(".*public$")).permitAll()
                )
                .addFilterBefore(new JwtTokenValidatorFilter(publicPaths), BasicAuthenticationFilter.class);

        http.formLogin(fl->fl.disable());
        http.httpBasic(hb -> hb.disable());
        http.exceptionHandling(eh -> eh.accessDeniedHandler((request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Access Denied\", \"message\":\"You don't have permission to access this resource\"}");

        }));
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
    public AuthenticationManager authenticationManager(JobPortalUsernamePwdAuthenticationProvider authenticationProvider) {
        return new ProviderManager(authenticationProvider);
    }


//    @Bean
//    public UserDetailsService userDetailsService() {
////
////        var pass1 = passwordEncoder().encode("Sandeep123");
////        var pass2 = passwordEncoder().encode("Admin123");
////        System.out.println(pass1);
////        System.out.println(pass2);
//
//        var user1 = User.builder().username("Sandeep").password("$2a$10$3mCXKgAKbrA7/LUmErqoPOWvY/e73A2OBmSlX2YAsC3w9H.oVJJZe").roles("USER").build();
//
//        var user2 = User.builder().username("Admin").password("$2a$10$CTpVx67ojjp2c6L3tru.wOVrmWcpvpjSi0V9xtAyXBmffPoYcViCK").roles("ADMIN").build();
//
//        return new InMemoryUserDetailsManager(user1, user2);
//    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker(){
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}

