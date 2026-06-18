package com.QuanLyDatBanNhaHang.demo.controller;

import com.QuanLyDatBanNhaHang.demo.dto.request.WebBookingRequestDTO;
import com.QuanLyDatBanNhaHang.demo.service.WebBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/web/booking")
@RequiredArgsConstructor
public class WebBookingController {

    private final WebBookingService webBookingService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createBooking(@Valid @RequestBody WebBookingRequestDTO request) {
        webBookingService.createWebBooking(request);
        return ResponseEntity.ok(ApiResponse.success("Đặt bàn thành công! Hệ thống đang xử lý và chờ xác nhận.", null));
    }
}
