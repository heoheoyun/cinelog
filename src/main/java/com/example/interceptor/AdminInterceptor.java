package com.example.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import com.example.entity.MemberEntity;
import com.example.entity.Role;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// ADMIN 이 아닌 요청을 영화 목록으로 리다이렉트
public class AdminInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		MemberEntity loginUser = (MemberEntity) request.getSession().getAttribute("loginUser");
		if (loginUser == null || loginUser.getRole() != Role.ADMIN) {
			response.sendRedirect("/movie/list");
			return false;
		}
		return true;
	}
}
