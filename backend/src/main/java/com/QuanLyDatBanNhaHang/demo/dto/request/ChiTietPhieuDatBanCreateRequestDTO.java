package com.QuanLyDatBanNhaHang.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class ChiTietPhieuDatBanCreateRequestDTO {
    private String maPhieuDat;

    @NotBlank(message = "Mã bàn không được để trống")
    private String maBan;
    private String ghiChu;
}
