package com.QuanLyDatBanNhaHang.demo.dto.request;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaLamViecRequestDTO {
    private String maCa;
    private String maNV;
    private LocalDateTime thoiGianVaoCa;
    private LocalDateTime thoiGianKetCa;
    private BigDecimal tienDauCa;
    private BigDecimal tienKetCa;
    private String trangThai;
    private String ghiChu;
}

