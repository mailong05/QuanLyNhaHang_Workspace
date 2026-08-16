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
@Table(name = "MonAn")
@SQLDelete(sql = "UPDATE mon_an SET deleted_at = CURRENT_TIMESTAMP WHERE id=?")
@SQLRestriction("deleted_at IS NULL")
public class MonAn extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "maMon", length = 100, unique = true, nullable = false)
    private String maMon;

    @Nationalized
    @Column(name = "tenMon", nullable = false, length = 100)
    private String tenMon;

    @Column(name = "donGia", nullable = false)
    private Double donGia;

    @Nationalized
    @Column(name = "donViTinh", length = 50)
    private String donViTinh;

    @Nationalized
    @Column(name = "tenLoai", nullable = false, length = 50)
    private String tenLoai;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai", nullable = false, length = 50)
    private TrangThaiMonAn trangThai;

    @Column(name = "urlHinhAnh", length = 255)
    private String urlHinhAnh;

    @OneToMany(mappedBy = "monAn", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChiTietHoaDon> chiTietHoaDons;
}

