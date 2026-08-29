package com.QuanLyDatBanNhaHang.demo.service.impl;

import com.QuanLyDatBanNhaHang.demo.dto.request.VaoCaRequest;
import com.QuanLyDatBanNhaHang.demo.dto.request.KetCaRequest;
import com.QuanLyDatBanNhaHang.demo.dto.response.GiaoCaResponseDTO;
import com.QuanLyDatBanNhaHang.demo.entity.CaLamViec;
import com.QuanLyDatBanNhaHang.demo.entity.GiaoCa;
import com.QuanLyDatBanNhaHang.demo.entity.NhanVien;
import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiGiaoCa;
import com.QuanLyDatBanNhaHang.demo.exception.ResourceNotFoundException;
import com.QuanLyDatBanNhaHang.demo.repository.CaLamViecRepository;
import com.QuanLyDatBanNhaHang.demo.repository.GiaoCaRepository;
import com.QuanLyDatBanNhaHang.demo.repository.NhanVienRepository;
import com.QuanLyDatBanNhaHang.demo.repository.HoaDonRepository;
import com.QuanLyDatBanNhaHang.demo.service.GiaoCaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GiaoCaServiceImpl implements GiaoCaService {

    private final GiaoCaRepository giaoCaRepository;
    private final NhanVienRepository nhanVienRepository;
    private final CaLamViecRepository caLamViecRepository;
    private final HoaDonRepository hoaDonRepository;

    private NhanVien getCurrentNhanVien() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return nhanVienRepository.findByTaiKhoanUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên đang đăng nhập"));
    }

    private CaLamViec getCaLamViecHienTai() {
        LocalTime now = LocalTime.now();
        List<CaLamViec> cas = caLamViecRepository.findAll();
        for (CaLamViec ca : cas) {
            if (!now.isBefore(ca.getGioBatDau()) && !now.isAfter(ca.getGioKetThuc())) {
                return ca;
            }
        }
        if (!cas.isEmpty()) return cas.get(0);
        throw new ResourceNotFoundException("Không có ca làm việc nào được cấu hình");
    }

    @Override
    public Page<GiaoCaResponseDTO> getAllGiaoCa(Pageable pageable) {
        return giaoCaRepository.findAllWithRelations(pageable).map(this::convertToResponseDTO);
    }

    @Override
    public GiaoCaResponseDTO getGiaoCaById(Long id) {
        GiaoCa g = giaoCaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Giao ca với ID: " + id));
        return convertToResponseDTO(g);
    }

    @Override
    @Transactional
    public GiaoCaResponseDTO vaoCa(VaoCaRequest request) {
        NhanVien nv = getCurrentNhanVien();
        
        giaoCaRepository.findByNhanVienAndTrangThai(nv, TrangThaiGiaoCa.DANG_LAM_VIEC)
                .ifPresent(g -> {
                    throw new IllegalArgumentException("Nhân viên đang có ca làm việc chưa kết thúc");
                });

        CaLamViec ca = getCaLamViecHienTai();

        GiaoCa giaoCa = GiaoCa.builder()
                .nhanVien(nv)
                .caLamViec(ca)
                .thoiGianVaoCa(LocalDateTime.now())
                .tienBanDau(request.getTienBanDau())
                .tienKetCa(BigDecimal.ZERO)
                .tienHeThong(request.getTienBanDau())
                .trangThai(TrangThaiGiaoCa.DANG_LAM_VIEC)
                .build();

        return convertToResponseDTO(giaoCaRepository.save(giaoCa));
    }

    @Override
    public GiaoCaResponseDTO getGiaoCaHienTai() {
        NhanVien nv = getCurrentNhanVien();
        GiaoCa giaoCa = giaoCaRepository.findByNhanVienAndTrangThai(nv, TrangThaiGiaoCa.DANG_LAM_VIEC)
                .orElseThrow(() -> new ResourceNotFoundException("Không có ca làm việc nào đang mở"));

        BigDecimal tongTienMat = hoaDonRepository.sumTienMatByDateRange(giaoCa.getThoiGianVaoCa(), LocalDateTime.now());
        if (tongTienMat == null) tongTienMat = BigDecimal.ZERO;

        giaoCa.setTienHeThong(giaoCa.getTienBanDau().add(tongTienMat));
        return convertToResponseDTO(giaoCa);
    }

    @Override
    @Transactional
    public GiaoCaResponseDTO ketCa(KetCaRequest request) {
        NhanVien nv = getCurrentNhanVien();
        GiaoCa giaoCa = giaoCaRepository.findByNhanVienAndTrangThai(nv, TrangThaiGiaoCa.DANG_LAM_VIEC)
                .orElseThrow(() -> new ResourceNotFoundException("Không có ca làm việc nào đang mở"));

        BigDecimal tongTienMat = hoaDonRepository.sumTienMatByDateRange(giaoCa.getThoiGianVaoCa(), LocalDateTime.now());
        if (tongTienMat == null) tongTienMat = BigDecimal.ZERO;

        giaoCa.setTienHeThong(giaoCa.getTienBanDau().add(tongTienMat));
        giaoCa.setTienKetCa(request.getTienThucTe());
        giaoCa.setGhiChu(request.getGhiChu());
        giaoCa.setThoiGianKetCa(LocalDateTime.now());
        giaoCa.setTrangThai(TrangThaiGiaoCa.DA_KET_CA);

        return convertToResponseDTO(giaoCaRepository.save(giaoCa));
    }

    private GiaoCaResponseDTO convertToResponseDTO(GiaoCa giaoCa) {
        return GiaoCaResponseDTO.builder()
                .id(giaoCa.getId())
                .maNV(giaoCa.getNhanVien() != null ? giaoCa.getNhanVien().getMaNV() : null)
                .hoTenNV(giaoCa.getNhanVien() != null ? giaoCa.getNhanVien().getHoTen() : null)
                .maCa(giaoCa.getCaLamViec() != null ? giaoCa.getCaLamViec().getMaCa() : null)
                .tenCa(giaoCa.getCaLamViec() != null ? giaoCa.getCaLamViec().getTenCa() : null)
                .thoiGianVaoCa(giaoCa.getThoiGianVaoCa())
                .thoiGianKetCa(giaoCa.getThoiGianKetCa())
                .tienBanDau(giaoCa.getTienBanDau())
                .tienKetCa(giaoCa.getTienKetCa())
                .tienHeThong(giaoCa.getTienHeThong())
                .ghiChu(giaoCa.getGhiChu())
                .trangThai(giaoCa.getTrangThai())
                .build();
    }
}
