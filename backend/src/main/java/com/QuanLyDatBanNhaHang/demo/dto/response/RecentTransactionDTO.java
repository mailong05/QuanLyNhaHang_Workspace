package com.QuanLyDatBanNhaHang.demo.dto.response;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Builder
public class RecentTransactionDTO {
    private String maHD;
    private LocalDateTime thoiGianThanhToan;
    private BigDecimal tongTien;
    private String phuongThucThanhToan;
}
