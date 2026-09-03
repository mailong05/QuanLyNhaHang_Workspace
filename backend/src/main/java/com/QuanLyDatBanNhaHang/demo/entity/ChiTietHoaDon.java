package com.QuanLyDatBanNhaHang.demo.entity;

import java.math.BigDecimal;
import jakarta.persistence.*;
import com.QuanLyDatBanNhaHang.demo.enums.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Entity
@Table(name = "ChiTietHoaDon")
public class ChiTietHoaDon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "soLuong", nullable = false)
    private Integer soLuong;

    @Column(name = "donGiaLuuTru", nullable = false)
    private BigDecimal donGiaLuuTru;

    @Column(name = "ghiChu", length = 255)
    private String ghiChu;

    @Column(name = "thanhTien", nullable = false)
    private BigDecimal thanhTien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maHD", nullable = false)
    private HoaDon hoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maMon", nullable = false)
    private MonAn monAn;
}

