package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.auth.MemberDetails;
import com.example.entity.Member;
import com.example.repository.MemberRepository;

@Service
public class MemberDetailsService implements UserDetailsService {

	@Autowired
	MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Member member = memberRepository.findById(username).orElse(null);

		if (member != null) {
			return new MemberDetails(member);
		}

		return null;
	}
}
