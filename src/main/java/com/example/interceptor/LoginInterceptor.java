package com.example.interceptor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 비로그인 요청을 로그인 페이지로 리다이렉트 (원래 URL 기억)
public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (request.getSession().getAttribute("loginUser") == null) {
			String uri = request.getRequestURI();
			String query = request.getQueryString();
			String redirectUrl = (query != null) ? uri + "?" + query : uri;

			// redirect URL을 인코딩해야 쿼리스트링의 ?와 &가 outer URL을 깨지 않음
			String encoded = URLEncoder.encode(redirectUrl, StandardCharsets.UTF_8);
			response.sendRedirect("/member/login?redirect=" + encoded);
			return false;
		}
		return true;
	}
}
