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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "KhachHang")
@SQLDelete(sql = "UPDATE KhachHang SET deleted_at = CURRENT_TIMESTAMP WHERE id=?")
@SQLRestriction("deleted_at IS NULL")
public class KhachHang extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "maKH", length = 100, unique = true, nullable = false)
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