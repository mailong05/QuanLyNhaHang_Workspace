package com.QuanLyDatBanNhaHang.demo.dto.request;

import com.QuanLyDatBanNhaHang.demo.enums.LoaiThanhVienKhachHang;
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
public class KhachHangCreateRequestDTO {
    
    

    @NotBlank(message = "Họ tên không được để trống")
    private String hoTen;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String sdt;

    private String email;

    @NotNull(message = "Loại thành viên không được để trống")
    private LoaiThanhVienKhachHang loaiThanhVien;

    private String username; // Optional (cho luồng tự đặt web cần map tk, hoặc guest để null)
}
