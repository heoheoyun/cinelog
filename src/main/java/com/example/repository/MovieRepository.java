package com.example.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    // 제목 검색 (파생 쿼리)
    List<Movie> findByTitleContaining(String keyword, Pageable pageable);
    int countByTitleContaining(String keyword);

    // 감독 검색 (파생 쿼리)
    List<Movie> findByDirectorContaining(String keyword, Pageable pageable);
    int countByDirectorContaining(String keyword);

    // 제목 또는 감독 통합 검색 (JPQL)
    @Query("SELECT m FROM Movie m WHERE m.title LIKE %:keyword% OR m.director LIKE %:keyword%")
    List<Movie> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(m) FROM Movie m WHERE m.title LIKE %:keyword% OR m.director LIKE %:keyword%")
    int countByKeyword(@Param("keyword") String keyword);
}
