package com.QuanLyDatBanNhaHang.demo.service.impl;

import com.QuanLyDatBanNhaHang.demo.dto.request.PosMoBanRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.PosThemMonRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.HoaDonResponseDTO;
import com.QuanLyDatBanNhaHang.demo.entity.*;
import com.QuanLyDatBanNhaHang.demo.enums.*;
import com.QuanLyDatBanNhaHang.demo.exception.ResourceNotFoundException;
import com.QuanLyDatBanNhaHang.demo.repository.*;
import com.QuanLyDatBanNhaHang.demo.service.PosOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PosOperationServiceImpl implements PosOperationService {

    private final BanAnRepository banAnRepository;
    private final PhieuDatBanRepository phieuDatBanRepository;
    private final HoaDonRepository hoaDonRepository;
    private final ThueRepository thueRepository;
    private final NhanVienRepository nhanVienRepository;
    private final KhachHangRepository khachHangRepository;
    private final MonAnRepository monAnRepository;
    private final ChiTietPhieuDatBanRepository chiTietPhieuDatBanRepository;
    private final ChiTietHoaDonRepository chiTietHoaDonRepository;

    @Override
    @Transactional
    public HoaDonResponseDTO moBan(PosMoBanRequestDTO request) {
        // 1. Tìm Bàn Ăn
        BanAn banAn = banAnRepository.findById(request.getBanId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bàn ăn"));

        if (banAn.getTrangThai() != TrangThaiBanAn.TRONG) {
            throw new IllegalArgumentException("Bàn không ở trạng thái trống để mở phục vụ");
        }

        // Cập nhật trạng thái Bàn
        banAn.setTrangThai(TrangThaiBanAn.DANG_SUDUNG);
        banAnRepository.save(banAn);

        // 2. Xử lý Nhân viên
        NhanVien nhanVien;
        if (request.getMaNV() != null) {
            nhanVien = nhanVienRepository.findByMaNVIgnoreCase(request.getMaNV())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên"));
        } else {
            // Lấy NV đầu tiên làm mặc định nếu không gửi
            nhanVien = nhanVienRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Không có nhân viên nào trong hệ thống"));
        }

        // 3. Xử lý Khách Hàng mặc định (Khách Lẻ)
        KhachHang khachLe = khachHangRepository.findAll().stream()
                .filter(kh -> "N/A".equals(kh.getSdt()) || "Khách Lẻ".equals(kh.getHoTen()))
                .findFirst()
                .orElseGet(() -> {
                    KhachHang newKh = KhachHang.builder()
                            .maKH("WLK" + System.currentTimeMillis())
                            .hoTen("Khách Lẻ")
                            .sdt("N/A")
                            .loaiThanhVien(LoaiThanhVienKhachHang.DONG)
                            .diemTichLuy(0)
                            .build();
                    return khachHangRepository.save(newKh);
                });

        // 4. Khởi tạo Phiếu Đặt Bàn ảo (Walk-in)
        PhieuDatBan phieuDatBan = PhieuDatBan.builder()
                .maPhieuDat("PDB" + System.currentTimeMillis())
                .ngayLapPhieu(LocalDateTime.now())
                .thoiGianDen(LocalDateTime.now())
                .soLuongNguoi(banAn.getSoGhe() > 0 ? 1 : 1)
                .ghiChu("WALK-IN")
                .trangThai(TrangThaiPhieuDatBan.DANG_PHUC_VU)
                .khachHang(khachLe)
                .nhanVien(nhanVien)
                .build();
        phieuDatBan = phieuDatBanRepository.save(phieuDatBan);

        // Map Bàn vào Phiếu đặt
        ChiTietPhieuDatBan chiTietPDB = ChiTietPhieuDatBan.builder()
                .phieuDatBan(phieuDatBan)
                .banAn(banAn)
                .ghiChu("Khách lẻ mở bàn")
                .build();
        chiTietPhieuDatBanRepository.save(chiTietPDB);

        // 5. Khởi tạo Hóa Đơn
        Thue thueMacDinh = thueRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Chưa cấu hình Thuế trong hệ thống"));

        HoaDon hoaDon = HoaDon.builder()
                .maHD("HD_" + System.currentTimeMillis())
                .phieuDatBan(phieuDatBan)
                .nhanVien(nhanVien)
                .thue(thueMacDinh)
                .ngayTao(LocalDateTime.now())
                .gioVao(LocalTime.now())
                .trangThaiThanhToan(TrangThaiThanhToanHoaDon.CHUA_THANH_TOAN)
                .thueSuat(thueMacDinh.getThueSuat())
                .tienThue(0.0)
                .tienPhiDV(0.0)
                .tongTienGoc(0.0)
                .tienGiamGia(0.0)
                .tongThanhToan(0.0)
                .build();
        hoaDon = hoaDonRepository.save(hoaDon);

        return mapToDTO(hoaDon);
    }

    @Override
    public HoaDonResponseDTO layHoaDonTheoBan(Long banId) {
        // Tìm Phiếu Đặt Bàn đang phục vụ tại Bàn này
        ChiTietPhieuDatBan chiTiet = chiTietPhieuDatBanRepository.findAll().stream()
                .filter(ct -> ct.getBanAn().getId().equals(banId) && ct.getPhieuDatBan().getTrangThai() == TrangThaiPhieuDatBan.DANG_PHUC_VU)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Bàn chưa được mở phục vụ"));

        PhieuDatBan phieuDatBan = chiTiet.getPhieuDatBan();

        // Tìm Hóa Đơn chưa thanh toán của Phiếu Đặt Bàn này
        HoaDon hoaDon = hoaDonRepository.findAll().stream()
                .filter(hd -> hd.getPhieuDatBan().getId().equals(phieuDatBan.getId()) && hd.getTrangThaiThanhToan() == TrangThaiThanhToanHoaDon.CHUA_THANH_TOAN)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn chưa thanh toán"));

        return mapToDTO(hoaDon);
    }

    @Override
    @Transactional
    public HoaDonResponseDTO themMon(PosThemMonRequestDTO request) {
        // Tìm Hóa đơn đang mở của Bàn này
        HoaDonResponseDTO hoaDonDto = layHoaDonTheoBan(request.getBanId());
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn"));

        MonAn monAn = monAnRepository.findByMaMonIgnoreCase(request.getMaMon())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy món ăn"));

        if (monAn.getTrangThai() == TrangThaiMonAn.HET_HANG) {
            throw new IllegalArgumentException("Món ăn này đã hết hàng");
        }

        final Long currentHoaDonId = hoaDon.getId();
        // Kiểm tra xem món ăn đã có trong hóa đơn chưa
        Optional<ChiTietHoaDon> existingChiTiet = chiTietHoaDonRepository.findAll().stream()
                .filter(ct -> ct.getHoaDon().getId().equals(currentHoaDonId) && ct.getMonAn().getId().equals(monAn.getId()))
                .findFirst();

        if (existingChiTiet.isPresent()) {
            // Đã có -> Cộng dồn số lượng
            ChiTietHoaDon ct = existingChiTiet.get();
            ct.setSoLuong(ct.getSoLuong() + request.getSoLuong());
            ct.setThanhTien(ct.getSoLuong() * ct.getDonGiaLuuTru());
            chiTietHoaDonRepository.save(ct);
        } else {
            // Chưa có -> Tạo mới
            ChiTietHoaDon newCt = ChiTietHoaDon.builder()
                    .hoaDon(hoaDon)
                    .monAn(monAn)
                    .soLuong(request.getSoLuong())
                    .donGiaLuuTru(monAn.getDonGia())
                    .thanhTien(monAn.getDonGia() * request.getSoLuong())
                    .build();
            chiTietHoaDonRepository.save(newCt);
        }

        // Tính lại tổng tiền Hóa Đơn
        List<ChiTietHoaDon> allChiTiet = chiTietHoaDonRepository.findAll().stream()
                .filter(ct -> ct.getHoaDon().getId().equals(currentHoaDonId))
                .toList();

        double tongTienGoc = allChiTiet.stream().mapToDouble(ChiTietHoaDon::getThanhTien).sum();
        hoaDon.setTongTienGoc(tongTienGoc);
        
        // Tính thuế và tổng thanh toán đơn giản
        double tienThue = tongTienGoc * (hoaDon.getThueSuat() != null ? hoaDon.getThueSuat() / 100 : 0);
        hoaDon.setTienThue(tienThue);
        hoaDon.setTongThanhToan(tongTienGoc + tienThue - (hoaDon.getTienGiamGia() != null ? hoaDon.getTienGiamGia() : 0));
        
        hoaDon = hoaDonRepository.save(hoaDon);

        return mapToDTO(hoaDon);
    }

    private HoaDonResponseDTO mapToDTO(HoaDon hd) {
        List<com.QuanLyDatBanNhaHang.demo.dto.response.ChiTietHoaDonResponseDTO> chiTiets = new ArrayList<>();
        if (hd.getChiTietHoaDons() != null) {
            chiTiets = hd.getChiTietHoaDons().stream().map(ct -> com.QuanLyDatBanNhaHang.demo.dto.response.ChiTietHoaDonResponseDTO.builder()
                    .id(ct.getId())
                    .maMon(ct.getMonAn() != null ? ct.getMonAn().getMaMon() : null)
                    .tenMon(ct.getMonAn() != null ? ct.getMonAn().getTenMon() : null)
                    .soLuong(ct.getSoLuong())
                    .donGiaLuuTru(ct.getDonGiaLuuTru() != null ? ct.getDonGiaLuuTru() : 0)
                    .thanhTien(ct.getThanhTien())
                    .ghiChu(ct.getGhiChu())
                    .build()).toList();
        }

        return HoaDonResponseDTO.builder()
                .id(hd.getId())
                .maHD(hd.getMaHD())
                .thueSuat(hd.getThueSuat())
                .tienThue(hd.getTienThue())
                .tyLePhiDV(hd.getTyLePhiDV())
                .tienPhiDV(hd.getTienPhiDV())
                .ngayTao(hd.getNgayTao())
                .gioVao(hd.getGioVao())
                .gioRa(hd.getGioRa())
                .tongTienGoc(hd.getTongTienGoc())
                .tienGiamGia(hd.getTienGiamGia())
                .tongThanhToan(hd.getTongThanhToan())
                .phuongThucTT(hd.getPhuongThucTT())
                .trangThaiThanhToan(hd.getTrangThaiThanhToan())
                .maPhieuDat(hd.getPhieuDatBan() != null ? hd.getPhieuDatBan().getMaPhieuDat() : null)
                .maNV(hd.getNhanVien() != null ? hd.getNhanVien().getMaNV() : null)
                .hoTenNV(hd.getNhanVien() != null ? hd.getNhanVien().getHoTen() : null)
                .maKM(hd.getKhuyenMai() != null ? hd.getKhuyenMai().getMaKM() : null)
                .maThue(hd.getThue() != null ? hd.getThue().getMaThue() : null)
                .chiTiets(chiTiets)
                .build();
    }
}
