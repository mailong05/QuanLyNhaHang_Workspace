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
@Table(name = "BanAn")
public class BanAn extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "maBan", length = 20, unique = true, nullable = false, updatable = false)
    private String maBan;

    @Column(name = "soGhe")
    private Integer soGhe;

    @Nationalized
    @Column(name = "viTri", length = 100)
    private String viTri;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai", nullable = false, length = 50)
    private TrangThaiBanAn trangThai;

    // SỬA Ở ĐÂY
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maKhuVuc")
    private KhuVuc khuVuc;

    @OneToMany(mappedBy = "banAn", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChiTietPhieuDatBan> chiTietPhieuDatBans;
}