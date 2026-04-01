package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.Member;
import com.example.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 특정 영화의 리뷰 목록 (movieId로 직접 조회 — 더미 엔티티 생성 불필요)
    @Query("SELECT r FROM Review r WHERE r.movie.mno = :mno ORDER BY r.regDate DESC")
    List<Review> findByMovieMno(@Param("mno") Long mno);

    // 특정 회원의 리뷰 목록 (최신순)
    List<Review> findByMemberOrderByRegDateDesc(Member member);

    // 영화 목록의 평균 평점 한 번에 조회 (N+1 제거)
    // 반환: [mno, avgScore] 형태의 Object[]
    @Query("SELECT r.movie.mno, AVG(r.score) FROM Review r WHERE r.movie.mno IN :mnoList GROUP BY r.movie.mno")
    List<Object[]> findAvgScoresByMovieIds(@Param("mnoList") List<Long> mnoList);

    // 특정 영화의 평균 평점
    @Query("SELECT AVG(r.score) FROM Review r WHERE r.movie.mno = :mno")
    Double findAvgScoreByMovieMno(@Param("mno") Long mno);
}
