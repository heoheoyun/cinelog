package com.example.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.entity.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 요청 처리 중 발생한 예외를 로그 파일에 기록
public class ErrorLogInterceptor implements HandlerInterceptor {

	private static final Logger log = LoggerFactory.getLogger(ErrorLogInterceptor.class);

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {

		if (ex == null)
			return;

		Member loginUser = (Member) request.getSession().getAttribute("loginUser");
		String username = (loginUser != null) ? loginUser.getUsername() : "비로그인";
		String uri = request.getRequestURI();
		int status = response.getStatus();

		log.error("[에러] {} | {} | HTTP {} | {} : {}", username, uri, status, ex.getClass().getSimpleName(),
				ex.getMessage());
	}
}
