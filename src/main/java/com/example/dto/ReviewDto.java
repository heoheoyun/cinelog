package com.example.dto;

import com.example.entity.MemberEntity;
import com.example.entity.MovieEntity;
import com.example.entity.ReviewEntity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class ReviewDto {

	@Min(value = 1, message = "평점은 1점 이상이어야 합니다.")
	@Max(value = 5, message = "평점은 5점 이하여야 합니다.")
	private int score;

	@NotBlank(message = "한 줄 평을 입력하세요.")
	@Size(max = 200, message = "한 줄 평은 200자 이내로 입력하세요.")
	private String content;

	private Long movieId;
	private String writer;

	public ReviewEntity toEntity() {
		MovieEntity movie = new MovieEntity();
		movie.setMno(this.movieId);

		MemberEntity member = new MemberEntity();
		member.setUsername(this.writer);

		return ReviewEntity.builder().score(this.score).content(this.content).movie(movie).member(member).build();
	}
}
