package com.QuanLyDatBanNhaHang.demo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PosThemMonRequestDTO {
    @NotNull(message = "ID bàn không được để trống")
    private Long banId;

    @NotBlank(message = "Mã món không được để trống")
    private String maMon;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer soLuong;
}
