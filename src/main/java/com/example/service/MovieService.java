package com.example.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.dto.MovieDto;
import com.example.entity.MovieEntity;
import com.example.repository.MovieRepository;
import com.example.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MovieService {

	private final MovieRepository movieRepository;
	private final ReviewRepository reviewRepository;
	private final FileService fileService;

	// 페이징 + 검색 조건으로 영화 목록 조회
	public List<MovieEntity> getList(String cat, String searchText, Pageable pageable) {
		if (searchText == null)
			return movieRepository.findAll(pageable).getContent();
		if ("all".equals(cat))
			return movieRepository.searchByKeyword(searchText, pageable);
		if ("director".equals(cat))
			return movieRepository.findByDirectorContaining(searchText, pageable);
		return movieRepository.findByTitleContaining(searchText, pageable);
	}

	// 검색 조건에 맞는 총 영화 수 조회
	public int count(String cat, String searchText) {
		if (searchText == null)
			return (int) movieRepository.count();
		if ("all".equals(cat))
			return movieRepository.countByKeyword(searchText);
		if ("director".equals(cat))
			return movieRepository.countByDirectorContaining(searchText);
		return movieRepository.countByTitleContaining(searchText);
	}

	// 영화 목록의 평균 평점 Map 반환 (쿼리 1번으로 처리)
	public Map<Long, Double> getAvgScores(List<MovieEntity> list) {
		List<Long> mnoList = list.stream().map(MovieEntity::getMno).collect(Collectors.toList());
		Map<Long, Double> map = new HashMap<>();
		for (Object[] row : reviewRepository.findAvgScoresByMovieIds(mnoList))
			map.put((Long) row[0], (Double) row[1]);
		return map;
	}

	// 영화 단건 조회
	public MovieEntity findById(Long mno) {
		return movieRepository.findById(mno).orElseThrow();
	}

	// 영화 등록 (FileService로 포스터 저장)
	public void register(MovieDto dto, MultipartFile poster) throws IOException {
		dto.setPoster(fileService.save(poster));
		movieRepository.save(dto.toEntity());
	}

	// 영화 수정 (포스터 변경 시에만 FileService 호출)
	public void modify(Long mno, MovieDto dto, MultipartFile poster) throws IOException {
		MovieEntity movie = findById(mno);
		movie.setTitle(dto.getTitle());
		movie.setDirector(dto.getDirector());
		movie.setGenre(dto.getGenre());
		movie.setReleaseYear(dto.getReleaseYear());
		movie.setSynopsis(dto.getSynopsis());
		if (poster != null && !poster.isEmpty())
			movie.setPoster(fileService.save(poster));
		movieRepository.save(movie);
	}

	// 영화 삭제
	public void delete(Long mno) {
		movieRepository.deleteById(mno);
	}

	// 특정 영화의 평균 평점 조회
	public Double getAvgScore(Long mno) {
		return reviewRepository.findAvgScoreByMovieMno(mno);
	}
}
