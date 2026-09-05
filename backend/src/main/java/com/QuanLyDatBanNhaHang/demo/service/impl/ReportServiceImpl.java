package com.QuanLyDatBanNhaHang.demo.service.impl;

import com.QuanLyDatBanNhaHang.demo.dto.response.DashboardOverviewDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.RecentTransactionDTO;
import com.QuanLyDatBanNhaHang.demo.dto.projection.TopItemProjection;
import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiBanAn;
import com.QuanLyDatBanNhaHang.demo.repository.BanAnRepository;
import com.QuanLyDatBanNhaHang.demo.repository.ChiTietHoaDonRepository;
import com.QuanLyDatBanNhaHang.demo.repository.HoaDonRepository;
import com.QuanLyDatBanNhaHang.demo.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final HoaDonRepository hoaDonRepository;
    private final BanAnRepository banAnRepository;
    private final ChiTietHoaDonRepository chiTietHoaDonRepository;

    @Override
    public DashboardOverviewDTO getDashboardOverview() {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        BigDecimal doanhThu = hoaDonRepository.sumDoanhThuByDateRange(startOfDay, endOfDay);
        if (doanhThu == null) doanhThu = BigDecimal.ZERO;

        Long soDon = hoaDonRepository.countDonHangByDateRange(startOfDay, endOfDay);
        if (soDon == null) soDon = 0L;

        Long banDangPhucVu = banAnRepository.countByTrangThaiIn(Arrays.asList(TrangThaiBanAn.DANG_SUDUNG, TrangThaiBanAn.DA_DAT));
        if (banDangPhucVu == null) banDangPhucVu = 0L;

        return DashboardOverviewDTO.builder()
                .doanhThuHomNay(doanhThu)
                .soDonHoanTat(soDon)
                .banDangPhucVu(banDangPhucVu)
                .build();
    }

    @Override
    public List<RecentTransactionDTO> getRecentTransactions() {
        return hoaDonRepository.findRecentTransactions(PageRequest.of(0, 5))
                .stream()
                .map(h -> RecentTransactionDTO.builder()
                        .maHD(h.getMaHD())
                        .thoiGianThanhToan(h.getThoiGianThanhToan())
                        .tongTien(h.getTongThanhToan())
                        .phuongThucThanhToan(h.getPhuongThucTT() != null ? h.getPhuongThucTT().name() : "")
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<TopItemProjection> getTopItems() {
        LocalDateTime startOfMonth = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);
        LocalDateTime endOfMonth = LocalDateTime.of(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()), LocalTime.MAX);
        List<TopItemProjection> topItems = chiTietHoaDonRepository.getTopItems(startOfMonth, endOfMonth);
        return topItems.stream().limit(5).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getRevenueChart() {
        List<Map<String, Object>> chart = new java.util.ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime startOfDay = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endOfDay = LocalDateTime.of(date, LocalTime.MAX);
            BigDecimal doanhThu = hoaDonRepository.sumDoanhThuByDateRange(startOfDay, endOfDay);
            if (doanhThu == null) doanhThu = BigDecimal.ZERO;
            
            chart.add(Map.of(
                "date", date.toString(),
                "doanhThu", doanhThu
            ));
        }
        return chart;
    }
}
