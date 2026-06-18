package com.QuanLyDatBanNhaHang.demo.controller;

import com.QuanLyDatBanNhaHang.demo.dto.request.KhuyenMaiCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.KhuyenMaiUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.KhuyenMaiResponseDTO;
import com.QuanLyDatBanNhaHang.demo.service.KhuyenMaiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/khuyen-mai")
@RequiredArgsConstructor
public class KhuyenMaiController {

    private final KhuyenMaiService khuyenMaiService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<KhuyenMaiResponseDTO>>> getAllKhuyenMai(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(khuyenMaiService.getAllKhuyenMai(pageable)));
    }

    @GetMapping("/{maKM}")
    public ResponseEntity<ApiResponse<KhuyenMaiResponseDTO>> getKhuyenMaiById(@PathVariable String maKM) {
        return ResponseEntity.ok(ApiResponse.success(khuyenMaiService.getKhuyenMaiByMa(maKM)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<KhuyenMaiResponseDTO>> createKhuyenMai(@Valid @RequestBody KhuyenMaiCreateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(khuyenMaiService.createKhuyenMai(requestDTO)));
    }

    @PutMapping("/{maKM}")
    public ResponseEntity<ApiResponse<KhuyenMaiResponseDTO>> updateKhuyenMai(@PathVariable String maKM, @Valid @RequestBody KhuyenMaiUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(ApiResponse.success(khuyenMaiService.updateKhuyenMai(maKM, requestDTO)));
    }

    @DeleteMapping("/{maKM}")
    public ResponseEntity<ApiResponse<Void>> deleteKhuyenMai(@PathVariable String maKM) {
        khuyenMaiService.deleteKhuyenMai(maKM);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}
