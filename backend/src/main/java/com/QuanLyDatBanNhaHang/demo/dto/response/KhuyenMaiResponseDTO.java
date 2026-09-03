package com.QuanLyDatBanNhaHang.demo.dto.response;

import java.math.BigDecimal;
import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiKhuyenMai;
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
public class KhuyenMaiResponseDTO {
    private Long id;
    private String maKM;
    private String tenKM;
    private BigDecimal giaTriGiam;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private BigDecimal dieuKienToiThieu;
    private TrangThaiKhuyenMai trangThai;
}
