package com.QuanLyDatBanNhaHang.demo.dto.request;

import java.math.BigDecimal;
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
public class MonAnRequestDTO {
    private String maMon;
    private String tenMon;
    private BigDecimal donGia;
    private String donViTinh;
    private String tenLoai;
    private String trangThai;
    private String urlHinhAnh;
}

