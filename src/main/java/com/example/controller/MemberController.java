package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.dto.MemberDto;
import com.example.entity.Member;
import com.example.service.MemberService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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

	// 회원가입 처리 (유효성 검증 + 중복 아이디 확인)
	@PostMapping("/member/reg")
	public String register(@Valid MemberDto dto, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
			return "member/regForm";
		}
		boolean success = memberService.register(dto);
		if (!success) {
			model.addAttribute("error", "이미 사용 중인 아이디입니다.");
			return "member/regForm";
		}
		return "redirect:/member/login";
	}

	// 로그인 폼 (Security가 /member/loginProc으로 처리)
	@GetMapping("/member/login")
	public String loginForm() {
		return "member/loginForm";
	}

	// 내 정보 및 작성 리뷰 목록 조회
	@GetMapping("/member/mypage")
	public String myPage(HttpSession session, Model model) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		Member member = memberService.findById(loginUser.getUsername());
		model.addAttribute("member", member);
		model.addAttribute("reviewList", memberService.getMyReviews(member));
		return "member/myPage";
	}

	// 닉네임 변경 후 세션 갱신
	@PostMapping("/member/edit/nickname")
	public String editNickname(String nickname, HttpSession session) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		Member updated = memberService.updateNickname(loginUser.getUsername(), nickname);
		session.setAttribute("loginUser", updated);
		return "redirect:/member/mypage?edited";
	}

	// 현재 비밀번호 검증 후 변경
	@PostMapping("/member/edit/password")
	public String editPassword(String currentPassword, String newPassword, HttpSession session) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		boolean success = memberService.updatePassword(loginUser.getUsername(), currentPassword, newPassword);
		if (!success)
			return "redirect:/member/mypage?pwError";
		session.setAttribute("loginUser", memberService.findById(loginUser.getUsername()));
		return "redirect:/member/mypage?edited";
	}
}
