package com.QuanLyDatBanNhaHang.demo.repository;

import com.QuanLyDatBanNhaHang.demo.entity.KhachHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Long> {
    
    @Query("SELECT k FROM KhachHang k LEFT JOIN FETCH k.taiKhoan WHERE LOWER(k.maKH) = LOWER(:maKH)")
    Optional<KhachHang> findByMaKHIgnoreCaseWithAuth(@Param("maKH") String maKH);
    
    Optional<KhachHang> findByMaKHIgnoreCase(String maKH);

    @Query(value = "SELECT k FROM KhachHang k LEFT JOIN FETCH k.taiKhoan", 
           countQuery = "SELECT COUNT(k) FROM KhachHang k")
    Page<KhachHang> findAllWithRelations(Pageable pageable);

    @Query(value = "SELECT k FROM KhachHang k LEFT JOIN FETCH k.taiKhoan " +
                   "WHERE LOWER(k.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                   "OR LOWER(k.sdt) LIKE LOWER(CONCAT('%', :keyword, '%'))",
           countQuery = "SELECT COUNT(k) FROM KhachHang k WHERE LOWER(k.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(k.sdt) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<KhachHang> searchByHoTenOrSdt(@Param("keyword") String keyword, Pageable pageable);
    @Query("SELECT MAX(CAST(SUBSTRING(k.maKH, 3, 6) AS int)) FROM KhachHang k")
    Integer findMaxMaKH();
}
