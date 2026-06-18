package com.QuanLyDatBanNhaHang.demo.dto.response;

import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiBanAn;
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
public class BanAnResponseDTO {
    private Long id;
    private String maBan;
    private Integer soGhe;
    private String viTri;
    private TrangThaiBanAn trangThai;
    private String maKhuVuc;
    private String tenKhuVuc;
}
