package com.QuanLyDatBanNhaHang.demo.controller;

import com.QuanLyDatBanNhaHang.demo.dto.request.PhieuDatBanCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.PhieuDatBanUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.PhieuDatBanResponseDTO;
import com.QuanLyDatBanNhaHang.demo.service.PhieuDatBanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/phieu-dat-ban")
@RequiredArgsConstructor
public class PhieuDatBanController {

    private final PhieuDatBanService phieuDatBanService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PhieuDatBanResponseDTO>>> getAllPhieuDatBan(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(phieuDatBanService.getAllPhieuDatBan(pageable)));
    }

    @GetMapping("/{maPhieuDat}")
    public ResponseEntity<ApiResponse<PhieuDatBanResponseDTO>> getPhieuDatBanById(@PathVariable String maPhieuDat) {
        return ResponseEntity.ok(ApiResponse.success(phieuDatBanService.getPhieuDatBanByMa(maPhieuDat)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PhieuDatBanResponseDTO>> createPhieuDatBan(@Valid @RequestBody PhieuDatBanCreateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(phieuDatBanService.createPhieuDatBan(requestDTO)));
    }

    @PutMapping("/{maPhieuDat}")
    public ResponseEntity<ApiResponse<PhieuDatBanResponseDTO>> updatePhieuDatBan(@PathVariable String maPhieuDat, @Valid @RequestBody PhieuDatBanUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(ApiResponse.success(phieuDatBanService.updatePhieuDatBan(maPhieuDat, requestDTO)));
    }

    @DeleteMapping("/{maPhieuDat}")
    public ResponseEntity<ApiResponse<Void>> deletePhieuDatBan(@PathVariable String maPhieuDat) {
        phieuDatBanService.deletePhieuDatBan(maPhieuDat);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}
