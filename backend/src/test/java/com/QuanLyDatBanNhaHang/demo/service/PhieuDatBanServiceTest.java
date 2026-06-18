package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.dto.response.PhieuDatBanDTO;
import com.QuanLyDatBanNhaHang.demo.entity.KhachHang;
import com.QuanLyDatBanNhaHang.demo.entity.NhanVien;
import com.QuanLyDatBanNhaHang.demo.entity.PhieuDatBan;
import com.QuanLyDatBanNhaHang.demo.repository.PhieuDatBanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit Test cho PhieuDatBanService.
 *
 * Mục đích:
 * - Demo luồng: Repository (JOIN FETCH) -> Service (chuyển Entity->DTO) -> DTO an toàn
 * - Kiểm tra DTO không chứa object liên quan (tránh recursion khi JSON serialize)
 * - Kiểm tra repository chỉ được qury 1 lần (tránh N+1)
 *
 * Cách chạy: mvn test hoặc right-click chạy trong IDE
 */
@DisplayName("PhieuDatBanService - Unit Test")
public class PhieuDatBanServiceTest {

    @Mock
    private PhieuDatBanRepository phieuDatBanRepository;

    @InjectMocks
    private PhieuDatBanService phieuDatBanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test 1: getAllPhieuDatBan() - Lấy tất cả phiếu, kiểm tra:
     * - Repository query được gọi đúng 1 lần (JOIN FETCH tránh N+1)
     * - DTO được tạo đúng
     * - DTO không chứa collection (tránh StackOverflow khi serialize JSON)
     */
    @Test
    @DisplayName("Lấy tất cả phiếu - kết quả trả DTO an toàn (không có recursion)")
    void testGetAllPhieuDatBan() {
        // Arrange: tạo dữ liệu mock
        KhachHang khach = KhachHang.builder()
                .maKH("KH001")
                .hoTen("Nguyễn Văn A")
                .sdt("0901234567")
                .loaiThanhVien("VIP")
                .build();

        NhanVien nhanVien = NhanVien.builder()
                .maNV("NV001")
                .hoTen("Trần Thị B")
                .chucVu("PHUC_VU")
                .build();

        PhieuDatBan phieu1 = PhieuDatBan.builder()
                .maPhieuDat("PDB001")
                .thoiGianDen(LocalDateTime.now())
                .soLuongNguoi(4)
                .trangThai("DANG_CHO")
                .tienDatCoc(200000.0)
                .khachHang(khach)
                .nhanVien(nhanVien)
                .build();

        PhieuDatBan phieu2 = PhieuDatBan.builder()
                .maPhieuDat("PDB002")
                .thoiGianDen(LocalDateTime.now().plusHours(2))
                .soLuongNguoi(6)
                .trangThai("DANG_SU_DUNG")
                .tienDatCoc(300000.0)
                .khachHang(khach)
                .nhanVien(nhanVien)
                .build();

        // Mock repository để trả 2 phiếu ngay (simulate JOIN FETCH result)
        when(phieuDatBanRepository.findAllWithRelations())
                .thenReturn(Arrays.asList(phieu1, phieu2));

        // Act: gọi service
        List<PhieuDatBanDTO> result = phieuDatBanService.getAllPhieuDatBan();

        // Assert
        assertNotNull(result, "Result không được null");
        assertEquals(2, result.size(), "Kết quả phải có 2 phiếu");

        // Kiểm tra DTO 1
        PhieuDatBanDTO dto1 = result.get(0);
        assertEquals("PDB001", dto1.getMaPhieuDat(), "Mã phiếu phải đúng");
        assertEquals("Nguyễn Văn A", dto1.getTenKH(), "Tên khách phải đúng");
        assertEquals("Trần Thị B", dto1.getHoTenNV(), "Tên nhân viên phải đúng");

        // Quan trọng: kiểm tra DTO KHÔNG chứa object nested (tránh recursion JSON)
        // DTO chỉ có scalar fields (String, Integer, Double, LocalDateTime) - không có KhachHang/NhanVien object
        assertFalse(dto1.getClass().getDeclaredFields()[0].getType().equals(KhachHang.class),
                "DTO không nên có field thuộc loại Entity (tránh recursion)");

        // Kiểm tra repository được gọi đúng 1 lần (JOIN FETCH, không N+1)
        verify(phieuDatBanRepository, times(1)).findAllWithRelations();
    }

