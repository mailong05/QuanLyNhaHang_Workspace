package com.QuanLyDatBanNhaHang.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeTableRequestDTO {
    @NotNull
    private Long oldBanId;

    @NotNull
    private Long newBanId;
}
