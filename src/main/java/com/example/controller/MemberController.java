package com.example.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.dto.MemberDto;
import com.example.entity.MemberEntity;
import com.example.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MemberController {

	private final MemberService memberService;

	// 회원가입 폼
	@GetMapping("/member/reg")
	public String regForm() {
		return "member/regForm";
	}

	// 회원가입 처리 (중복 아이디 시 오류 메시지)
	@PostMapping("/member/reg")
	public String register(MemberDto dto, Model model) {
		boolean success = memberService.register(dto);
		if (!success) {
			model.addAttribute("error", "이미 사용 중인 아이디입니다.");
			return "member/regForm";
		}
		return "redirect:/member/login";
	}

	// 로그인 폼 (redirect 파라미터 유지)
	@GetMapping("/member/login")
	public String loginForm(String error, String redirect, Model model) {
		if (error != null)
			model.addAttribute("msg", "아이디 또는 비밀번호가 올바르지 않습니다.");
		if (redirect != null)
			model.addAttribute("redirect", redirect);
		return "member/loginForm";
	}

	// 로그인 처리 후 세션 저장, redirect 있으면 원래 페이지로 복귀
	@PostMapping("/member/login")
	public String login(String username, String password, String redirect, HttpSession session) {
		MemberEntity member = memberService.login(username, password);
		if (member == null)
			return "redirect:/member/login?error" + (redirect != null ? "&redirect=" + redirect : "");

		session.setAttribute("loginUser", member);

		if (redirect != null && !redirect.isBlank()) {
			String decoded = URLDecoder.decode(redirect, StandardCharsets.UTF_8);
			return "redirect:" + decoded;
		}
		return "redirect:/";
	}

	// 세션 무효화 후 로그아웃
	@GetMapping("/member/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	// 내 정보 및 작성 리뷰 목록 조회 (LoginInterceptor)
	@GetMapping("/member/mypage")
	public String myPage(HttpSession session, Model model) {
		MemberEntity loginUser = (MemberEntity) session.getAttribute("loginUser");
		MemberEntity member = memberService.findById(loginUser.getUsername());
		model.addAttribute("member", member);
		model.addAttribute("reviewList", memberService.getMyReviews(member));
		return "member/myPage";
	}

	// 닉네임 변경 후 세션 갱신
	@PostMapping("/member/edit/nickname")
	public String editNickname(String nickname, HttpSession session) {
		MemberEntity loginUser = (MemberEntity) session.getAttribute("loginUser");
		MemberEntity updated = memberService.updateNickname(loginUser.getUsername(), nickname);
		session.setAttribute("loginUser", updated);
		return "redirect:/member/mypage?edited";
	}

	// 현재 비밀번호 검증 후 변경
	@PostMapping("/member/edit/password")
	public String editPassword(String currentPassword, String newPassword, HttpSession session) {
		MemberEntity loginUser = (MemberEntity) session.getAttribute("loginUser");
		boolean success = memberService.updatePassword(loginUser.getUsername(), currentPassword, newPassword);
		if (!success)
			return "redirect:/member/mypage?pwError";
		session.setAttribute("loginUser", memberService.findById(loginUser.getUsername()));
		return "redirect:/member/mypage?edited";
	}
}
