package com.example.dto;

import com.example.entity.MemberEntity;
import com.example.entity.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

	@NotBlank(message = "아이디를 입력하세요.")
	@Size(min = 4, max = 20, message = "아이디는 4~20자로 입력하세요.")
	private String username;

	@NotBlank(message = "비밀번호를 입력하세요.")
	@Size(min = 4, max = 20, message = "비밀번호는 4~20자로 입력하세요.")
	private String password;

	@NotBlank(message = "닉네임을 입력하세요.")
	@Size(max = 20, message = "닉네임은 20자 이내로 입력하세요.")
	private String nickname;

	public MemberEntity toEntity() {
		return MemberEntity.builder().username(this.username).password(this.password).nickname(this.nickname)
				.role(Role.USER).build();
	}
}
