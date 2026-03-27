package com.example.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.dto.MovieDto;
import com.example.entity.MovieEntity;
import com.example.entity.ReviewEntity;
import com.example.repository.MovieRepository;
import com.example.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MovieService {

	private final MovieRepository movieRepository;
	private final ReviewRepository reviewRepository;

	private final String uploadDir = "./uploads";

	// 페이징 + 검색 조건으로 영화 목록 조회
	public List<MovieEntity> getList(String cat, String searchText, Pageable pageable) {
		if (searchText == null)
			return movieRepository.findAll(pageable).getContent();
		if ("director".equals(cat))
			return movieRepository.findByDirectorContaining(searchText, pageable);
		return movieRepository.findByTitleContaining(searchText, pageable);
	}

	// 검색 조건에 맞는 총 영화 수 조회
	public int count(String cat, String searchText) {
		if (searchText == null)
			return (int) movieRepository.count();
		if ("director".equals(cat))
			return movieRepository.countByDirectorContaining(searchText);
		return movieRepository.countByTitleContaining(searchText);
	}

	// 영화 목록의 평균 평점 Map 반환
	public Map<Long, Double> getAvgScores(List<MovieEntity> list) {
		Map<Long, Double> map = new HashMap<>();
		for (MovieEntity m : list) {
			Double avg = calcAvg(reviewRepository.findByMovie(m));
			if (avg != null)
				map.put(m.getMno(), avg);
		}
		return map;
	}

	// 영화 단건 조회
	public MovieEntity findById(Long mno) {
		return movieRepository.findById(mno).orElseThrow();
	}

	// 영화 등록 (포스터 파일 저장 포함)
	public void register(MovieDto dto, MultipartFile poster) throws IOException {
		dto.setPoster(saveFile(poster));
		movieRepository.save(dto.toEntity());
	}

	// 영화 수정 (포스터 변경 시에만 파일 교체)
	public void modify(Long mno, MovieDto dto, MultipartFile poster) throws IOException {
		MovieEntity movie = findById(mno);
		movie.setTitle(dto.getTitle());
		movie.setDirector(dto.getDirector());
		movie.setGenre(dto.getGenre());
		movie.setReleaseYear(dto.getReleaseYear());
		movie.setSynopsis(dto.getSynopsis());
		if (poster != null && !poster.isEmpty())
			movie.setPoster(saveFile(poster));
		movieRepository.save(movie);
	}

	// 영화 삭제
	public void delete(Long mno) {
		movieRepository.deleteById(mno);
	}

	// 리뷰 목록으로 평균 평점 계산
	public Double calcAvg(List<ReviewEntity> reviews) {
		if (reviews == null || reviews.isEmpty())
			return null;
		return reviews.stream().mapToInt(ReviewEntity::getScore).average().orElse(0);
	}

	// 파일 저장 후 저장된 파일명 반환
	private String saveFile(MultipartFile file) throws IOException {
		if (file == null || file.isEmpty())
			return null;
		File dir = new File(uploadDir);
		if (!dir.exists())
			dir.mkdirs();
		String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
		file.transferTo(new File(dir, filename));
		return filename;
	}
}
