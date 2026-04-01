package com.example.entity;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_reviews")
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_seq")
	@SequenceGenerator(name = "review_seq", sequenceName = "review_seq", allocationSize = 1)
	private Long rno;

	@Column(nullable = false)
	private int score;

	@Column(nullable = false, length = 500)
	private String content;

	@ManyToOne
	@JoinColumn(name = "movie_id", nullable = false)
	private Movie movie;

	@ManyToOne
	@JoinColumn(name = "writer", nullable = false)
	private Member member;

	@CreatedDate
	@Column(updatable = false)
	private LocalDate regDate;

	@LastModifiedDate
	private LocalDate modifyDate;

	@Builder
	public Review(int score, String content, Movie movie, Member member) {
		this.score = score;
		this.content = content;
		this.movie = movie;
		this.member = member;
	}
}
