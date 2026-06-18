package com.QuanLyDatBanNhaHang.demo.repository;

import com.QuanLyDatBanNhaHang.demo.entity.KhuVuc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KhuVucRepository extends JpaRepository<KhuVuc, Long> {
    Optional<KhuVuc> findByMaKhuVucIgnoreCase(String maKhuVuc);
    Page<KhuVuc> findByTenKhuVucContainingIgnoreCase(String tenKhuVuc, Pageable pageable);

    
    @Query("SELECT MAX(CAST(SUBSTRING(kv.maKhuVuc, 3, 6) AS int)) FROM KhuVuc kv")
    Integer findMaxMaKhuVuc();
}
