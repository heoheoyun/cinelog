package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {

    // 아이디 + 비밀번호로 회원 조회 (로그인)
    Member findByUsernameAndPassword(String username, String password);

    // 아이디 존재 여부 확인 (중복 체크)
    boolean existsByUsername(String username);
}
