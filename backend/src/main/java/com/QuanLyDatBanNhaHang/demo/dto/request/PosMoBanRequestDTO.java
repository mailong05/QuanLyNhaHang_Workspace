package com.QuanLyDatBanNhaHang.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PosMoBanRequestDTO {
    @NotNull(message = "ID bàn không được để trống")
    private Long banId;

    // Tuỳ chọn. Nếu frontend không gửi, Backend tự lấy NV đầu tiên
    private String maNV; 
}
