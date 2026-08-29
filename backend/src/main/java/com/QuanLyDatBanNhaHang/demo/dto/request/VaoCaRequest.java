package com.QuanLyDatBanNhaHang.demo.dto.request;

import lombok.Data;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;

@Data
public class VaoCaRequest {
    @NotNull(message = "Tiền ban đầu không được để trống")
    private BigDecimal tienBanDau;
}
