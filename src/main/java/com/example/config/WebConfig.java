package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.interceptor.ActivityLogInterceptor;
import com.example.interceptor.ErrorLogInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 모든 요청 활동 기록 (정적 리소스 제외)
        registry.addInterceptor(new ActivityLogInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/js/**", "/uploads/**", "/favicon.ico");

        // 에러 발생 기록
        registry.addInterceptor(new ErrorLogInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/js/**", "/uploads/**", "/favicon.ico");
    }
}