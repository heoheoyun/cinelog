package com.example.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 비로그인 요청을 로그인 페이지로 리다이렉트
public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (request.getSession().getAttribute("loginUser") == null) {
			response.sendRedirect("/member/login");
			return false;
		}
		return true;
	}
}
