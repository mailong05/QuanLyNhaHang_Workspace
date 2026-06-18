package com.QuanLyDatBanNhaHang.demo.controller;

import com.QuanLyDatBanNhaHang.demo.dto.request.ChiTietPhieuDatBanCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.ChiTietPhieuDatBanUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.ChiTietPhieuDatBanResponseDTO;
import com.QuanLyDatBanNhaHang.demo.service.ChiTietPhieuDatBanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chi-tiet-phieu-dat-ban")
@RequiredArgsConstructor
public class ChiTietPhieuDatBanController {

    private final ChiTietPhieuDatBanService chiTietPhieuDatBanService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ChiTietPhieuDatBanResponseDTO>>> getAllChiTietPhieuDatBan() {
        return ResponseEntity.ok(ApiResponse.success(chiTietPhieuDatBanService.getAllChiTietPhieuDatBan()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ChiTietPhieuDatBanResponseDTO>> getChiTietPhieuDatBanById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(chiTietPhieuDatBanService.getChiTietPhieuDatBanById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ChiTietPhieuDatBanResponseDTO>> createChiTietPhieuDatBan(@Valid @RequestBody ChiTietPhieuDatBanCreateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(chiTietPhieuDatBanService.createChiTietPhieuDatBan(requestDTO)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ChiTietPhieuDatBanResponseDTO>> updateChiTietPhieuDatBan(@PathVariable Long id, @Valid @RequestBody ChiTietPhieuDatBanUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(ApiResponse.success(chiTietPhieuDatBanService.updateChiTietPhieuDatBan(id, requestDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteChiTietPhieuDatBan(@PathVariable Long id) {
        chiTietPhieuDatBanService.deleteChiTietPhieuDatBan(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}
