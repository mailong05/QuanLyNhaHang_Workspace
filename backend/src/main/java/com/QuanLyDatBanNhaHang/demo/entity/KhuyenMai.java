package com.QuanLyDatBanNhaHang.demo.entity;

import java.math.BigDecimal;
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

import java.time.LocalDate;
import java.util.List;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "KhuyenMai")
@SQLDelete(sql = "UPDATE KhuyenMai SET deleted_at = CURRENT_TIMESTAMP WHERE id=?")
@SQLRestriction("deleted_at IS NULL")
public class KhuyenMai extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "maKM", length = 100, unique = true, nullable = false)
    private String maKM;

    @Nationalized
    @Column(name = "tenKM", nullable = false, length = 100)
    private String tenKM;

    @Column(name = "giaTriGiam", nullable = false)
    private BigDecimal giaTriGiam;

    @Column(name = "ngayBatDau", nullable = false)
    private LocalDate ngayBatDau;

    @Column(name = "ngayKetThuc", nullable = false)
    private LocalDate ngayKetThuc;

    @Column(name = "dieuKienToiThieu")
    private BigDecimal dieuKienToiThieu;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai", nullable = false, length = 50)
    private TrangThaiKhuyenMai trangThai;

    @OneToMany(mappedBy = "khuyenMai", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HoaDon> hoaDons;
}

