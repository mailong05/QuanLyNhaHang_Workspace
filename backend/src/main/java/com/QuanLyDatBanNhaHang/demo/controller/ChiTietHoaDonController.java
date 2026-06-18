package com.QuanLyDatBanNhaHang.demo.controller;

import com.QuanLyDatBanNhaHang.demo.dto.request.ChiTietHoaDonCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.ChiTietHoaDonUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.ChiTietHoaDonResponseDTO;
import com.QuanLyDatBanNhaHang.demo.service.ChiTietHoaDonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chi-tiet-hoa-don")
@RequiredArgsConstructor
public class ChiTietHoaDonController {

    private final ChiTietHoaDonService chiTietHoaDonService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ChiTietHoaDonResponseDTO>>> getAllChiTietHoaDon() {
        return ResponseEntity.ok(ApiResponse.success(chiTietHoaDonService.getAllChiTietHoaDon()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ChiTietHoaDonResponseDTO>> getChiTietHoaDonById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(chiTietHoaDonService.getChiTietHoaDonById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ChiTietHoaDonResponseDTO>> createChiTietHoaDon(@Valid @RequestBody ChiTietHoaDonCreateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(chiTietHoaDonService.createChiTietHoaDon(requestDTO)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ChiTietHoaDonResponseDTO>> updateChiTietHoaDon(@PathVariable Long id, @Valid @RequestBody ChiTietHoaDonUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(ApiResponse.success(chiTietHoaDonService.updateChiTietHoaDon(id, requestDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteChiTietHoaDon(@PathVariable Long id) {
        chiTietHoaDonService.deleteChiTietHoaDon(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}
