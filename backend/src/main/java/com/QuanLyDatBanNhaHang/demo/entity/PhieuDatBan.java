package com.QuanLyDatBanNhaHang.demo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;
import com.QuanLyDatBanNhaHang.demo.enums.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Entity
@Table(name = "PhieuDatBan")
public class PhieuDatBan extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "maPhieuDat", length = 20, unique = true, nullable = false, updatable = false)
    private String maPhieuDat;

    @Column(name = "ngayLapPhieu")
    private LocalDateTime ngayLapPhieu;

    @Column(name = "thoiGianDen", nullable = false)
    private LocalDateTime thoiGianDen;

    @Column(name = "soLuongNguoi")
    private Integer soLuongNguoi;

    @Nationalized
    @Column(name = "ghiChu", length = 255)
    private String ghiChu;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai", nullable = false, length = 50)
    private TrangThaiPhieuDatBan trangThai;

    @Column(name = "tienDatCoc")
    private Double tienDatCoc;

    // SỬA Ở ĐÂY: Thêm LAZY và chặn đệ quy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maKH")
    private KhachHang khachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNV")
    private NhanVien nhanVien;

    @OneToMany(mappedBy = "phieuDatBan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChiTietPhieuDatBan> chiTietPhieuDatBans;

    @OneToMany(mappedBy = "phieuDatBan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HoaDon> hoaDons;
}