package com.QuanLyDatBanNhaHang.demo.dto.request;

import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiKhuyenMai;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class KhuyenMaiCreateRequestDTO {
    

    @NotBlank(message = "Tên khuyến mãi không được để trống")
    private String tenKM;

    @NotNull(message = "Giá trị giảm không được để trống")
    private Double giaTriGiam;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDate ngayBatDau;

    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDate ngayKetThuc;

    private Double dieuKienToiThieu;

    @NotNull(message = "Trạng thái không được để trống")
    private TrangThaiKhuyenMai trangThai;
}
