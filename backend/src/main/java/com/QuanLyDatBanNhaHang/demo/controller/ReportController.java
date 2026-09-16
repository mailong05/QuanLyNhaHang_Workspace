package com.QuanLyDatBanNhaHang.demo.controller;

import com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;
import com.QuanLyDatBanNhaHang.demo.dto.response.DashboardOverviewDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.RecentTransactionDTO;
import com.QuanLyDatBanNhaHang.demo.dto.projection.TopItemProjection;
import com.QuanLyDatBanNhaHang.demo.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/dashboard-overview")
    @PreAuthorize("hasAnyRole('ADMIN', 'NHAN_VIEN')")
    public ResponseEntity<ApiResponse<DashboardOverviewDTO>> getDashboardOverview() {
        return ResponseEntity.ok(ApiResponse.success(reportService.getDashboardOverview()));
    }

    @GetMapping("/recent-transactions")
    @PreAuthorize("hasAnyRole('ADMIN', 'NHAN_VIEN')")
    public ResponseEntity<ApiResponse<List<RecentTransactionDTO>>> getRecentTransactions() {
        return ResponseEntity.ok(ApiResponse.success(reportService.getRecentTransactions()));
    }

    @GetMapping("/top-items")
    @PreAuthorize("hasAnyRole('ADMIN', 'NHAN_VIEN')")
    public ResponseEntity<ApiResponse<List<TopItemProjection>>> getTopItems(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String startDate,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(ApiResponse.success(reportService.getTopItems(startDate, endDate)));
    }

    @GetMapping("/revenue-chart")
    @PreAuthorize("hasAnyRole('ADMIN', 'NHAN_VIEN')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getRevenueChart(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String startDate,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(ApiResponse.success(reportService.getRevenueChart(startDate, endDate)));
    }
}
