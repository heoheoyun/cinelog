package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	// 메인 페이지
	@GetMapping(value = { "/", "/index" })
	public String index() {
		return "index";
	}
}
