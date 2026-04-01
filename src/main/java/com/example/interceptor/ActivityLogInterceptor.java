package com.example.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.entity.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 사용자 요청 활동을 로그 파일에 기록
public class ActivityLogInterceptor implements HandlerInterceptor {

	private static final Logger log = LoggerFactory.getLogger(ActivityLogInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

		Member loginUser = (Member) request.getSession().getAttribute("loginUser");
		String username = (loginUser != null) ? loginUser.getUsername() : "비로그인";
		String method = request.getMethod();
		String uri = request.getRequestURI();
		String query = request.getQueryString();
		String fullUrl = (query != null) ? uri + "?" + query : uri;

		log.info("[활동] {} | {} {} | IP: {}", username, method, fullUrl, request.getRemoteAddr());
		return true;
	}
}
