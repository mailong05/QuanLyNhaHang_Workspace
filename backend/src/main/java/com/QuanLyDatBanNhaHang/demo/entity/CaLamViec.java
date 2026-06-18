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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "CaLamViec")
public class CaLamViec extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "maCa", length = 20, unique = true, nullable = false, updatable = false)
    private String maCa;

    @Nationalized
    @Column(name = "tenCa", nullable = false, length = 100)
    private String tenCa;

    @Column(name = "gioBatDau", nullable = false)
    private java.time.LocalTime gioBatDau;

    @Column(name = "gioKetThuc", nullable = false)
    private java.time.LocalTime gioKetThuc;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai", nullable = false, length = 50)
    private TrangThaiCaLamViec trangThai;

    @OneToMany(mappedBy = "caLamViec", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<PhanCong> phanCongs;

    @OneToMany(mappedBy = "caLamViec", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<GiaoCa> giaoCas;


}

