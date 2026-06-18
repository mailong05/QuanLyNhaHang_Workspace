package com.QuanLyDatBanNhaHang.demo.controller;

import com.QuanLyDatBanNhaHang.demo.dto.request.PhanCongCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.PhanCongUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.PhanCongResponseDTO;
import com.QuanLyDatBanNhaHang.demo.service.PhanCongService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/phan-cong")
@RequiredArgsConstructor
public class PhanCongController {

    private final PhanCongService phanCongService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PhanCongResponseDTO>>> getAllPhanCong(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(phanCongService.getAllPhanCong(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PhanCongResponseDTO>> getPhanCongById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(phanCongService.getPhanCongById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PhanCongResponseDTO>> createPhanCong(@Valid @RequestBody PhanCongCreateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(phanCongService.createPhanCong(requestDTO)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PhanCongResponseDTO>> updatePhanCong(@PathVariable Long id, @Valid @RequestBody PhanCongUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(ApiResponse.success(phanCongService.updatePhanCong(id, requestDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePhanCong(@PathVariable Long id) {
        phanCongService.deletePhanCong(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}
