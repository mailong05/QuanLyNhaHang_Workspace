package com.QuanLyDatBanNhaHang.demo.dto.request;

import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiGiaoCa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiaoCaUpdateRequestDTO {
    
    @NotBlank(message = "Mã nhân viên không được để trống")
    private String maNV;

    @NotBlank(message = "Mã ca không được để trống")
    private String maCa;

    @NotNull(message = "Thời gian vào ca không được để trống")
    private LocalDateTime thoiGianVaoCa;

    private LocalDateTime thoiGianKetCa;

    @NotNull(message = "Tiền ban đầu không được để trống")
    private Double tienBanDau;

    private Double tienKetCa;
    private Double tienHeThong;
    private String ghiChu;

    @NotNull(message = "Trạng thái không được để trống")
    private TrangThaiGiaoCa trangThai;
}
