package com.QuanLyDatBanNhaHang.demo.repository;

import com.QuanLyDatBanNhaHang.demo.entity.KhuyenMai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai, Long> {
    Optional<KhuyenMai> findByMaKMIgnoreCase(String maKM);
    @Query("SELECT MAX(CAST(SUBSTRING(km.maKM, 3, 6) AS int)) FROM KhuyenMai km")
    Integer findMaxMaKM();

    boolean existsByMaKM(String maKM);

    @org.springframework.data.jpa.repository.Query(value = "SELECT * FROM KhuyenMai WHERE deleted_at IS NOT NULL", nativeQuery = true)
    java.util.List<KhuyenMai> findAllDeleted();

    @org.springframework.data.jpa.repository.Query(value = "SELECT * FROM KhuyenMai WHERE id = :id AND deleted_at IS NOT NULL", nativeQuery = true)
    java.util.Optional<KhuyenMai> findDeletedById(@org.springframework.data.repository.query.Param("id") Long id);
}