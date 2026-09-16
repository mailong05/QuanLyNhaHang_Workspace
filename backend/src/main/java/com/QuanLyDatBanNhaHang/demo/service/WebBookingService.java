package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.dto.request.WebBookingRequestDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface WebBookingService {
    void createWebBooking(WebBookingRequestDTO request);
    List<Map<String, Object>> getAvailableTables(LocalDateTime thoiGianDen);
}
