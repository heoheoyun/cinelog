package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.interceptor.AdminInterceptor;
import com.example.interceptor.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		// 로그인 필요 URL
		registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/member/mypage", "/member/edit/**",
				"/review/**");

		// ADMIN 전용 URL (LoginInterceptor 이후 동작)
		registry.addInterceptor(new AdminInterceptor()).addPathPatterns("/movie/reg", "/movie/modify", "/movie/delete");
	}
}
