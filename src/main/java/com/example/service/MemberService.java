package com.example.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dto.MemberDto;
import com.example.entity.Member;
import com.example.entity.Review;
import com.example.repository.MemberRepository;
import com.example.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final ReviewRepository reviewRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	// 회원가입 (중복 아이디 시 false 반환, 비밀번호 BCrypt 인코딩)
	public boolean register(MemberDto dto) {
		if (memberRepository.existsByUsername(dto.getUsername()))
			return false;
		dto.setPassword(passwordEncoder.encode(dto.getPassword()));
		memberRepository.save(dto.toEntity());
		return true;
	}

	// username으로 회원 조회
	public Member findById(String username) {
		return memberRepository.findById(username).orElseThrow();
	}

	// 닉네임 변경
	public Member updateNickname(String username, String nickname) {
		Member member = findById(username);
		member.setNickname(nickname);
		return memberRepository.save(member);
	}

	// 비밀번호 변경 (현재 비밀번호 BCrypt 검증 후 새 비밀번호 인코딩)
	public boolean updatePassword(String username, String currentPassword, String newPassword) {
		Member member = findById(username);
		if (!passwordEncoder.matches(currentPassword, member.getPassword()))
			return false;
		member.setPassword(passwordEncoder.encode(newPassword));
		memberRepository.save(member);
		return true;
	}

	// 해당 회원의 리뷰 목록 조회 (최신순)
	public List<Review> getMyReviews(Member member) {
		return reviewRepository.findByMemberOrderByRegDateDesc(member);
	}
}
