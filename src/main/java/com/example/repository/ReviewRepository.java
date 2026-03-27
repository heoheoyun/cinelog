package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.MemberEntity;
import com.example.entity.MovieEntity;
import com.example.entity.ReviewEntity;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    // 특정 영화의 리뷰 목록
    List<ReviewEntity> findByMovie(MovieEntity movie);

    // 특정 회원의 리뷰 목록 (최신순)
    List<ReviewEntity> findByMemberOrderByRegDateDesc(MemberEntity member);
}