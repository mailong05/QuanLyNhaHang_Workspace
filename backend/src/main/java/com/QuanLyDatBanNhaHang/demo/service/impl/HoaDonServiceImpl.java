package com.QuanLyDatBanNhaHang.demo.service.impl;

import com.QuanLyDatBanNhaHang.demo.dto.request.ChiTietHoaDonCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.HoaDonCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.HoaDonUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.ChiTietHoaDonResponseDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.HoaDonResponseDTO;
import com.QuanLyDatBanNhaHang.demo.entity.ChiTietHoaDon;
import com.QuanLyDatBanNhaHang.demo.entity.HoaDon;
import com.QuanLyDatBanNhaHang.demo.entity.KhuyenMai;
import com.QuanLyDatBanNhaHang.demo.entity.MonAn;
import com.QuanLyDatBanNhaHang.demo.entity.NhanVien;
import com.QuanLyDatBanNhaHang.demo.entity.PhieuDatBan;
import com.QuanLyDatBanNhaHang.demo.entity.Thue;
import com.QuanLyDatBanNhaHang.demo.exception.DuplicateResourceException;
import com.QuanLyDatBanNhaHang.demo.exception.ResourceNotFoundException;
import com.QuanLyDatBanNhaHang.demo.repository.HoaDonRepository;
import com.QuanLyDatBanNhaHang.demo.repository.KhuyenMaiRepository;
import com.QuanLyDatBanNhaHang.demo.repository.MonAnRepository;
import com.QuanLyDatBanNhaHang.demo.repository.NhanVienRepository;
import com.QuanLyDatBanNhaHang.demo.repository.PhieuDatBanRepository;
import com.QuanLyDatBanNhaHang.demo.repository.ThueRepository;
import com.QuanLyDatBanNhaHang.demo.service.HoaDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HoaDonServiceImpl implements HoaDonService {

    private final HoaDonRepository hoaDonRepository;
    private final PhieuDatBanRepository phieuDatBanRepository;
    private final NhanVienRepository nhanVienRepository;
    private final KhuyenMaiRepository khuyenMaiRepository;
    private final ThueRepository thueRepository;
    private final MonAnRepository monAnRepository;

    @Override
    public Page<HoaDonResponseDTO> getAllHoaDon(Pageable pageable) {
        return hoaDonRepository.findAllWithRelations(pageable).map(this::convertToResponseDTO);
    }

    @Override
    public HoaDonResponseDTO getHoaDonByMa(String maHD) {
        HoaDon hd = hoaDonRepository.findByMaHDIgnoreCaseWithRelations(maHD)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Hóa đơn với mã: " + maHD));
        return convertToResponseDTO(hd);
    }

    @Override
    @Transactional
    public HoaDonResponseDTO createHoaDon(HoaDonCreateRequestDTO requestDTO) {


        PhieuDatBan pdb = phieuDatBanRepository.findByMaPhieuDatIgnoreCaseWithRelations(requestDTO.getMaPhieuDat())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Phiếu đặt: " + requestDTO.getMaPhieuDat()));
        NhanVien nv = nhanVienRepository.findByMaNVIgnoreCase(requestDTO.getMaNV())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Nhân viên: " + requestDTO.getMaNV()));
        Thue thue = thueRepository.findByMaThueIgnoreCase(requestDTO.getMaThue())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Thuế: " + requestDTO.getMaThue()));
        
        KhuyenMai km = null;
        if (requestDTO.getMaKM() != null && !requestDTO.getMaKM().isBlank()) {
            km = khuyenMaiRepository.findByMaKMIgnoreCase(requestDTO.getMaKM())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Khuyến mãi: " + requestDTO.getMaKM()));
        }

        
        if (km != null) {
            LocalDate ngayTao = LocalDate.now();
            if (ngayTao.isBefore(km.getNgayBatDau()) || ngayTao.isAfter(km.getNgayKetThuc())) {
                throw new IllegalArgumentException("Khuyến mãi không nằm trong thời gian áp dụng.");
            }
            if (requestDTO.getTongTienGoc() < km.getDieuKienToiThieu()) {
                throw new IllegalArgumentException("Chưa đạt điều kiện tối thiểu để áp dụng khuyến mãi.");
            }
        }

        HoaDon hd = HoaDon.builder()
                .maHD(generateNextMaHD())
                .thueSuat(requestDTO.getThueSuat())
                .tienThue(requestDTO.getTienThue())
                .tyLePhiDV(requestDTO.getTyLePhiDV())
                .tienPhiDV(requestDTO.getTienPhiDV())
                .ngayTao(LocalDateTime.now())
                .gioVao(LocalTime.now())
                .tongTienGoc(requestDTO.getTongTienGoc())
                .tienGiamGia(requestDTO.getTienGiamGia())
                .tongThanhToan(requestDTO.getTongThanhToan())
                .phuongThucTT(requestDTO.getPhuongThucTT())
                .trangThaiThanhToan(requestDTO.getTrangThaiThanhToan())
                .phieuDatBan(pdb)
                .nhanVien(nv)
                .thue(thue)
                .khuyenMai(km)
                .chiTietHoaDons(new ArrayList<>())
                .build();

        if (requestDTO.getChiTiets() != null) {
            for (ChiTietHoaDonCreateRequestDTO cReq : requestDTO.getChiTiets()) {
                MonAn ma = monAnRepository.findByMaMonIgnoreCase(cReq.getMaMon())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Món ăn: " + cReq.getMaMon()));
                
                Double thanhTien = ma.getDonGia() * cReq.getSoLuong();
                
                ChiTietHoaDon ct = ChiTietHoaDon.builder()
                        .hoaDon(hd)
                        .monAn(ma)
                        .soLuong(cReq.getSoLuong())
                        .donGiaLuuTru(ma.getDonGia())
                        .thanhTien(thanhTien)
                        .ghiChu(cReq.getGhiChu())
                        .build();
                hd.getChiTietHoaDons().add(ct);
            }
        }

        return convertToResponseDTO(hoaDonRepository.save(hd));
    }

    @Override
    @Transactional
    public HoaDonResponseDTO updateHoaDon(String maHD, HoaDonUpdateRequestDTO requestDTO) {
        HoaDon hd = hoaDonRepository.findByMaHDIgnoreCaseWithRelations(maHD)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Hóa đơn với mã: " + maHD));

        Thue thue = thueRepository.findByMaThueIgnoreCase(requestDTO.getMaThue())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Thuế: " + requestDTO.getMaThue()));
        
        KhuyenMai km = null;
        if (requestDTO.getMaKM() != null && !requestDTO.getMaKM().isBlank()) {
            km = khuyenMaiRepository.findByMaKMIgnoreCase(requestDTO.getMaKM())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Khuyến mãi: " + requestDTO.getMaKM()));
        }

        hd.setThueSuat(requestDTO.getThueSuat());
        hd.setTienThue(requestDTO.getTienThue());
        hd.setTyLePhiDV(requestDTO.getTyLePhiDV());
        hd.setTienPhiDV(requestDTO.getTienPhiDV());
        hd.setTongTienGoc(requestDTO.getTongTienGoc());
        hd.setTienGiamGia(requestDTO.getTienGiamGia());
        hd.setTongThanhToan(requestDTO.getTongThanhToan());
        hd.setPhuongThucTT(requestDTO.getPhuongThucTT());
        hd.setTrangThaiThanhToan(requestDTO.getTrangThaiThanhToan());
        hd.setThue(thue);
        
        if (km != null) {
            LocalDate ngayTao = LocalDate.now();
            if (ngayTao.isBefore(km.getNgayBatDau()) || ngayTao.isAfter(km.getNgayKetThuc())) {
                throw new IllegalArgumentException("Khuyến mãi không nằm trong thời gian áp dụng.");
            }
            if (requestDTO.getTongTienGoc() < km.getDieuKienToiThieu()) {
                throw new IllegalArgumentException("Chưa đạt điều kiện tối thiểu để áp dụng khuyến mãi.");
            }
        }

        hd.setKhuyenMai(km);

        if (requestDTO.getChiTiets() != null) {
            hd.getChiTietHoaDons().clear();
            for (ChiTietHoaDonCreateRequestDTO cReq : requestDTO.getChiTiets()) {
                MonAn ma = monAnRepository.findByMaMonIgnoreCase(cReq.getMaMon())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Món ăn: " + cReq.getMaMon()));
                
                Double thanhTien = ma.getDonGia() * cReq.getSoLuong();
                
                ChiTietHoaDon ct = ChiTietHoaDon.builder()
                        .hoaDon(hd)
                        .monAn(ma)
                        .soLuong(cReq.getSoLuong())
                        .donGiaLuuTru(ma.getDonGia())
                        .thanhTien(thanhTien)
                        .ghiChu(cReq.getGhiChu())
                        .build();
                hd.getChiTietHoaDons().add(ct);
            }
        }

        return convertToResponseDTO(hoaDonRepository.save(hd));
    }

    @Override
    @Transactional
    public void deleteHoaDon(String maHD) {
        HoaDon hd = hoaDonRepository.findByMaHDIgnoreCaseWithRelations(maHD)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Hóa đơn với mã: " + maHD));
        hoaDonRepository.delete(hd);
    }

    private HoaDonResponseDTO convertToResponseDTO(HoaDon hd) {
        List<ChiTietHoaDonResponseDTO> chiTiets = new ArrayList<>();
        if (hd.getChiTietHoaDons() != null) {
            chiTiets = hd.getChiTietHoaDons().stream().map(ct -> ChiTietHoaDonResponseDTO.builder()
                    .id(ct.getId())
                    .maMon(ct.getMonAn() != null ? ct.getMonAn().getMaMon() : null)
                    .tenMon(ct.getMonAn() != null ? ct.getMonAn().getTenMon() : null)
                    .soLuong(ct.getSoLuong())
                    .donGiaLuuTru(ct.getDonGiaLuuTru())
                    .thanhTien(ct.getThanhTien())
                    .ghiChu(ct.getGhiChu())
                    .build()).collect(Collectors.toList());
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

    private String generateNextMaHD() {
        Integer maxMa = hoaDonRepository.findMaxMaHD();
        if (maxMa == null) {
            return String.format("HD%06d", 1);
        }
        return String.format("HD%06d", maxMa + 1);
    }
}
