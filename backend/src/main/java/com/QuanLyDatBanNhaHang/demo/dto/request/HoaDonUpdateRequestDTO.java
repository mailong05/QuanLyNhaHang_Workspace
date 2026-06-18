package com.QuanLyDatBanNhaHang.demo.dto.request;

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
    
    private Double thueSuat;
    private Double tienThue;
    private Double tyLePhiDV;
    private Double tienPhiDV;
    private Double tongTienGoc;
    private Double tienGiamGia;
    private Double tongThanhToan;
    
    private PhuongThucThanhToanHoaDon phuongThucTT;
    
    @NotNull(message = "Trạng thái thanh toán không được để trống")
    private TrangThaiThanhToanHoaDon trangThaiThanhToan;

    private String maKM;
    private String maThue;

    @Valid
    private List<ChiTietHoaDonCreateRequestDTO> chiTiets;
}
