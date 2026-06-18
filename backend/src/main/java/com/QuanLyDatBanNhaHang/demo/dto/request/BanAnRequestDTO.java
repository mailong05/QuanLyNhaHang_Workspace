package com.QuanLyDatBanNhaHang.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO request cho tạo/cập nhật bàn ăn.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BanAnRequestDTO {

    private String maBan;

    private Integer soGhe;

    private String viTri;

    private String trangThai;

    private String maKhuVuc;
}

