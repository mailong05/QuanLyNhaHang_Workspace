package com.QuanLyDatBanNhaHang.demo.dto.request;

import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiPhanCong;
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
public class PhanCongCreateRequestDTO {
    
    @NotBlank(message = "Mã ca không được để trống")
    private String maCa;

    @NotBlank(message = "Mã nhân viên không được để trống")
    private String maNV;

    @NotNull(message = "Ngày làm không được để trống")
    private LocalDate ngayLam;

    @NotNull(message = "Trạng thái không được để trống")
    private TrangThaiPhanCong trangThai;
}
