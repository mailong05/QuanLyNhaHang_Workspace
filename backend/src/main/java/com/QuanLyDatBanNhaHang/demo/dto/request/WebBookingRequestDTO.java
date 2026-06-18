package com.QuanLyDatBanNhaHang.demo.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class WebBookingRequestDTO {
    @NotBlank
    @Size(max = 100)
    private String hoTen;

    @NotBlank
    @Size(max = 15)
    private String sdt;

    @Email
    @Size(max = 100)
    private String email;

    @NotNull
    @Future
    private LocalDateTime thoiGianDen;

    @Min(1)
    private Integer soLuongNguoi;

    private String ghiChu;

    @NotEmpty
    private List<Long> danhSachBanId;
}
