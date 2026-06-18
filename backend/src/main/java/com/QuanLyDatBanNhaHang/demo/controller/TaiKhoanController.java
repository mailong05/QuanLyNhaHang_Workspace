package com.QuanLyDatBanNhaHang.demo.controller;

import com.QuanLyDatBanNhaHang.demo.dto.request.TaiKhoanCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.TaiKhoanUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.TaiKhoanResponseDTO;
import com.QuanLyDatBanNhaHang.demo.service.TaiKhoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tai-khoan")
@RequiredArgsConstructor
public class TaiKhoanController {

    private final TaiKhoanService taiKhoanService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TaiKhoanResponseDTO>>> getAllTaiKhoan(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(taiKhoanService.getAllTaiKhoan(pageable)));
    }

    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse<TaiKhoanResponseDTO>> getTaiKhoanById(@PathVariable String username) {
        return ResponseEntity.ok(ApiResponse.success(taiKhoanService.getTaiKhoanByUsername(username)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TaiKhoanResponseDTO>> createTaiKhoan(@Valid @RequestBody TaiKhoanCreateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(taiKhoanService.createTaiKhoan(requestDTO)));
    }

    @PutMapping("/{username}")
    public ResponseEntity<ApiResponse<TaiKhoanResponseDTO>> updateTaiKhoan(@PathVariable String username, @Valid @RequestBody TaiKhoanUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(ApiResponse.success(taiKhoanService.updateTaiKhoan(username, requestDTO)));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<ApiResponse<Void>> deleteTaiKhoan(@PathVariable String username) {
        taiKhoanService.deleteTaiKhoan(username);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}
