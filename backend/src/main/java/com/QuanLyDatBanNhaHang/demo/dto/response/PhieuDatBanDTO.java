package com.QuanLyDatBanNhaHang.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO an toàn cho Phiếu Đặt Bàn - không trả trực tiếp entity để tránh:
 * - Recursion vô hạn khi JSON serialization
 * - N+1 query
 * - Override dữ liệu nhạy cảm
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhieuDatBanDTO {

    private String maPhieuDat;
    private LocalDateTime ngayLapPhieu;
    private LocalDateTime thoiGianDen;
    private Integer soLuongNguoi;
    private String ghiChu;
    private String trangThai;
    private Double tienDatCoc;

    // Thông tin khách hàng (bốc ra từ quan hệ ManyToOne)
    private String maKH;
    private String tenKH;
    private String sdtKH;

    // Thông tin nhân viên (bốc ra từ quan hệ ManyToOne)
    private String maNV;
    private String hoTenNV;
}

