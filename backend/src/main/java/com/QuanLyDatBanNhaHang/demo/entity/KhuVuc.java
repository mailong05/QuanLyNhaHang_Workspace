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
@Table(name = "KhuVuc")
public class KhuVuc extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "maKhuVuc", length = 20, unique = true, nullable = false, updatable = false)
    private String maKhuVuc;

    @Nationalized
    @Column(name = "tenKhuVuc", nullable = false, length = 100)
    private String tenKhuVuc;

    @OneToMany(mappedBy = "khuVuc", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BanAn> banAns;
}

