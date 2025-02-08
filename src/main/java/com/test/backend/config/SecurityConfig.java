package com.test.backend.config;
import com.test.backend.RestBean;
import com.test.backend.filter.LoginFilter;
import com.test.backend.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfig {
    @Resource
    private AccountService accountService;

    @Resource
    private LoginFilter loginFilter;

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
                            response.setStatus(200);
                            response.getWriter().write(new RestBean(200,"Successful Login!").asJsonString());
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setContentType("application/json;charset=utf-8");
                            response.setStatus(401);
                            response.getWriter().write(new RestBean(401,"Login failed"+ exception.getMessage()).asJsonString());
                        })
                )
                .logout(conf -> conf
                        .logoutUrl("/api/auth/logout")
                                .logoutSuccessHandler((request, response, authentication) -> {
                                    response.setContentType("application/json;charset=utf-8");
                                    // without login, you cannot log out!
                                    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
                                        response.setStatus(401);
                                        response.getWriter().write(new RestBean(401,"You can't log out without login").asJsonString());
                                    } else {
                                        response.setStatus(200);
                                        response.getWriter().write(new RestBean(200,"Log out successfully!").asJsonString());
                                    }
                                })
                        )
                .exceptionHandling(conf->conf
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json;charset=utf-8");
                            response.setStatus(401);
                            response.getWriter().write(new RestBean(401,"Authorization failed").asJsonString());
                        }))
                .addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(accountService)
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
