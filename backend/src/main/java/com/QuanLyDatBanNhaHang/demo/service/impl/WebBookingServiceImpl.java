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

import java.math.BigDecimal;
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

    public java.util.List<java.util.Map<String, Object>> getAvailableTables(LocalDateTime thoiGianDen) {
        LocalDateTime start = thoiGianDen.minusHours(2);
        LocalDateTime end = thoiGianDen.plusHours(2);

        List<BanAn> allTables = banAnRepository.findAll();
        List<java.util.Map<String, Object>> result = new java.util.ArrayList<>();

        for (BanAn banAn : allTables) {
            boolean isAvailable = false;

            List<ChiTietPhieuDatBan> conflicts = chiTietPhieuDatBanRepository.findConflictingBookings(banAn.getMaBan(), start, end, null);
            isAvailable = conflicts.isEmpty();


            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", banAn.getId());
            map.put("maBan", banAn.getMaBan());
            map.put("soGhe", banAn.getSoGhe());
            map.put("khuVuc", banAn.getKhuVuc() != null ? banAn.getKhuVuc().getTenKhuVuc() : "");
            map.put("isAvailable", isAvailable);
            result.add(map);
        }
        return result;
    }

    @Override
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

        if (request.getDanhSachBanId() != null && !request.getDanhSachBanId().isEmpty()) {
            for (Long banId : request.getDanhSachBanId()) {
                BanAn banAn = banAnRepository.findById(banId)
                        .orElseThrow(() -> new ResourceNotFoundException("Bàn ăn không tồn tại: " + banId));

                List<ChiTietPhieuDatBan> conflicts = chiTietPhieuDatBanRepository.findConflictingBookings(banAn.getMaBan(), start, end, null);
                if (!conflicts.isEmpty()) {
                    throw new IllegalArgumentException("Bàn " + banAn.getMaBan() + " đã có người đặt trong khung giờ này.");
                }
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
                .tienDatCoc(request.getTienDatCoc() != null ? request.getTienDatCoc() : BigDecimal.ZERO)
                .khachHang(khachHang)
                .nhanVien(null) // Cho phép NULL vì khách tự đặt
                .build();

        PhieuDatBan savedPhieu = phieuDatBanRepository.save(phieuDatBan);

        // 4. Tạo ChiTietPhieuDatBan (nếu khách có chọn bàn, nhưng thường web sẽ không chọn)
        if (request.getDanhSachBanId() != null && !request.getDanhSachBanId().isEmpty()) {
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
}
