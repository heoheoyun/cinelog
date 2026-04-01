package com.example.service;

import java.util.List;

import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import com.example.dto.ReviewDto;
import com.example.entity.Review;

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
    public Review findById(Long rno) {
        return reviewRepository.findById(rno)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 리뷰입니다: " + rno));
    }

    // 특정 영화의 리뷰 목록 조회
    public List<Review> findByMovieMno(Long mno) {
        return reviewRepository.findByMovieMno(mno);
    }

    // 리뷰 수정
    public void modify(Long rno, int score, String content) {
        Review review = findById(rno);
        review.setScore(score);
        review.setContent(content);
        reviewRepository.save(review);
    }

    // 리뷰 삭제
    public void delete(Long rno) {
        if (!reviewRepository.existsById(rno))
            throw new NoSuchElementException("존재하지 않는 리뷰입니다: " + rno);
        reviewRepository.deleteById(rno);
    }
}
