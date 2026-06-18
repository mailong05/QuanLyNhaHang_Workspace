package com.QuanLyDatBanNhaHang.demo.service.impl;

import com.QuanLyDatBanNhaHang.demo.dto.request.ChiTietPhieuDatBanCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.PhieuDatBanCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.PhieuDatBanUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.ChiTietPhieuDatBanResponseDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.PhieuDatBanResponseDTO;
import com.QuanLyDatBanNhaHang.demo.entity.BanAn;
import com.QuanLyDatBanNhaHang.demo.entity.ChiTietPhieuDatBan;
import com.QuanLyDatBanNhaHang.demo.entity.KhachHang;
import com.QuanLyDatBanNhaHang.demo.entity.NhanVien;
import com.QuanLyDatBanNhaHang.demo.entity.PhieuDatBan;
import com.QuanLyDatBanNhaHang.demo.exception.DuplicateResourceException;
import com.QuanLyDatBanNhaHang.demo.exception.ResourceNotFoundException;
import com.QuanLyDatBanNhaHang.demo.repository.BanAnRepository;
import com.QuanLyDatBanNhaHang.demo.repository.KhachHangRepository;
import com.QuanLyDatBanNhaHang.demo.repository.NhanVienRepository;
import com.QuanLyDatBanNhaHang.demo.repository.PhieuDatBanRepository;
import com.QuanLyDatBanNhaHang.demo.service.PhieuDatBanService;
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

        if (requestDTO.getThoiGianDen().isBefore(LocalDateTime.now().plusMinutes(30))) {
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

        // Xóa chi tiết cũ và map chi tiết mới
        if (requestDTO.getChiTiets() != null) {
            pdb.getChiTietPhieuDatBans().clear();
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
