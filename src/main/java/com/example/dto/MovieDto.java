package com.example.dto;

import com.example.entity.MovieEntity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {

	@NotBlank(message = "제목을 입력하세요.")
	private String title;

	@NotBlank(message = "감독을 입력하세요.")
	private String director;

	private String genre;

	@Min(value = 1900, message = "개봉년도는 1900년 이후여야 합니다.")
	@Max(value = 2099, message = "개봉년도는 2099년 이하여야 합니다.")
	private int releaseYear;

	private String synopsis;
	private String poster;

	public MovieEntity toEntity() {
		return MovieEntity.builder().title(this.title).director(this.director).genre(this.genre)
				.releaseYear(this.releaseYear).synopsis(this.synopsis).poster(this.poster).build();
	}
}
