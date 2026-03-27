package com.example.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.MovieEntity;

public interface MovieRepository extends JpaRepository<MovieEntity, Long> {

    // 제목 검색
    List<MovieEntity> findByTitleContaining(String keyword, Pageable pageable);
    int countByTitleContaining(String keyword);

    // 감독 검색
    List<MovieEntity> findByDirectorContaining(String keyword, Pageable pageable);
    int countByDirectorContaining(String keyword);
}