package com.xunni.hotel.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF（使用JWT认证，无需CSRF）
                .csrf(csrf -> csrf.disable())
                // CORS跨域配置
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource())
                )
                // 安全响应头配置
                .headers(headers -> headers
                        // 点击劫持防护
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                        // HSTS配置
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000)
                        )
                )
                // 会话管理 - 无状态（JWT认证）
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 请求授权
                .authorizeRequests(auth -> auth
                        .antMatchers("/auth/**", "/menu/**", "/dashboard/**").permitAll()
                        .antMatchers("/health", "/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                // 禁用默认表单登录
                .formLogin(form -> form.disable())
                // 禁用HTTP Basic认证
                .httpBasic(basic -> basic.disable());
        
        return http.build();
    }
    
    /**
     * CORS跨域配置
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}