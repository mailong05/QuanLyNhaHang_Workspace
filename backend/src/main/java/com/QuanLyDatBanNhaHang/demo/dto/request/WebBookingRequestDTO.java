package com.QuanLyDatBanNhaHang.demo.dto.request;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

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
    @Pattern(regexp = "^(0[3|5|7|8|9])+([0-9]{8})$", message = "Số điện thoại không hợp lệ")
    private String sdt;

    @Email
    @Size(max = 100)
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotNull
    @Future
    @Future(message = "Thời gian đến phải ở trong tương lai")
    private LocalDateTime thoiGianDen;

    @Min(1)
    @Min(value = 1, message = "Số lượng người phải lớn hơn 0")
    private Integer soLuongNguoi;

    private String ghiChu;

    private List<Long> danhSachBanId;
    
    private java.math.BigDecimal tienDatCoc;
}
