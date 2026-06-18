package com.QuanLyDatBanNhaHang.demo.dto.response;

import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiPhieuDatBan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhieuDatBanResponseDTO {
    private Long id;
    private String maPhieuDat;
    private LocalDateTime ngayLapPhieu;
    private LocalDateTime thoiGianDen;
    private Integer soLuongNguoi;
    private String ghiChu;
    private TrangThaiPhieuDatBan trangThai;
    private Double tienDatCoc;
    
    private String maKH;
    private String hoTenKH;
    private String maNV;
    private String hoTenNV;

    private List<ChiTietPhieuDatBanResponseDTO> chiTiets;
}
