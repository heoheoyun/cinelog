package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_movie_members")
@Data
@NoArgsConstructor
public class MemberEntity {

	@Id
	@Column(length = 20)
	private String username;

	@Column(nullable = false, length = 20)
	private String password;

	@Column(nullable = false, length = 20)
	private String nickname;

	@Column(name = "user_role", nullable = false, length = 10)
	private String role;

	@Builder
	public MemberEntity(String username, String password, String nickname, String role) {
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.role = role;
	}
}