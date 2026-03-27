package com.example.controller;

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

	// 회원가입 처리
	@PostMapping("/member/reg")
	public String register(MemberDto dto) {
		memberService.register(dto);
		return "redirect:/member/login";
	}

	// 로그인 폼
	@GetMapping("/member/login")
	public String loginForm(String error, Model model) {
		if (error != null)
			model.addAttribute("msg", "아이디 또는 비밀번호가 올바르지 않습니다.");
		return "member/loginForm";
	}

	// 로그인 처리 후 세션 저장
	@PostMapping("/member/login")
	public String login(String username, String password, HttpSession session) {
		MemberEntity member = memberService.login(username, password);
		if (member == null)
			return "redirect:/member/login?error";

		session.setAttribute("loginUser", member);
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
