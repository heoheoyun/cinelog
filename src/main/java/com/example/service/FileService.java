package com.example.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

// 파일 업로드 공통 처리
@Service
public class FileService {

	private final String uploadDir = "./uploads";

	// 파일 저장 후 저장된 파일명 반환, 파일 없으면 null
	public String save(MultipartFile file) throws IOException {
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
