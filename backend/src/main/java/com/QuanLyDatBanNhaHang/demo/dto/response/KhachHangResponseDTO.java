package com.QuanLyDatBanNhaHang.demo.dto.response;

import com.QuanLyDatBanNhaHang.demo.enums.LoaiThanhVienKhachHang;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhachHangResponseDTO {
    private Long id;
    private String maKH;
    private String hoTen;
    private String sdt;

    private String email;
    private Integer diemTichLuy;
    private LoaiThanhVienKhachHang loaiThanhVien;
    
    private String username;
    private String quyenHan; // Derived from TaiKhoan
}
