package com.QuanLyDatBanNhaHang.demo.dto.response;

import java.math.BigDecimal;
import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThue;
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
public class ThueResponseDTO {
    private Long id;
    private String maThue;
    private String tenThue;
    private BigDecimal thueSuat;
    private TrangThaiThue trangThai;
}
