package com.QuanLyDatBanNhaHang.demo.dto.request;

import java.math.BigDecimal;
import com.QuanLyDatBanNhaHang.demo.enums.PhuongThucThanhToanHoaDon;
import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThanhToanHoaDon;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HoaDonUpdateRequestDTO {
    
    private BigDecimal thueSuat;
    private BigDecimal tienThue;
    private BigDecimal tyLePhiDV;
    private BigDecimal tienPhiDV;
    private BigDecimal tongTienGoc;
    private BigDecimal tienGiamGia;
    private BigDecimal tongThanhToan;
    
    private PhuongThucThanhToanHoaDon phuongThucTT;
    
    @NotNull(message = "Trạng thái thanh toán không được để trống")
    private TrangThaiThanhToanHoaDon trangThaiThanhToan;

    private String maKM;
    private String maThue;

    @Valid
    private List<ChiTietHoaDonCreateRequestDTO> chiTiets;
}
