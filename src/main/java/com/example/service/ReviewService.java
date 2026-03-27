package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dto.ReviewDto;
import com.example.entity.MovieEntity;
import com.example.entity.ReviewEntity;
import com.example.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReviewService {

	private final ReviewRepository reviewRepository;

	// 리뷰 작성
	public void write(ReviewDto dto) {
		reviewRepository.save(dto.toEntity());
	}

	// 리뷰 단건 조회
	public ReviewEntity findById(Long rno) {
		return reviewRepository.findById(rno).orElseThrow();
	}

	// 특정 영화의 리뷰 목록 조회
	public List<ReviewEntity> findByMovie(MovieEntity movie) {
		return reviewRepository.findByMovie(movie);
	}

	// 리뷰 수정
	public void modify(Long rno, int score, String content) {
		ReviewEntity review = findById(rno);
		review.setScore(score);
		review.setContent(content);
		reviewRepository.save(review);
	}

	// 리뷰 삭제
	public void delete(Long rno) {
		reviewRepository.deleteById(rno);
	}
}
