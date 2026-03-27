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
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_reviews")
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ReviewEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long rno;

	@Column(nullable = false)
	private int score;

	@Column(nullable = false, length = 500)
	private String content;

	@ManyToOne
	@JoinColumn(name = "movie_id", nullable = false)
	private MovieEntity movie;

	@ManyToOne
	@JoinColumn(name = "writer", nullable = false)
	private MemberEntity member;

	@CreatedDate
	@Column(updatable = false)
	private LocalDate regDate;

	@LastModifiedDate
	private LocalDate modifyDate;

	@Builder
	public ReviewEntity(int score, String content, MovieEntity movie, MemberEntity member) {
		this.score = score;
		this.content = content;
		this.movie = movie;
		this.member = member;
	}
}
