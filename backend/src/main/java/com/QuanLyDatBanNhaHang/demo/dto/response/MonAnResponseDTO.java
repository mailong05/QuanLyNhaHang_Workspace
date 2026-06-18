package com.QuanLyDatBanNhaHang.demo.dto.response;

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
    private Double donGia;
    private String donViTinh;
    private String tenLoai;
    private TrangThaiMonAn trangThai;
    private String urlHinhAnh;
}
