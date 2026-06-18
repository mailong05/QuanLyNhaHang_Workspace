package com.QuanLyDatBanNhaHang.demo.repository;

import com.QuanLyDatBanNhaHang.demo.entity.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {
    
    @Query("SELECT h FROM HoaDon h LEFT JOIN FETCH h.phieuDatBan LEFT JOIN FETCH h.nhanVien LEFT JOIN FETCH h.khuyenMai LEFT JOIN FETCH h.thue LEFT JOIN FETCH h.chiTietHoaDons c LEFT JOIN FETCH c.monAn WHERE LOWER(h.maHD) = LOWER(:maHD)")
    Optional<HoaDon> findByMaHDIgnoreCaseWithRelations(@Param("maHD") String maHD);
    
    @Query(value = "SELECT h FROM HoaDon h LEFT JOIN FETCH h.nhanVien", 
           countQuery = "SELECT COUNT(h) FROM HoaDon h")
    Page<HoaDon> findAllWithRelations(Pageable pageable);
    @Query("SELECT MAX(CAST(SUBSTRING(h.maHD, 3, 6) AS int)) FROM HoaDon h")
    Integer findMaxMaHD();
}
