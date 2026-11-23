package com.QuanLyDatBanNhaHang.demo.service.impl;

import com.QuanLyDatBanNhaHang.demo.dto.request.WebBookingRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.ChiTietPhieuDatBanResponseDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.PhieuDatBanResponseDTO;
import com.QuanLyDatBanNhaHang.demo.entity.*;
import com.QuanLyDatBanNhaHang.demo.enums.*;
import com.QuanLyDatBanNhaHang.demo.exception.ResourceNotFoundException;
import com.QuanLyDatBanNhaHang.demo.repository.*;
import com.QuanLyDatBanNhaHang.demo.service.CodeGeneratorService;
import com.QuanLyDatBanNhaHang.demo.service.WebBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebBookingServiceImpl implements WebBookingService {

    private final KhachHangRepository khachHangRepository;
    private final PhieuDatBanRepository phieuDatBanRepository;
    private final BanAnRepository banAnRepository;
    private final ChiTietPhieuDatBanRepository chiTietPhieuDatBanRepository;
    private final CodeGeneratorService codeGeneratorService;

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAvailableTables(LocalDateTime thoiGianDen) {
        LocalDateTime start = thoiGianDen.minusHours(2);
        LocalDateTime end = thoiGianDen.plusHours(2);

        List<BanAn> allTables = banAnRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (BanAn banAn : allTables) {
            List<ChiTietPhieuDatBan> conflicts = chiTietPhieuDatBanRepository.findConflictingBookings(banAn.getMaBan(), start, end, null);
            boolean isAvailable = conflicts.isEmpty();

            Map<String, Object> map = new HashMap<>();
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
    @Transactional(rollbackFor = Exception.class)
    public void createWebBooking(WebBookingRequestDTO request) {
        // 1. Khóa bàn trước để xử lý chống race condition (PESSIMISTIC_WRITE)
        List<BanAn> selectedTables = new ArrayList<>();
        if (request.getDanhSachBanId() != null && !request.getDanhSachBanId().isEmpty()) {
            selectedTables = banAnRepository.findAllByIdWithLock(request.getDanhSachBanId());
            if (selectedTables.size() != request.getDanhSachBanId().size()) {
                throw new ResourceNotFoundException("Một hoặc nhiều bàn ăn được chọn không tồn tại.");
            }

            LocalDateTime start = request.getThoiGianDen().minusHours(2);
            LocalDateTime end = request.getThoiGianDen().plusHours(2);

            for (BanAn banAn : selectedTables) {
                List<ChiTietPhieuDatBan> conflicts = chiTietPhieuDatBanRepository.findConflictingBookings(banAn.getMaBan(), start, end, null);
                if (!conflicts.isEmpty()) {
                    throw new IllegalStateException("Bàn " + banAn.getMaBan() + " vừa có khách hàng khác đặt trong khung giờ này. Vui lòng chọn bàn khác.");
                }
            }
        }

        // 2. Tìm hoặc tạo Khách Hàng (Dùng query có chỉ mục theo SĐT, không kéo toàn bộ DB)
        KhachHang khachHang = khachHangRepository.findBySdt(request.getSdt())
                .orElseGet(() -> {
                    KhachHang newKh = KhachHang.builder()
                            .maKH(codeGeneratorService.generateNextMaKH())
                            .hoTen(request.getHoTen())
                            .sdt(request.getSdt())
                            .email(request.getEmail())
                            .diemTichLuy(0)
                            .loaiThanhVien(LoaiThanhVienKhachHang.DONG)
                            .build();
                    return khachHangRepository.save(newKh);
                });

        // 3. Tạo Phiếu đặt bàn
        PhieuDatBan phieuDatBan = PhieuDatBan.builder()
                .maPhieuDat(codeGeneratorService.generateNextMaPhieuDat())
                .ngayLapPhieu(LocalDateTime.now())
                .thoiGianDen(request.getThoiGianDen())
                .soLuongNguoi(request.getSoLuongNguoi())
                .ghiChu("Khách đặt qua Web. " + (request.getGhiChu() != null ? request.getGhiChu() : ""))
                .trangThai(TrangThaiPhieuDatBan.CHO_XAC_NHAN)
                .tienDatCoc(request.getTienDatCoc() != null ? request.getTienDatCoc() : BigDecimal.ZERO)
                .khachHang(khachHang)
                .nhanVien(null)
                .build();

        PhieuDatBan savedPhieu = phieuDatBanRepository.save(phieuDatBan);

        // 4. Lưu ChiTietPhieuDatBan
        for (BanAn banAn : selectedTables) {
            ChiTietPhieuDatBan ct = ChiTietPhieuDatBan.builder()
                    .phieuDatBan(savedPhieu)
                    .banAn(banAn)
                    .ghiChu("")
                    .build();
            chiTietPhieuDatBanRepository.save(ct);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhieuDatBanResponseDTO> getMyBookings(String phoneOrUsername) {
        List<PhieuDatBan> bookings;
        if (phoneOrUsername != null && phoneOrUsername.matches("^\\d+$")) {
            bookings = phieuDatBanRepository.findHistoryByKhachHangSdt(phoneOrUsername);
        } else {
            bookings = phieuDatBanRepository.findHistoryByKhachHangUsername(phoneOrUsername);
        }

        List<PhieuDatBanResponseDTO> result = new ArrayList<>();
        for (PhieuDatBan p : bookings) {
            List<ChiTietPhieuDatBanResponseDTO> chiTiets = new ArrayList<>();
            if (p.getChiTietPhieuDatBans() != null) {
                for (ChiTietPhieuDatBan ct : p.getChiTietPhieuDatBans()) {
                    chiTiets.add(ChiTietPhieuDatBanResponseDTO.builder()
                            .id(ct.getId())
                            .maBan(ct.getBanAn() != null ? ct.getBanAn().getMaBan() : null)
                            .soGhe(ct.getBanAn() != null ? ct.getBanAn().getSoGhe() : null)
                            .ghiChu(ct.getGhiChu())
                            .build());
                }
            }

            result.add(PhieuDatBanResponseDTO.builder()
                    .id(p.getId())
                    .maPhieuDat(p.getMaPhieuDat())
                    .ngayLapPhieu(p.getNgayLapPhieu())
                    .thoiGianDen(p.getThoiGianDen())
                    .soLuongNguoi(p.getSoLuongNguoi())
                    .ghiChu(p.getGhiChu())
                    .trangThai(p.getTrangThai())
                    .tienDatCoc(p.getTienDatCoc())
                    .maKH(p.getKhachHang() != null ? p.getKhachHang().getMaKH() : null)
                    .hoTenKH(p.getKhachHang() != null ? p.getKhachHang().getHoTen() : null)
                    .sdtKH(p.getKhachHang() != null ? p.getKhachHang().getSdt() : null)
                    .chiTiets(chiTiets)
                    .build());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelWebBooking(String maPhieuDat, String phoneOrUsername) {
        PhieuDatBan pdb = phieuDatBanRepository.findByMaPhieuDatIgnoreCaseWithRelations(maPhieuDat)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu đặt: " + maPhieuDat));

        if (pdb.getTrangThai() != TrangThaiPhieuDatBan.CHO_XAC_NHAN && pdb.getTrangThai() != TrangThaiPhieuDatBan.DA_XAC_NHAN) {
            throw new IllegalStateException("Không thể hủy phiếu đặt ở trạng thái: " + pdb.getTrangThai());
        }

        // Kiểm tra quyền sở hữu
        boolean isOwner = false;
        if (pdb.getKhachHang() != null) {
            if (phoneOrUsername.equalsIgnoreCase(pdb.getKhachHang().getSdt())) {
                isOwner = true;
            } else if (pdb.getKhachHang().getTaiKhoan() != null && phoneOrUsername.equalsIgnoreCase(pdb.getKhachHang().getTaiKhoan().getUsername())) {
                isOwner = true;
            }
        }

        if (!isOwner) {
            throw new IllegalArgumentException("Bạn không có quyền hủy phiếu đặt bàn này.");
        }

        pdb.setTrangThai(TrangThaiPhieuDatBan.DA_HUY);
        pdb.setGhiChu(pdb.getGhiChu() + " [Khách hủy qua web]");
        phieuDatBanRepository.save(pdb);

        // Giải phóng bàn nếu có
        if (pdb.getChiTietPhieuDatBans() != null) {
            for (ChiTietPhieuDatBan ct : pdb.getChiTietPhieuDatBans()) {
                BanAn ba = ct.getBanAn();
                if (ba != null && ba.getTrangThai() == TrangThaiBanAn.DA_DAT) {
                    ba.setTrangThai(TrangThaiBanAn.TRONG);
                    banAnRepository.save(ba);
                }
            }
        }
    }
}
