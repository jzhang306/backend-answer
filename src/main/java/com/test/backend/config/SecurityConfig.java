package com.test.backend.config;
import com.test.backend.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                                response.getWriter().write("Login success\n");
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setContentType("application/json;charset=utf-8");
                            response.setStatus(401);
                            response.getWriter().write("Login failed "+ exception.getMessage());
                        })
                )
                .logout(conf -> conf
                        .logoutUrl("/api/auth/logout")
                        )
                .userDetailsService(accountService)
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
