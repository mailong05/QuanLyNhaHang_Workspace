package com.QuanLyDatBanNhaHang.demo.dto.request;

import java.math.BigDecimal;
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
public class ThueRequestDTO {
    private String maThue;
    private String tenThue;
    private BigDecimal thueSuat;
    private String trangThai;
}

