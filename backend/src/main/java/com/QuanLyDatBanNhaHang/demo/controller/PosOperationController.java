package com.QuanLyDatBanNhaHang.demo.controller;

import com.QuanLyDatBanNhaHang.demo.dto.request.PosMoBanRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.PosThemMonRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;
import com.QuanLyDatBanNhaHang.demo.dto.response.HoaDonResponseDTO;
import com.QuanLyDatBanNhaHang.demo.service.PosOperationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pos")
@RequiredArgsConstructor
public class PosOperationController {

    private final PosOperationService posOperationService;

    @PostMapping("/mo-ban")
    public ResponseEntity<ApiResponse<HoaDonResponseDTO>> moBan(@Valid @RequestBody PosMoBanRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.success(posOperationService.moBan(request)));
    }

    @GetMapping("/ban/{banId}/hoa-don")
    public ResponseEntity<ApiResponse<HoaDonResponseDTO>> layHoaDonTheoBan(@PathVariable Long banId) {
        return ResponseEntity.ok(ApiResponse.success(posOperationService.layHoaDonTheoBan(banId)));
    }

    @PostMapping("/them-mon")
    public ResponseEntity<ApiResponse<HoaDonResponseDTO>> themMon(@Valid @RequestBody PosThemMonRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.success(posOperationService.themMon(request)));
    }

    @PostMapping("/thanh-toan/{hoaDonId}")
    public ResponseEntity<ApiResponse<HoaDonResponseDTO>> thanhToan(
            @PathVariable Long hoaDonId,
            @Valid @RequestBody com.QuanLyDatBanNhaHang.demo.dto.request.PosThanhToanRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.success(posOperationService.thanhToanHoaDon(hoaDonId, request)));
    }
}
