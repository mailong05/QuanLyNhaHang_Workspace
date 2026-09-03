package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.dto.response.PhieuDatBanResponseDTO;
import com.QuanLyDatBanNhaHang.demo.entity.KhachHang;
import com.QuanLyDatBanNhaHang.demo.entity.NhanVien;
import com.QuanLyDatBanNhaHang.demo.entity.PhieuDatBan;
import com.QuanLyDatBanNhaHang.demo.enums.ChucVuNhanVien;
import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiPhieuDatBan;
import com.QuanLyDatBanNhaHang.demo.repository.PhieuDatBanRepository;
import com.QuanLyDatBanNhaHang.demo.service.impl.PhieuDatBanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PhieuDatBanServiceTest {

    @Mock
    private PhieuDatBanRepository phieuDatBanRepository;

    @InjectMocks
    private PhieuDatBanServiceImpl phieuDatBanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test get all")
    void testGetAllPhieuDatBan() {
        KhachHang khach = KhachHang.builder()
                .maKH("KH001")
                .hoTen("Nguyen Van A")
                .sdt("0901234567")
                .loaiThanhVien(com.QuanLyDatBanNhaHang.demo.enums.LoaiThanhVienKhachHang.VANG)
                .build();

        NhanVien nhanVien = NhanVien.builder()
                .maNV("NV001")
                .hoTen("Tran Thi B")
                .chucVu(ChucVuNhanVien.PHUC_VU)
                .build();

        PhieuDatBan phieu1 = PhieuDatBan.builder()
                .maPhieuDat("PDB001")
                .thoiGianDen(LocalDateTime.now())
                .soLuongNguoi(4)
                .trangThai(TrangThaiPhieuDatBan.CHO_XAC_NHAN)
                .tienDatCoc(BigDecimal.valueOf(200000))
                .khachHang(khach)
                .nhanVien(nhanVien)
                .build();

        PhieuDatBan phieu2 = PhieuDatBan.builder()
                .maPhieuDat("PDB002")
                .thoiGianDen(LocalDateTime.now().plusHours(2))
                .soLuongNguoi(6)
                .trangThai(TrangThaiPhieuDatBan.DANG_PHUC_VU)
                .tienDatCoc(BigDecimal.valueOf(300000))
                .khachHang(khach)
                .nhanVien(nhanVien)
                .build();

        when(phieuDatBanRepository.findAllWithRelations(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(phieu1, phieu2)));

        Page<PhieuDatBanResponseDTO> result = phieuDatBanService.getAllPhieuDatBan(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(2, result.getContent().size());

        PhieuDatBanResponseDTO dto1 = result.getContent().get(0);
        assertEquals("PDB001", dto1.getMaPhieuDat());
        assertEquals("Nguyen Van A", dto1.getHoTenKH());
        assertEquals("Tran Thi B", dto1.getHoTenNV());

        verify(phieuDatBanRepository, times(1)).findAllWithRelations(any(Pageable.class));
    }

    @Test
    @DisplayName("Test get by id")
    void testGetPhieuDatBanById() {
        KhachHang khach = KhachHang.builder()
                .maKH("KH001")
                .hoTen("Nguyen Van A")
                .sdt("0901234567")
                .build();

        NhanVien nhanVien = NhanVien.builder()
                .maNV("NV001")
                .hoTen("Tran Thi B")
                .build();

        PhieuDatBan phieu = PhieuDatBan.builder()
                .maPhieuDat("PDB001")
                .thoiGianDen(LocalDateTime.now())
                .soLuongNguoi(4)
                .trangThai(TrangThaiPhieuDatBan.CHO_XAC_NHAN)
                .khachHang(khach)
                .nhanVien(nhanVien)
                .build();

        when(phieuDatBanRepository.findByMaPhieuDatIgnoreCaseWithRelations("PDB001"))
                .thenReturn(Optional.of(phieu));

        PhieuDatBanResponseDTO result = phieuDatBanService.getPhieuDatBanByMa("PDB001");

        assertNotNull(result);
        assertEquals("PDB001", result.getMaPhieuDat());
        assertEquals("Nguyen Van A", result.getHoTenKH());

        verify(phieuDatBanRepository, times(1)).findByMaPhieuDatIgnoreCaseWithRelations("PDB001");
    }
}
