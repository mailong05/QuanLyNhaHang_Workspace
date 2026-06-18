package com.QuanLyDatBanNhaHang.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

public class KhuVucRequestDTO {

    @NotBlank(message = "Mã khu vực không được để trống hoặc chứa toàn dấu cách")
    @Size(max = 20, message = "Mã khu vực không được vượt quá 20 ký tự")
    @Pattern(regexp = "^KV\\d{3}$", message = "Mã khu vực không hợp lệ. Vui lòng nhập theo định dạng KV + 3 chữ số (Ví dụ: KV001, KV099)")
    private String maKhuVuc;

    @NotBlank(message = "Tên khu vực bắt buộc phải nhập")
    @Size(min = 5, message = "Tên khu vực phải dài ít nhất 5 ký tự")
    private String tenKhuVuc;
}