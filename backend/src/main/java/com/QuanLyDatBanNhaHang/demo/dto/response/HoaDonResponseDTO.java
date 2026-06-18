package com.QuanLyDatBanNhaHang.demo.dto.response;

import com.QuanLyDatBanNhaHang.demo.enums.PhuongThucThanhToanHoaDon;
import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThanhToanHoaDon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HoaDonResponseDTO {
    private Long id;
    private String maHD;
    private Double thueSuat;
    private Double tienThue;
    private Double tyLePhiDV;
    private Double tienPhiDV;
    private LocalDateTime ngayTao;
    private LocalTime gioVao;
    private LocalTime gioRa;
    private Double tongTienGoc;
    private Double tienGiamGia;
    private Double tongThanhToan;
    private PhuongThucThanhToanHoaDon phuongThucTT;
    private TrangThaiThanhToanHoaDon trangThaiThanhToan;
    
    private String maPhieuDat;
    private String maNV;
    private String hoTenNV;
    private String maKM;
    private String maThue;

    private List<ChiTietHoaDonResponseDTO> chiTiets;
}
