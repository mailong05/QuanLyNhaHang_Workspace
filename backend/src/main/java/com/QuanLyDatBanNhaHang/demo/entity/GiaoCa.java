package com.QuanLyDatBanNhaHang.demo.entity;

import jakarta.persistence.*;
import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiGiaoCa;
import org.hibernate.annotations.Nationalized;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "GiaoCa")
public class GiaoCa extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNV", nullable = false)
    private NhanVien nhanVien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maCa", nullable = false)
    private CaLamViec caLamViec;

    @Column(name = "thoiGianVaoCa", nullable = false)
    private LocalDateTime thoiGianVaoCa;

    @Column(name = "thoiGianKetCa")
    private LocalDateTime thoiGianKetCa;

    @Column(name = "tienBanDau", nullable = false)
    private BigDecimal tienBanDau;

    @Column(name = "tienKetCa")
    private BigDecimal tienKetCa;

    @Column(name = "tienHeThong")
    private BigDecimal tienHeThong;

    @Nationalized
    @Column(name = "ghiChu", length = 255)
    private String ghiChu;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai", nullable = false, length = 50)
    private TrangThaiGiaoCa trangThai;
}
