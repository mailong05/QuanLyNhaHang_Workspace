package com.QuanLyDatBanNhaHang.demo.dto.request;

import com.QuanLyDatBanNhaHang.demo.enums.PhuongThucThanhToanHoaDon;
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
public class PosThanhToanRequestDTO {
    @NotNull(message = "Phương thức thanh toán không được để trống")
    private PhuongThucThanhToanHoaDon phuongThucTT;
}
