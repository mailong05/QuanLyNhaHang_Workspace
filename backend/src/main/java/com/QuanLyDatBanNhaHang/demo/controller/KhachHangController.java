package com.QuanLyDatBanNhaHang.demo.controller;

import com.QuanLyDatBanNhaHang.demo.dto.request.KhachHangCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.KhachHangUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.KhachHangResponseDTO;
import com.QuanLyDatBanNhaHang.demo.service.KhachHangService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/khach-hang")
@RequiredArgsConstructor
public class KhachHangController {

    private final KhachHangService khachHangService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<KhachHangResponseDTO>>> getAllKhachHang(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        if (search != null && !search.trim().isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success(khachHangService.searchKhachHang(search, pageable)));
        }
        return ResponseEntity.ok(ApiResponse.success(khachHangService.getAllKhachHang(pageable)));
    }

    @GetMapping("/{maKH}")
    public ResponseEntity<ApiResponse<KhachHangResponseDTO>> getKhachHangById(@PathVariable String maKH) {
        return ResponseEntity.ok(ApiResponse.success(khachHangService.getKhachHangByMa(maKH)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<KhachHangResponseDTO>> createKhachHang(@Valid @RequestBody KhachHangCreateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(khachHangService.createKhachHang(requestDTO)));
    }

    @PutMapping("/{maKH}")
    public ResponseEntity<ApiResponse<KhachHangResponseDTO>> updateKhachHang(@PathVariable String maKH, @Valid @RequestBody KhachHangUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(ApiResponse.success(khachHangService.updateKhachHang(maKH, requestDTO)));
    }

    @DeleteMapping("/{maKH}")
    public ResponseEntity<ApiResponse<Void>> deleteKhachHang(@PathVariable String maKH) {
        khachHangService.deleteKhachHang(maKH);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}
