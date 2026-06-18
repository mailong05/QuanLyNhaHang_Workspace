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
@Table(name = "Thue")
public class Thue extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "maThue", length = 20, unique = true, nullable = false, updatable = false)
    private String maThue;

    @Nationalized
    @Column(name = "tenThue", nullable = false, length = 100)
    private String tenThue;

    @Column(name = "thueSuat", nullable = false)
    private Double thueSuat;

    @Enumerated(EnumType.STRING)
    @Column(name = "trangThai", nullable = false, length = 50)
    private TrangThaiThue trangThai;

    @OneToMany(mappedBy = "thue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HoaDon> hoaDons;
}