    /**
     * Test 2: getPhieuDatBanById() - Lấy 1 phiếu theo ID
     */
    @Test
    @DisplayName("Lấy phiếu theo ID - trả DTO an toàn")
    void testGetPhieuDatBanById() {
        // Arrange
        KhachHang khach = KhachHang.builder()
                .maKH("KH001")
                .hoTen("Nguyễn Văn A")
                .sdt("0901234567")
                .build();

        NhanVien nhanVien = NhanVien.builder()
                .maNV("NV001")
                .hoTen("Trần Thị B")
                .build();

        PhieuDatBan phieu = PhieuDatBan.builder()
                .maPhieuDat("PDB001")
                .thoiGianDen(LocalDateTime.now())
                .soLuongNguoi(4)
                .trangThai("DANG_CHO")
                .khachHang(khach)
                .nhanVien(nhanVien)
                .build();

        when(phieuDatBanRepository.findByIdWithRelations("PDB001"))
                .thenReturn(Optional.of(phieu));

        // Act
        Optional<PhieuDatBanDTO> result = phieuDatBanService.getPhieuDatBanById("PDB001");

        // Assert
        assertTrue(result.isPresent(), "Phiếu phải tồn tại");
        assertEquals("PDB001", result.get().getMaPhieuDat());
        assertEquals("Nguyễn Văn A", result.get().getTenKH());

        verify(phieuDatBanRepository, times(1)).findByIdWithRelations("PDB001");
    }

    /**
     * Test 3: Kiểm tra N+1 query KHÔNG xảy ra
     *
     * Cách thức:
     * - Nếu dùng JOIN FETCH: repository chỉ query 1 lần (tất cả dữ liệu trong 1 result set)
     * - Nếu không dùng JOIN FETCH: query sẽ chạy lần lượt (1 + N)
     *
     * Mock repository simulate câu query với JOIN FETCH success.
     */
    @Test
    @DisplayName("JOIN FETCH tránh N+1 query - repository chỉ gọi 1 lần")
    void testNoN1Query() {
        // Arrange
        PhieuDatBan phieu = PhieuDatBan.builder()
                .maPhieuDat("PDB001")
                .khachHang(KhachHang.builder().maKH("KH001").hoTen("Khách A").build())
                .nhanVien(NhanVien.builder().maNV("NV001").hoTen("NV A").build())
                .build();

        when(phieuDatBanRepository.findAllWithRelations())
                .thenReturn(Arrays.asList(phieu));

        // Act
        phieuDatBanService.getAllPhieuDatBan();

        // Assert: repository được gọi đúng 1 lần (không N+1)
        verify(phieuDatBanRepository, times(1)).findAllWithRelations();
        verifyNoMoreInteractions(phieuDatBanRepository);
    }

    /**
     * Test 4: DTO chứa thông tin cần thiết, không expose toàn bộ entity
     */
    @Test
    @DisplayName("DTO chỉ chứa fields cần thiết (an toàn dữ liệu)")
    void testDTOSafeData() {
        // Arrange
        KhachHang khach = KhachHang.builder()
                .maKH("KH001")
                .hoTen("Khách A")
                .sdt("0901234567")
                .diemTichLuy(1000)  // Field nhạy cảm, không nên trả client
                .loaiThanhVien("VIP")  // Field nhạy cảm
                .build();

        PhieuDatBan phieu = PhieuDatBan.builder()
                .maPhieuDat("PDB001")
                .thoiGianDen(LocalDateTime.now())
                .khachHang(khach)
                .nhanVien(NhanVien.builder().maNV("NV001").hoTen("NV A").build())
                .build();

        when(phieuDatBanRepository.findByIdWithRelations("PDB001"))
                .thenReturn(Optional.of(phieu));

        // Act
        PhieuDatBanDTO dto = phieuDatBanService.getPhieuDatBanById("PDB001").orElse(null);

        // Kiểm tra DTO không có getter cho diemTichLuy (không expose field nhạy cảm từ khách)
        // DTO chỉ có tenKH + sdtKH, không có toàn bộ object KhachHang (tránh expose diemTichLuy)
        assertNotNull(dto);
        assertEquals("Khách A", dto.getTenKH());
        assertEquals("0901234567", dto.getSdtKH());

        // Kiểm tra: DTO không có method getKhachHang() - vì không return toàn bộ entity
        boolean hasKhachHangField = false;
        for (java.lang.reflect.Field f : dto.getClass().getDeclaredFields()) {
            if (f.getType().equals(KhachHang.class) || f.getType().equals(NhanVien.class)) {
                hasKhachHangField = true;
                break;
            }
        }
        assertFalse(hasKhachHangField, "DTO không nên có field thuộc Entity (chỉ scalar)");
    }
}



