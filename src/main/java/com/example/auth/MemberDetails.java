package com.example.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.entity.Member;

public class MemberDetails implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Member member;

	public MemberDetails(Member member) {
		this.member = member;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Collection<GrantedAuthority> collection = new ArrayList<>();
		collection.add(new GrantedAuthority() {

			@Override
			public @Nullable String getAuthority() {
				return "ROLE_" + member.getRole().name();
			}
		});

		return collection;
	}

	@Override
	public @Nullable String getPassword() {
		return member.getPassword();
	}

	@Override
	public String getUsername() {
		return member.getUsername();
	}

	public String getNickname() {
		return member.getNickname();
	}

	public String getRole() {
		return member.getRole().name();
	}

	public Member getMember() {
		return member;
	}
}
