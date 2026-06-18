package com.QuanLyDatBanNhaHang.demo.dto.response;

import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiPhanCong;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhanCongResponseDTO {
    private Long id;
    private String maCa;
    private String tenCa;
    private String maNV;
    private String hoTenNV;
    private LocalDate ngayLam;
    private TrangThaiPhanCong trangThai;
}
