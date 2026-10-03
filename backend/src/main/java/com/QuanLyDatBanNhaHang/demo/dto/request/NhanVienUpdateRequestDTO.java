package com.QuanLyDatBanNhaHang.demo.dto.request;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import com.QuanLyDatBanNhaHang.demo.enums.ChucVuNhanVien;
import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiNhanVien;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhanVienUpdateRequestDTO {
    
    @NotBlank(message = "Họ tên không được để trống")
    private String hoTen;

    @Pattern(regexp = "^(0[3|5|7|8|9])+([0-9]{8})$", message = "Số điện thoại không hợp lệ")
    private String sdt;

    @Email(message = "Email không hợp lệ")
    private String email;

    @NotNull(message = "Chức vụ không được để trống")
    private ChucVuNhanVien chucVu;

    @NotNull(message = "Ngày vào làm không được để trống")
    private LocalDate ngayVaoLam;

    @NotNull(message = "Lương cơ bản không được để trống")
    @Min(value = 0, message = "Lương cơ bản không được âm")
    private BigDecimal luongCoBan;

    @NotNull(message = "Trạng thái không được để trống")
    private TrangThaiNhanVien trangThai;

    @Pattern(regexp = "^[a-zA-Z0-9_]{4,20}$", message = "Username từ 4-20 ký tự, chỉ chứa chữ, số và dấu gạch dưới")
    private String username;
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password; // Optional
}
