package com.QuanLyDatBanNhaHang.demo.service.impl;

import com.QuanLyDatBanNhaHang.demo.dto.request.WebBookingRequestDTO;
import com.QuanLyDatBanNhaHang.demo.entity.*;
import com.QuanLyDatBanNhaHang.demo.enums.*;
import com.QuanLyDatBanNhaHang.demo.repository.*;
import com.QuanLyDatBanNhaHang.demo.service.WebBookingService;
import com.QuanLyDatBanNhaHang.demo.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WebBookingServiceImpl implements WebBookingService {

    private final KhachHangRepository khachHangRepository;
    private final PhieuDatBanRepository phieuDatBanRepository;
    private final BanAnRepository banAnRepository;
    private final ChiTietPhieuDatBanRepository chiTietPhieuDatBanRepository;

    private String generateNextMaPhieu() {
        Integer maxId = phieuDatBanRepository.findMaxMaPhieuDat();
        int nextId = (maxId == null) ? 1 : maxId + 1;
        return String.format("PDB%06d", nextId);
    }
    
    private String generateNextMaKH() {
        Integer maxId = khachHangRepository.findMaxMaKH();
        int nextId = (maxId == null) ? 1 : maxId + 1;
        return String.format("KH%06d", nextId);
    }

    @Override
    @Transactional
    public void createWebBooking(WebBookingRequestDTO request) {
        // 1. Tìm hoặc tạo Khách Hàng
        KhachHang khachHang = khachHangRepository.findAll().stream()
                .filter(kh -> request.getSdt().equals(kh.getSdt()))
                .findFirst()
                .orElseGet(() -> {
                    KhachHang newKh = KhachHang.builder()
                            .maKH(generateNextMaKH())
                            .hoTen(request.getHoTen())
                            .sdt(request.getSdt())
                            .email(request.getEmail())
                            .diemTichLuy(0)
                            .loaiThanhVien(LoaiThanhVienKhachHang.DONG)
                            .build();
                    return khachHangRepository.save(newKh);
                });

        // 2. Validate chống trùng lịch bàn ăn
        LocalDateTime start = request.getThoiGianDen().minusHours(2);
        LocalDateTime end = request.getThoiGianDen().plusHours(2);

        for (Long banId : request.getDanhSachBanId()) {
            BanAn banAn = banAnRepository.findById(banId)
                    .orElseThrow(() -> new ResourceNotFoundException("Bàn ăn không tồn tại: " + banId));

            List<ChiTietPhieuDatBan> conflicts = chiTietPhieuDatBanRepository.findConflictingBookings(banAn.getMaBan(), start, end, null);
            if (!conflicts.isEmpty()) {
                throw new IllegalArgumentException("Bàn " + banAn.getMaBan() + " đã có người đặt trong khung giờ này.");
            }
        }

        // 3. Tạo Phiếu đặt bàn
        PhieuDatBan phieuDatBan = PhieuDatBan.builder()
                .maPhieuDat(generateNextMaPhieu())
                .ngayLapPhieu(LocalDateTime.now())
                .thoiGianDen(request.getThoiGianDen())
                .soLuongNguoi(request.getSoLuongNguoi())
                .ghiChu("Khách đặt qua Web. " + (request.getGhiChu() != null ? request.getGhiChu() : ""))
                .trangThai(TrangThaiPhieuDatBan.CHO_XAC_NHAN)
                .tienDatCoc(0.0)
                .khachHang(khachHang)
                .nhanVien(null) // Cho phép NULL vì khách tự đặt
                .build();

        PhieuDatBan savedPhieu = phieuDatBanRepository.save(phieuDatBan);

        // 4. Tạo ChiTietPhieuDatBan
        for (Long banId : request.getDanhSachBanId()) {
            BanAn banAn = banAnRepository.findById(banId).orElseThrow();
            
            ChiTietPhieuDatBan ct = ChiTietPhieuDatBan.builder()
                    .phieuDatBan(savedPhieu)
                    .banAn(banAn)
                    .ghiChu("")
                    .build();
            chiTietPhieuDatBanRepository.save(ct);
        }
    }
}
