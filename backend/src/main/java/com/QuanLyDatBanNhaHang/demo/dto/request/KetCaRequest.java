package com.QuanLyDatBanNhaHang.demo.dto.request;

import lombok.Data;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;

@Data
public class KetCaRequest {
    @NotNull(message = "Tiền thực tế không được để trống")
    private BigDecimal tienThucTe;
    
    private String ghiChu;
}
