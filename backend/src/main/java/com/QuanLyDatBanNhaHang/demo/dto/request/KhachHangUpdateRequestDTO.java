package com.QuanLyDatBanNhaHang.demo.dto.request;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import com.QuanLyDatBanNhaHang.demo.enums.LoaiThanhVienKhachHang;
import jakarta.validation.constraints.NotBlank;
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
public class KhachHangUpdateRequestDTO {
    
    @NotBlank(message = "Họ tên không được để trống")
    private String hoTen;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0[3|5|7|8|9])+([0-9]{8})$", message = "Số điện thoại không hợp lệ")
    private String sdt;

    @Email(message = "Email không hợp lệ")
    private String email;

    @NotNull(message = "Loại thành viên không được để trống")
    private LoaiThanhVienKhachHang loaiThanhVien;

    private Integer diemTichLuy;

    @Pattern(regexp = "^[a-zA-Z0-9_]{4,20}$", message = "Username từ 4-20 ký tự, chỉ chứa chữ, số và dấu gạch dưới")
    private String username;
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password; // Optional
}
