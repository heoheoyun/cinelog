package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dto.MemberDto;
import com.example.entity.MemberEntity;
import com.example.entity.ReviewEntity;
import com.example.repository.MemberRepository;
import com.example.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final ReviewRepository reviewRepository;

	// 회원가입
	public void register(MemberDto dto) {
		memberRepository.save(dto.toEntity());
	}

	// 아이디 + 비밀번호로 회원 조회 (로그인 검증)
	public MemberEntity login(String username, String password) {
		return memberRepository.findByUsernameAndPassword(username, password);
	}

	// username으로 회원 조회
	public MemberEntity findById(String username) {
		return memberRepository.findById(username).orElseThrow();
	}

	// 닉네임 변경
	public MemberEntity updateNickname(String username, String nickname) {
		MemberEntity member = findById(username);
		member.setNickname(nickname);
		return memberRepository.save(member);
	}

	// 비밀번호 변경 (현재 비밀번호 불일치 시 false 반환)
	public boolean updatePassword(String username, String currentPassword, String newPassword) {
		MemberEntity member = findById(username);
		if (!member.getPassword().equals(currentPassword))
			return false;
		member.setPassword(newPassword);
		memberRepository.save(member);
		return true;
	}

	// 해당 회원의 리뷰 목록 조회 (최신순)
	public List<ReviewEntity> getMyReviews(MemberEntity member) {
		return reviewRepository.findByMemberOrderByRegDateDesc(member);
	}
}
