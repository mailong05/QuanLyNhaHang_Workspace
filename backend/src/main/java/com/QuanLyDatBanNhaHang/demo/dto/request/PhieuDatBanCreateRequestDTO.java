package com.QuanLyDatBanNhaHang.demo.dto.request;

import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiPhieuDatBan;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhieuDatBanCreateRequestDTO {
    

    @NotNull(message = "Thời gian đến không được để trống")
    private LocalDateTime thoiGianDen;

    private Integer soLuongNguoi;
    private String ghiChu;
    
    @NotNull(message = "Trạng thái không được để trống")
    private TrangThaiPhieuDatBan trangThai;

    private Double tienDatCoc;

    @NotBlank(message = "Mã khách hàng không được để trống")
    private String maKH;

    private String maNV; // Nullable for web guest

    @Valid
    private List<ChiTietPhieuDatBanCreateRequestDTO> chiTiets;
}
