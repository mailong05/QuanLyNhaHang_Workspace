package com.QuanLyDatBanNhaHang.demo.dto.response;

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
public class ChiTietHoaDonResponseDTO {
    private Long id;
    private String maMon;
    private String tenMon;
    private Integer soLuong;
    private BigDecimal donGia;
    private BigDecimal donGiaLuuTru;
    private String ghiChu;
    private BigDecimal thanhTien;
}
