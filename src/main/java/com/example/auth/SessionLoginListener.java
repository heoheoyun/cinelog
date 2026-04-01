package com.example.auth;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

// 폼 로그인 + remember-me 자동 로그인 모두에서 loginUser 세션을 채움
@Component
public class SessionLoginListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

	@Override
	public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
		if (!(event.getAuthentication().getPrincipal() instanceof MemberDetails details))
			return;

		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attrs == null)
			return;

		HttpSession session = attrs.getRequest().getSession();
		session.setAttribute("loginUser", details.getMember());
	}
}
