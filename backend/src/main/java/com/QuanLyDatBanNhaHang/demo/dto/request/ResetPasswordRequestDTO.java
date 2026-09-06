package com.QuanLyDatBanNhaHang.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequestDTO {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String username;

    @NotBlank(message = "Số điện thoại hoặc Email không được để trống")
    private String emailOrPhone;
    
    @NotBlank(message = "Mật khẩu mới không được để trống")
    private String newPassword;
}
