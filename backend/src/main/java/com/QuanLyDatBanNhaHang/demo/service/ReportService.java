package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.dto.response.DashboardOverviewDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.RecentTransactionDTO;
import com.QuanLyDatBanNhaHang.demo.dto.projection.TopItemProjection;
import java.util.List;
import java.util.Map;

public interface ReportService {
    DashboardOverviewDTO getDashboardOverview();
    List<RecentTransactionDTO> getRecentTransactions();
    List<TopItemProjection> getTopItems();
    List<Map<String, Object>> getRevenueChart();
}
