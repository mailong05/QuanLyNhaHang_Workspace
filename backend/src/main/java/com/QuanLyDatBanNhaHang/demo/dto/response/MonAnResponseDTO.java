package com.QuanLyDatBanNhaHang.demo.dto.response;

import java.math.BigDecimal;
import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiMonAn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonAnResponseDTO {
    private Long id;
    private String maMon;
    private String tenMon;
    private BigDecimal donGia;
    private com.QuanLyDatBanNhaHang.demo.enums.DonViTinhMonAn donViTinh;
    private com.QuanLyDatBanNhaHang.demo.enums.LoaiMonAn tenLoai;
    private TrangThaiMonAn trangThai;
    private String urlHinhAnh;
}
