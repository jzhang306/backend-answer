package com.test.backend.config;
import com.test.backend.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;




@Configuration
public class SecurityConfig {
    @Resource
    private AccountService accountService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(conf->conf
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(conf->conf
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler((request, response, authentication) -> {
                            response.setContentType("application/json;charset=utf-8");
                            response.getWriter().write("Login successful\n");
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setContentType("application/json;charset=utf-8");
                            response.setStatus(401);
                            response.getWriter().write("Login failed "+ exception.getMessage());
                        })
                )
                .logout(conf -> conf
                        .logoutUrl("/api/auth/logout")
                                .logoutSuccessHandler((request, response, authentication) -> {
                                    response.setContentType("application/json;charset=utf-8");
                                    // without login, you cannot log out!
                                    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
                                        response.setStatus(401);
                                        response.getWriter().write("Logout failed: Not logged in\n");
                                    } else {
                                        response.getWriter().write("Logout success\n");
                                    }
                                })
                        )
                .exceptionHandling(conf->conf
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json;charset=utf-8");
                            response.setStatus(401);
                            response.getWriter().write("Authentication Failed\n");
                        }))
                .userDetailsService(accountService)
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
