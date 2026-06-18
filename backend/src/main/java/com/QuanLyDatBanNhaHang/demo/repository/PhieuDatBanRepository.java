package com.QuanLyDatBanNhaHang.demo.repository;

import com.QuanLyDatBanNhaHang.demo.entity.PhieuDatBan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhieuDatBanRepository extends JpaRepository<PhieuDatBan, Long> {
    
    @Query("SELECT p FROM PhieuDatBan p LEFT JOIN FETCH p.khachHang LEFT JOIN FETCH p.nhanVien LEFT JOIN FETCH p.chiTietPhieuDatBans c LEFT JOIN FETCH c.banAn WHERE LOWER(p.maPhieuDat) = LOWER(:maPhieuDat)")
    Optional<PhieuDatBan> findByMaPhieuDatIgnoreCaseWithRelations(@Param("maPhieuDat") String maPhieuDat);
    
    @Query(value = "SELECT p FROM PhieuDatBan p LEFT JOIN FETCH p.khachHang LEFT JOIN FETCH p.nhanVien", 
           countQuery = "SELECT COUNT(p) FROM PhieuDatBan p")
    Page<PhieuDatBan> findAllWithRelations(Pageable pageable);
    @Query("SELECT MAX(CAST(SUBSTRING(p.maPhieuDat, 4, 6) AS int)) FROM PhieuDatBan p")
    Integer findMaxMaPhieuDat();
}
