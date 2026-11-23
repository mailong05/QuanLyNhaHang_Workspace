package com.QuanLyDatBanNhaHang.demo.controller;

import com.QuanLyDatBanNhaHang.demo.dto.request.WebBookingRequestDTO;
import com.QuanLyDatBanNhaHang.demo.service.WebBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

@RestController
@RequestMapping("/api/web/booking")
@RequiredArgsConstructor
public class WebBookingController {

    private final WebBookingService webBookingService;

    @GetMapping("/available-tables")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAvailableTables(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime thoiGianDen) {
        return ResponseEntity.ok(ApiResponse.success(webBookingService.getAvailableTables(thoiGianDen)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createBooking(@Valid @RequestBody WebBookingRequestDTO request) {
        webBookingService.createWebBooking(request);
        return ResponseEntity.ok(ApiResponse.success("Đặt bàn thành công! Hệ thống đang xử lý và chờ xác nhận.", null));
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<ApiResponse<List<com.QuanLyDatBanNhaHang.demo.dto.response.PhieuDatBanResponseDTO>>> getMyBookings(
            @RequestParam String identifier) {
        return ResponseEntity.ok(ApiResponse.success(webBookingService.getMyBookings(identifier)));
    }

    @PutMapping("/{maPhieuDat}/cancel")
    public ResponseEntity<ApiResponse<String>> cancelBooking(
            @PathVariable String maPhieuDat,
            @RequestParam String identifier) {
        webBookingService.cancelWebBooking(maPhieuDat, identifier);
        return ResponseEntity.ok(ApiResponse.success("Hủy đặt bàn thành công.", null));
    }
}
