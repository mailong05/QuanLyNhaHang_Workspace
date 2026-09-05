package com.QuanLyDatBanNhaHang.demo.dto.request;

import java.math.BigDecimal;
import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiMonAn;
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
public class MonAnUpdateRequestDTO {
    @NotBlank(message = "Tên món không được để trống")
    private String tenMon;

    @NotNull(message = "Đơn giá không được để trống")
    private BigDecimal donGia;

    private com.QuanLyDatBanNhaHang.demo.enums.DonViTinhMonAn donViTinh;

    @NotNull(message = "Tên loại không được để trống")
    private com.QuanLyDatBanNhaHang.demo.enums.LoaiMonAn tenLoai;

    @NotNull(message = "Trạng thái không được để trống")
    private TrangThaiMonAn trangThai;

    private String urlHinhAnh;
}
