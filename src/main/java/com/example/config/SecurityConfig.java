package com.example.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.example.auth.MemberDetails;
import com.example.service.MemberDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final DataSource dataSource;
	private final MemberDetailsService detailsService;

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(auth -> auth.disable());

		// URL 접근 권한
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/member/login", "/member/reg", "/movie/list", "/movie/detail", "/css/**",
						"/js/**", "/uploads/**")
				.permitAll().requestMatchers("/movie/reg", "/movie/modify", "/movie/delete").hasRole("ADMIN")
				.requestMatchers("/member/mypage", "/member/edit/**", "/review/**").authenticated().anyRequest()
				.permitAll());

		// 로그인 - 성공 시 loginUser 세션 저장
		http.formLogin(auth -> auth.loginPage("/member/login").loginProcessingUrl("/member/loginProc")
				.failureUrl("/member/login?error").usernameParameter("username").passwordParameter("password")
				.successHandler((request, response, authentication) -> {
					MemberDetails details = (MemberDetails) authentication.getPrincipal();
					request.getSession().setAttribute("loginUser", details.getMember());
					response.sendRedirect("/");
				}));

		// 로그아웃
		http.logout(auth -> auth.logoutUrl("/member/logout").logoutSuccessUrl("/").invalidateHttpSession(true));

		// Remember-me (7일)
		http.rememberMe(auth -> auth.key("cinelog-remember-me-key").tokenRepository(persistentTokenRepository())
				.userDetailsService(detailsService).tokenValiditySeconds(60 * 60 * 24 * 7));

		return http.build();
	}

	@Bean
	PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
		repo.setDataSource(dataSource);
		return repo;
	}
}
