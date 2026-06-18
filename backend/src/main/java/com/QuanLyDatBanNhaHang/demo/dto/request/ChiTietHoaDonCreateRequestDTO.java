package com.QuanLyDatBanNhaHang.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ChiTietHoaDonCreateRequestDTO {
    private String maHD;

    @NotBlank(message = "Mã món không được để trống")
    private String maMon;

    @NotNull(message = "Số lượng không được để trống")
    private Integer soLuong;

    private String ghiChu;
}
