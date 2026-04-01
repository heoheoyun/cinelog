package com.example.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.dto.MovieDto;
import com.example.entity.Movie;
import com.example.entity.Review;
import com.example.service.MovieService;
import com.example.service.ReviewService;
import com.example.util.PageHandler;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MovieController {

    private final MovieService movieService;
    private final ReviewService reviewService;

    private final int PAGE_SIZE = 5;

    // 영화 목록 조회 (제목/감독/통합 검색, 페이징)
    @GetMapping("/movie/list")
    public String list(@RequestParam(defaultValue = "1") int page, String cat, String searchText, Model model) {

        if (searchText != null && searchText.isBlank()) searchText = null;
        if (cat != null && cat.isBlank()) cat = null;

        var pageable = PageRequest.of(page - 1, PAGE_SIZE, Sort.by("mno").descending());
        List<Movie> movieList = movieService.getList(cat, searchText, pageable);
        int totalCount = movieService.count(cat, searchText);

        String searchParam = "";
        if (cat != null)        searchParam += "&cat=" + cat;
        if (searchText != null) searchParam += "&searchText=" + searchText;

        model.addAttribute("movieList",   movieList);
        model.addAttribute("avgScores",   movieService.getAvgScores(movieList));
        model.addAttribute("ph",          new PageHandler(totalCount, page, PAGE_SIZE));
        model.addAttribute("cat",         cat);
        model.addAttribute("searchText",  searchText);
        model.addAttribute("searchParam", searchParam);
        return "movie/list";
    }

    // 영화 상세 조회 (리뷰 목록, 평균 평점)
    @GetMapping("/movie/detail")
    public String detail(Long mno, Model model) {
        List<Review> reviewList = reviewService.findByMovieMno(mno);

        model.addAttribute("movie",      movieService.findById(mno));
        model.addAttribute("avgScore",   movieService.getAvgScore(mno));
        model.addAttribute("reviewList", reviewList);
        return "movie/detail";
    }

    // 영화 등록 폼
    @GetMapping("/movie/reg")
    public String regForm() {
        return "movie/regForm";
    }

    // 영화 등록 처리 (유효성 검증)
    @PostMapping("/movie/reg")
    public String register(@Valid MovieDto dto, BindingResult result, MultipartFile posterFile, Model model) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
            return "movie/regForm";
        }
        movieService.register(dto, posterFile);
        return "redirect:/movie/list";
    }

    // 영화 수정 폼
    @GetMapping("/movie/modify")
    public String modifyForm(Long mno, Model model) {
        model.addAttribute("movie", movieService.findById(mno));
        return "movie/modify";
    }

    // 영화 수정 처리 (유효성 검증)
    @PostMapping("/movie/modify")
    public String modify(Long mno, @Valid MovieDto dto, BindingResult result, MultipartFile posterFile, Model model) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("movie", movieService.findById(mno));
            model.addAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
            return "movie/modify";
        }
        movieService.modify(mno, dto, posterFile);
        return "redirect:/movie/detail?mno=" + mno;
    }

    // 영화 삭제
    @PostMapping("/movie/delete")
    public String delete(Long mno) {
        movieService.delete(mno);
        return "redirect:/movie/list";
    }
}
