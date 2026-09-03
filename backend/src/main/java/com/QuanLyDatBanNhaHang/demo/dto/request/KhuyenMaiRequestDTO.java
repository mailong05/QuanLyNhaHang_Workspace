package com.QuanLyDatBanNhaHang.demo.dto.request;

import java.math.BigDecimal;
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
public class KhuyenMaiRequestDTO {
    private String maKM;
    private String tenKM;
    private BigDecimal giaTriGiam;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private BigDecimal dieuKienToiThieu;
    private String trangThai;
}

