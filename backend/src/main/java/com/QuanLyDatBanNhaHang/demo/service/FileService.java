package com.QuanLyDatBanNhaHang.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String storeFile(MultipartFile file);
}
