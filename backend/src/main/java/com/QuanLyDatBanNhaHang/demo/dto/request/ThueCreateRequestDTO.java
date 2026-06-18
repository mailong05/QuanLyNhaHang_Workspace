package com.QuanLyDatBanNhaHang.demo.dto.request;

import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class ThueCreateRequestDTO {

    @NotBlank(message = "Tên thuế không được để trống")
    private String tenThue;

    @NotNull(message = "Thuế suất không được để trống")
    private Double thueSuat;

    @NotNull(message = "Trạng thái không được để trống")
    private TrangThaiThue trangThai;
}
