package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.dto.ReviewDto;
import com.example.entity.MemberEntity;
import com.example.entity.ReviewEntity;
import com.example.service.ReviewService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 작성 (LoginInterceptor)
    @PostMapping("/review/write")
    public String write(ReviewDto dto, HttpSession session) {
        MemberEntity loginUser = (MemberEntity) session.getAttribute("loginUser");
        dto.setWriter(loginUser.getUsername());
        reviewService.write(dto);
        return "redirect:/movie/detail?mno=" + dto.getMovieId();
    }

    // 리뷰 수정 폼 (본인 리뷰만 접근 가능, from으로 돌아갈 위치 전달)
    @GetMapping("/review/modify")
    public String modifyForm(Long rno, String from, Model model, HttpSession session) {
        ReviewEntity review = reviewService.findById(rno);
        if (!isOwner(session, review))
            return "redirect:/movie/detail?mno=" + review.getMovie().getMno();

        model.addAttribute("review", review);
        model.addAttribute("from", from);
        return "review/modify";
    }

    // 리뷰 수정 처리 (from이 mypage면 마이페이지로, 아니면 영화 상세로)
    @PostMapping("/review/modify")
    public String modify(Long rno, int score, String content, String from, HttpSession session) {
        ReviewEntity review = reviewService.findById(rno);
        if (!isOwner(session, review))
            return "redirect:/movie/detail?mno=" + review.getMovie().getMno();

        reviewService.modify(rno, score, content);

        if ("mypage".equals(from)) return "redirect:/member/mypage";
        return "redirect:/movie/detail?mno=" + review.getMovie().getMno();
    }

    // 리뷰 삭제 (from이 mypage면 마이페이지로, 아니면 영화 상세로)
    @PostMapping("/review/delete")
    public String delete(Long rno, String from, HttpSession session) {
        ReviewEntity review = reviewService.findById(rno);
        Long mno = review.getMovie().getMno();
        if (!isOwner(session, review))
            return "redirect:/movie/detail?mno=" + mno;

        reviewService.delete(rno);

        if ("mypage".equals(from)) return "redirect:/member/mypage";
        return "redirect:/movie/detail?mno=" + mno;
    }

    // 세션 유저와 리뷰 작성자 일치 여부 확인
    private boolean isOwner(HttpSession session, ReviewEntity review) {
        MemberEntity loginUser = (MemberEntity) session.getAttribute("loginUser");
        return loginUser.getUsername().equals(review.getMember().getUsername());
    }
}
