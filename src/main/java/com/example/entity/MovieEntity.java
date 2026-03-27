package com.example.entity;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_movies")
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MovieEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_seq")
	@SequenceGenerator(name = "movie_seq", sequenceName = "movie_seq", allocationSize = 1)
	private Long mno;

	@Column(nullable = false, length = 100)
	private String title;

	@Column(nullable = false, length = 50)
	private String director;

	@Column(length = 20)
	private String genre;

	private int releaseYear;

	@Column(length = 1000)
	private String synopsis;

	@Column(length = 200)
	private String poster;

	@CreatedDate
	@Column(updatable = false)
	private LocalDate regDate;

	@Builder
	public MovieEntity(String title, String director, String genre, int releaseYear, String synopsis, String poster) {
		this.title = title;
		this.director = director;
		this.genre = genre;
		this.releaseYear = releaseYear;
		this.synopsis = synopsis;
		this.poster = poster;
	}
}
