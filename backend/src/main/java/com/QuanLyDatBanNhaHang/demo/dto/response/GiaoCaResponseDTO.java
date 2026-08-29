package com.QuanLyDatBanNhaHang.demo.dto.response;

import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiGiaoCa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiaoCaResponseDTO {
    private Long id;
    private String maNV;
    private String hoTenNV;
    private String maCa;
    private String tenCa;
    private LocalDateTime thoiGianVaoCa;
    private LocalDateTime thoiGianKetCa;
    private BigDecimal tienBanDau;
    private BigDecimal tienKetCa;
    private BigDecimal tienHeThong;
    private String ghiChu;
    private TrangThaiGiaoCa trangThai;
}
