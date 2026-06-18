package com.QuanLyDatBanNhaHang.demo.dto.request;

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
public class KhachHangRequestDTO {
    private String maKH;
    private String hoTen;
    private String sdt;
    private Integer diemTichLuy;
    private String loaiThanhVien;
}

