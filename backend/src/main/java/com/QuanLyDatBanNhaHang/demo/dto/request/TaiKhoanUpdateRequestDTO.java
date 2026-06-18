package com.QuanLyDatBanNhaHang.demo.dto.request;

import com.QuanLyDatBanNhaHang.demo.enums.QuyenHanTaiKhoan;
import jakarta.validation.constraints.NotNull;
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
public class TaiKhoanUpdateRequestDTO {
    // Nếu rỗng thì không đổi mật khẩu
    private String password;

    @NotNull(message = "Quyền hạn không được để trống")
    private QuyenHanTaiKhoan quyenHan;
}
