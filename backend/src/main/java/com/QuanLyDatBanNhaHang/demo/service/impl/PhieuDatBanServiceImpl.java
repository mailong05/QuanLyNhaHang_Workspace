package com.QuanLyDatBanNhaHang.demo.service.impl;

import com.QuanLyDatBanNhaHang.demo.dto.request.ChiTietPhieuDatBanCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.PhieuDatBanCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.PhieuDatBanUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.ChiTietPhieuDatBanResponseDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.PhieuDatBanResponseDTO;
import com.QuanLyDatBanNhaHang.demo.entity.*;
import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiPhieuDatBan;
import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThanhToanHoaDon;
import com.QuanLyDatBanNhaHang.demo.exception.DuplicateResourceException;
import com.QuanLyDatBanNhaHang.demo.exception.ResourceNotFoundException;
import com.QuanLyDatBanNhaHang.demo.repository.BanAnRepository;
import com.QuanLyDatBanNhaHang.demo.repository.KhachHangRepository;
import com.QuanLyDatBanNhaHang.demo.repository.NhanVienRepository;
import com.QuanLyDatBanNhaHang.demo.repository.PhieuDatBanRepository;
import com.QuanLyDatBanNhaHang.demo.repository.ThueRepository;
import com.QuanLyDatBanNhaHang.demo.service.PhieuDatBanService;
import com.QuanLyDatBanNhaHang.demo.repository.HoaDonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhieuDatBanServiceImpl implements PhieuDatBanService {

    private final PhieuDatBanRepository phieuDatBanRepository;
    private final com.QuanLyDatBanNhaHang.demo.repository.ChiTietPhieuDatBanRepository chiTietPhieuDatBanRepository;
    private final KhachHangRepository khachHangRepository;
    private final NhanVienRepository nhanVienRepository;
    private final BanAnRepository banAnRepository;
    private final HoaDonRepository hoaDonRepository;
    private final ThueRepository thueRepository;

    @Override
    public Page<PhieuDatBanResponseDTO> getAllPhieuDatBan(Pageable pageable) {
        return phieuDatBanRepository.findAllWithRelations(pageable).map(this::convertToResponseDTO);
    }

    @Override
    public PhieuDatBanResponseDTO getPhieuDatBanByMa(String maPhieuDat) {
        PhieuDatBan pdb = phieuDatBanRepository.findByMaPhieuDatIgnoreCaseWithRelations(maPhieuDat)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Phiếu đặt với mã: " + maPhieuDat));
        return convertToResponseDTO(pdb);
    }

    @Override
    @Transactional
    public PhieuDatBanResponseDTO createPhieuDatBan(PhieuDatBanCreateRequestDTO requestDTO) {

        if (requestDTO.getThoiGianDen().isBefore(LocalDateTime.now().plusMinutes(30))) {
            throw new IllegalArgumentException("Thời gian đến phải lớn hơn thời gian hiện tại ít nhất 30 phút.");
        }

        if (requestDTO.getChiTiets() != null) {
            LocalDateTime start = requestDTO.getThoiGianDen().minusHours(2);
            LocalDateTime end = requestDTO.getThoiGianDen().plusHours(2);
            for (var ct : requestDTO.getChiTiets()) {
                if (!chiTietPhieuDatBanRepository.findConflictingBookings(ct.getMaBan(), start, end, null).isEmpty()) {
                    throw new IllegalArgumentException("Bàn " + ct.getMaBan() + " đã được đặt trong khoảng thời gian này.");
                }
            }
        }



        KhachHang kh = khachHangRepository.findByMaKHIgnoreCase(requestDTO.getMaKH())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Khách hàng: " + requestDTO.getMaKH()));
        
        NhanVien nv = null;
        if (requestDTO.getMaNV() != null && !requestDTO.getMaNV().isBlank()) {
            nv = nhanVienRepository.findByMaNVIgnoreCase(requestDTO.getMaNV())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Nhân viên: " + requestDTO.getMaNV()));
        }

        PhieuDatBan pdb = PhieuDatBan.builder()
                .maPhieuDat(generateNextMaPhieuDat())
                .ngayLapPhieu(LocalDateTime.now())
                .thoiGianDen(requestDTO.getThoiGianDen())
                .soLuongNguoi(requestDTO.getSoLuongNguoi())
                .ghiChu(requestDTO.getGhiChu())
                .trangThai(requestDTO.getTrangThai())
                .tienDatCoc(requestDTO.getTienDatCoc())
                .khachHang(kh)
                .nhanVien(nv)
                .chiTietPhieuDatBans(new ArrayList<>())
                .build();

        if (requestDTO.getChiTiets() != null) {
            for (ChiTietPhieuDatBanCreateRequestDTO cReq : requestDTO.getChiTiets()) {
                BanAn ba = banAnRepository.findByMaBanIgnoreCase(cReq.getMaBan())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Bàn ăn: " + cReq.getMaBan()));
                
                ChiTietPhieuDatBan ct = ChiTietPhieuDatBan.builder()
                        .phieuDatBan(pdb)
                        .banAn(ba)
                        .ghiChu(cReq.getGhiChu())
                        .build();
                pdb.getChiTietPhieuDatBans().add(ct);
            }
        }

        
        return convertToResponseDTO(phieuDatBanRepository.save(pdb));

    }

    @Override
    @Transactional
    public PhieuDatBanResponseDTO updatePhieuDatBan(String maPhieuDat, PhieuDatBanUpdateRequestDTO requestDTO) {
        PhieuDatBan pdb = phieuDatBanRepository.findByMaPhieuDatIgnoreCaseWithRelations(maPhieuDat)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Phiếu đặt với mã: " + maPhieuDat));

        if (!requestDTO.getThoiGianDen().equals(pdb.getThoiGianDen()) && requestDTO.getThoiGianDen().isBefore(LocalDateTime.now().plusMinutes(30))) {
            throw new IllegalArgumentException("Thời gian đến phải lớn hơn thời gian hiện tại ít nhất 30 phút.");
        }

        if (requestDTO.getChiTiets() != null) {
            LocalDateTime start = requestDTO.getThoiGianDen().minusHours(2);
            LocalDateTime end = requestDTO.getThoiGianDen().plusHours(2);
            for (var ct : requestDTO.getChiTiets()) {
                if (!chiTietPhieuDatBanRepository.findConflictingBookings(ct.getMaBan(), start, end, pdb.getId()).isEmpty()) {
                    throw new IllegalArgumentException("Bàn " + ct.getMaBan() + " đã được đặt trong khoảng thời gian này.");
                }
            }
        }


        pdb.setThoiGianDen(requestDTO.getThoiGianDen());
        pdb.setSoLuongNguoi(requestDTO.getSoLuongNguoi());
        pdb.setGhiChu(requestDTO.getGhiChu());
        pdb.setTrangThai(requestDTO.getTrangThai());
        pdb.setTienDatCoc(requestDTO.getTienDatCoc());

        // Update KhachHang if requested
        if (pdb.getKhachHang() != null) {
            if (requestDTO.getHoTenKH() != null && !requestDTO.getHoTenKH().isBlank()) {
                pdb.getKhachHang().setHoTen(requestDTO.getHoTenKH());
            }
            if (requestDTO.getSdtKH() != null && !requestDTO.getSdtKH().isBlank()) {
                pdb.getKhachHang().setSdt(requestDTO.getSdtKH());
            }
            khachHangRepository.save(pdb.getKhachHang());
        }

        // Xóa chi tiết cũ và map chi tiết mới
        if (requestDTO.getChiTiets() != null) {
            // Lấy danh sách mã bàn mới
            java.util.List<String> newTableCodes = requestDTO.getChiTiets().stream().map(ChiTietPhieuDatBanCreateRequestDTO::getMaBan).toList();
            // Restore trạng thái bàn cũ về TRONG nếu phiếu đang phục vụ, nhưng chỉ các bàn KHÔNG nằm trong danh sách mới
            if (pdb.getTrangThai() == TrangThaiPhieuDatBan.DANG_PHUC_VU || pdb.getTrangThai() == TrangThaiPhieuDatBan.DA_GOP_BAN || pdb.getTrangThai() == TrangThaiPhieuDatBan.HOAN_THANH || pdb.getTrangThai() == TrangThaiPhieuDatBan.DA_HUY) {
                for (ChiTietPhieuDatBan oldCt : pdb.getChiTietPhieuDatBans()) {
                    if (!newTableCodes.contains(oldCt.getBanAn().getMaBan())) {
                        oldCt.getBanAn().setTrangThai(com.QuanLyDatBanNhaHang.demo.enums.TrangThaiBanAn.TRONG);
                        banAnRepository.save(oldCt.getBanAn());
                    }
                }
            }

            pdb.getChiTietPhieuDatBans().clear();
            for (ChiTietPhieuDatBanCreateRequestDTO cReq : requestDTO.getChiTiets()) {
                BanAn ba = banAnRepository.findByMaBanIgnoreCase(cReq.getMaBan())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Bàn ăn: " + cReq.getMaBan()));
                
                if (requestDTO.getTrangThai() == TrangThaiPhieuDatBan.DANG_PHUC_VU) {
                    ba.setTrangThai(com.QuanLyDatBanNhaHang.demo.enums.TrangThaiBanAn.DANG_SUDUNG);
                    banAnRepository.save(ba);
                } else if (requestDTO.getTrangThai() == TrangThaiPhieuDatBan.DA_XAC_NHAN) {
                    ba.setTrangThai(com.QuanLyDatBanNhaHang.demo.enums.TrangThaiBanAn.DA_DAT);
                    banAnRepository.save(ba);
                }

                ChiTietPhieuDatBan ct = ChiTietPhieuDatBan.builder()
                        .phieuDatBan(pdb)
                        .banAn(ba)
                        .ghiChu(cReq.getGhiChu())
                        .build();
                pdb.getChiTietPhieuDatBans().add(ct);
            }
        } else {
             // Cập nhật trạng thái phiếu mà không đổi bàn (ví dụ Hủy phiếu, Thanh toán)
             if (requestDTO.getTrangThai() == TrangThaiPhieuDatBan.HOAN_THANH || requestDTO.getTrangThai() == TrangThaiPhieuDatBan.DA_HUY || requestDTO.getTrangThai() == TrangThaiPhieuDatBan.DA_GOP_BAN) {
                 for (ChiTietPhieuDatBan oldCt : pdb.getChiTietPhieuDatBans()) {
                     oldCt.getBanAn().setTrangThai(com.QuanLyDatBanNhaHang.demo.enums.TrangThaiBanAn.TRONG);
                     banAnRepository.save(oldCt.getBanAn());
                 }
             } else if (requestDTO.getTrangThai() == TrangThaiPhieuDatBan.DANG_PHUC_VU) {
                 for (ChiTietPhieuDatBan oldCt : pdb.getChiTietPhieuDatBans()) {
                     oldCt.getBanAn().setTrangThai(com.QuanLyDatBanNhaHang.demo.enums.TrangThaiBanAn.DANG_SUDUNG);
                     banAnRepository.save(oldCt.getBanAn());
                 }
             }
        }

        
        if (requestDTO.getTrangThai() == TrangThaiPhieuDatBan.DANG_PHUC_VU) {
            boolean hasOpenInvoice = hoaDonRepository.findAll().stream()
                    .anyMatch(hd -> hd.getPhieuDatBan() != null && hd.getPhieuDatBan().getId().equals(pdb.getId()) && hd.getTrangThaiThanhToan() == com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThanhToanHoaDon.CHUA_THANH_TOAN);
            if (!hasOpenInvoice) {
                NhanVien nv = pdb.getNhanVien();
                if (nv == null) {
                    nv = nhanVienRepository.findAll().stream().findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên"));
                }
                Thue thueMacDinh = thueRepository.findAll().stream().findFirst().orElseThrow(() -> new ResourceNotFoundException("Chưa cấu hình Thuế trong hệ thống"));
                HoaDon newInvoice = HoaDon.builder()
                        .maHD("HD_" + System.currentTimeMillis())
                        .phieuDatBan(pdb)
                        .nhanVien(nv)
                        .thue(thueMacDinh)
                        .ngayTao(java.time.LocalDateTime.now())
                        .gioVao(java.time.LocalTime.now())
                        .trangThaiThanhToan(com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThanhToanHoaDon.CHUA_THANH_TOAN)
                        .thueSuat(thueMacDinh.getThueSuat())
                        .tienThue(java.math.BigDecimal.ZERO)
                        .tienPhiDV(java.math.BigDecimal.ZERO)
                        .tongTienGoc(java.math.BigDecimal.ZERO)
                        .tienGiamGia(java.math.BigDecimal.ZERO)
                        .tongThanhToan(java.math.BigDecimal.ZERO)
                        .tyLePhiDV(java.math.BigDecimal.ZERO)
                        .build();
                hoaDonRepository.save(newInvoice);
            }
        }
        return convertToResponseDTO(phieuDatBanRepository.save(pdb));

    }

    @Override
    public boolean checkTableAvailability(String maBan, java.time.LocalDateTime thoiGianDen, Long excludePhieuId) {
        java.time.LocalDateTime start = thoiGianDen.minusHours(2);
        java.time.LocalDateTime end = thoiGianDen.plusHours(2);
        return chiTietPhieuDatBanRepository.findConflictingBookings(maBan, start, end, excludePhieuId).isEmpty();
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void deletePhieuDatBan(String maPhieuDat) {
        PhieuDatBan pdb = phieuDatBanRepository.findByMaPhieuDatIgnoreCaseWithRelations(maPhieuDat)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Phiếu đặt với mã: " + maPhieuDat));
        phieuDatBanRepository.delete(pdb);
    }

    private PhieuDatBanResponseDTO convertToResponseDTO(PhieuDatBan pdb) {
        List<ChiTietPhieuDatBanResponseDTO> chiTiets = new ArrayList<>();
        if (pdb.getChiTietPhieuDatBans() != null) {
            chiTiets = pdb.getChiTietPhieuDatBans().stream().map(ct -> ChiTietPhieuDatBanResponseDTO.builder()
                    .id(ct.getId())
                    .maBan(ct.getBanAn() != null ? ct.getBanAn().getMaBan() : null)
                    .viTri(ct.getBanAn() != null ? ct.getBanAn().getViTri() : null)
                    .ghiChu(ct.getGhiChu())
                    .build()).collect(Collectors.toList());
        }

        return PhieuDatBanResponseDTO.builder()
                .id(pdb.getId())
                .maPhieuDat(pdb.getMaPhieuDat())
                .ngayLapPhieu(pdb.getNgayLapPhieu())
                .thoiGianDen(pdb.getThoiGianDen())
                .soLuongNguoi(pdb.getSoLuongNguoi())
                .ghiChu(pdb.getGhiChu())
                .trangThai(pdb.getTrangThai())
                .tienDatCoc(pdb.getTienDatCoc())
                .maKH(pdb.getKhachHang() != null ? pdb.getKhachHang().getMaKH() : null)
                .hoTenKH(pdb.getKhachHang() != null ? pdb.getKhachHang().getHoTen() : null)
                .sdtKH(pdb.getKhachHang() != null ? pdb.getKhachHang().getSdt() : null)
                .maNV(pdb.getNhanVien() != null ? pdb.getNhanVien().getMaNV() : null)
                .hoTenNV(pdb.getNhanVien() != null ? pdb.getNhanVien().getHoTen() : null)
                .chiTiets(chiTiets)
                .build();
    }

    private String generateNextMaPhieuDat() {
        Integer maxMa = phieuDatBanRepository.findMaxMaPhieuDat();
        if (maxMa == null) {
            return String.format("PDB%06d", 1);
        }
        return String.format("PDB%06d", maxMa + 1);
    }
}
