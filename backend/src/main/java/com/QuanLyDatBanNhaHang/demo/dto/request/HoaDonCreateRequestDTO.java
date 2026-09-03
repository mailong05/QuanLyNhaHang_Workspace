package com.QuanLyDatBanNhaHang.demo.dto.request;

import java.math.BigDecimal;
import com.QuanLyDatBanNhaHang.demo.enums.PhuongThucThanhToanHoaDon;
import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThanhToanHoaDon;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class HoaDonCreateRequestDTO {
    

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

    @NotBlank(message = "Mã phiếu đặt không được để trống")
    private String maPhieuDat;

    @NotBlank(message = "Mã nhân viên không được để trống")
    private String maNV;

    private String maKM;

    @NotBlank(message = "Mã thuế không được để trống")
    private String maThue;

    @Valid
    private List<ChiTietHoaDonCreateRequestDTO> chiTiets;
}
