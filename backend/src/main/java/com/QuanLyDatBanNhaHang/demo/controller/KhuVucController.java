package com.QuanLyDatBanNhaHang.demo.controller;

import com.QuanLyDatBanNhaHang.demo.dto.request.KhuVucCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.KhuVucUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.KhuVucResponseDTO;
import com.QuanLyDatBanNhaHang.demo.service.KhuVucService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/khu-vuc")
@RequiredArgsConstructor
public class KhuVucController {

    private final KhuVucService khuVucService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<KhuVucResponseDTO>>> getAllKhuVuc(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(khuVucService.getAllKhuVuc(pageable)));
    }

    @GetMapping("/{maKhuVuc}")
    public ResponseEntity<ApiResponse<KhuVucResponseDTO>> getKhuVucById(@PathVariable String maKhuVuc) {
        return ResponseEntity.ok(ApiResponse.success(khuVucService.getKhuVucByMa(maKhuVuc)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<KhuVucResponseDTO>> createKhuVuc(@Valid @RequestBody KhuVucCreateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(khuVucService.createKhuVuc(requestDTO)));
    }

    @PutMapping("/{maKhuVuc}")
    public ResponseEntity<ApiResponse<KhuVucResponseDTO>> updateKhuVuc(@PathVariable String maKhuVuc, @Valid @RequestBody KhuVucUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(ApiResponse.success(khuVucService.updateKhuVuc(maKhuVuc, requestDTO)));
    }

    @DeleteMapping("/{maKhuVuc}")
    public ResponseEntity<ApiResponse<Void>> deleteKhuVuc(@PathVariable String maKhuVuc) {
        khuVucService.deleteKhuVuc(maKhuVuc);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}
