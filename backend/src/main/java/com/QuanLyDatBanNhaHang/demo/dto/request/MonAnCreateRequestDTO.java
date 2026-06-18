package com.QuanLyDatBanNhaHang.demo.dto.request;

import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiMonAn;
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
public class MonAnCreateRequestDTO {

    @NotBlank(message = "Tên món không được để trống")
    private String tenMon;

    @NotNull(message = "Đơn giá không được để trống")
    private Double donGia;

    private String donViTinh;

    @NotBlank(message = "Tên loại không được để trống")
    private String tenLoai;

    @NotNull(message = "Trạng thái không được để trống")
    private TrangThaiMonAn trangThai;

    private String urlHinhAnh;
}
