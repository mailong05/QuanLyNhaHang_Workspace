package com.QuanLyDatBanNhaHang.demo.dto.response;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
@Data
@Builder
public class DashboardOverviewDTO {
    private BigDecimal doanhThuHomNay;
    private Long soDonHoanTat;
    private Long banDangPhucVu;
}
