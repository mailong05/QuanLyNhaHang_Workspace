package com.QuanLyDatBanNhaHang.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MergeTableRequestDTO {
    @NotNull
    private Long targetPhieuDatBanId;
}
