package com.QuanLyDatBanNhaHang.demo.dto.request;

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

    private String sdt;

    private String email;

    @NotNull(message = "Chức vụ không được để trống")
    private ChucVuNhanVien chucVu;

    @NotNull(message = "Ngày vào làm không được để trống")
    private LocalDate ngayVaoLam;

    @NotNull(message = "Lương cơ bản không được để trống")
    private Double luongCoBan;

    @NotNull(message = "Trạng thái không được để trống")
    private TrangThaiNhanVien trangThai;

    private String username; // Optional
}
