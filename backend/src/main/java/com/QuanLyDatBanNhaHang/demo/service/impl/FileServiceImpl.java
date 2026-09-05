package com.QuanLyDatBanNhaHang.demo.service.impl;

import com.QuanLyDatBanNhaHang.demo.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final Path fileStorageLocation;

    public FileServiceImpl(@Value("${app.upload.dir:uploads/images/}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Không thể tạo thư mục lưu trữ file.", ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        
        try {
            // Kiểm tra tên file có chứa ký tự không hợp lệ
            if (originalFileName.contains("..")) {
                throw new RuntimeException("Tên file không hợp lệ " + originalFileName);
            }

            // Tạo tên file mới tránh trùng lặp
            String fileExtension = "";
            int lastIndex = originalFileName.lastIndexOf('.');
            if (lastIndex != -1) {
                fileExtension = originalFileName.substring(lastIndex);
            }
            String newFileName = UUID.randomUUID().toString() + fileExtension;

            // Copy file vào thư mục đích
            Path targetLocation = this.fileStorageLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Trả về URL đầy đủ (ví dụ: http://localhost:8080/api/v1/images/abcd.jpg)
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/images/")
                    .path(newFileName)
                    .toUriString();

            return fileDownloadUri;
        } catch (IOException ex) {
            throw new RuntimeException("Không thể lưu file " + originalFileName, ex);
        }
    }
}
