package com.QuanLyDatBanNhaHang.demo.dto.request;

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
public class NhanVienRequestDTO {
    private String maNV;
    private String hoTen;
    private String sdt;
    private String chucVu;
    private LocalDate ngayVaoLam;
    private Double luongCoBan;
    private String trangThai;
}

