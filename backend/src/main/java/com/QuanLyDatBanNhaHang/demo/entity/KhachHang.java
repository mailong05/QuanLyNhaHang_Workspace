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

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "KhachHang")
public class KhachHang extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "maKH", length = 20, unique = true, nullable = false, updatable = false)
    private String maKH;

    @Nationalized
    @Column(name = "hoTen", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "sdt", nullable = false, length = 15)
    private String sdt;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "diemTichLuy")
    private Integer diemTichLuy;

    @Enumerated(EnumType.STRING)
    @Column(name = "loaiThanhVien", nullable = false, length = 50)
    private LoaiThanhVienKhachHang loaiThanhVien;

    @OneToMany(mappedBy = "khachHang", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhieuDatBan> phieuDatBans;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taiKhoanId")
    private TaiKhoan taiKhoan;
}