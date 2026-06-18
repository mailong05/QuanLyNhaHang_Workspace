package com.QuanLyDatBanNhaHang.demo.dto.response;

import com.QuanLyDatBanNhaHang.demo.enums.ChucVuNhanVien;
import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiNhanVien;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhanVienResponseDTO {
    private Long id;
    private String maNV;
    private String hoTen;
    private String sdt;

    private String email;
    private ChucVuNhanVien chucVu;
    private LocalDate ngayVaoLam;
    private Double luongCoBan;
    private TrangThaiNhanVien trangThai;
    
    private String username;
    private String quyenHan; // Derived from TaiKhoan
}
