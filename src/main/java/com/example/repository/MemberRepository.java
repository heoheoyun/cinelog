package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {
	MemberEntity findByUsernameAndPassword(String username, String password);
}
