package com.QuanLyDatBanNhaHang.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PosGopBanRequestDTO {
    @NotBlank
    private String maPhieuDatNguon;
    @NotBlank
    private String maBanDich;
}