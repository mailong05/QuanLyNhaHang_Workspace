package com.QuanLyDatBanNhaHang.demo.repository;

import com.QuanLyDatBanNhaHang.demo.entity.NhanVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, Long> {
    
    @Query("SELECT n FROM NhanVien n LEFT JOIN FETCH n.taiKhoan WHERE LOWER(n.maNV) = LOWER(:maNV)")
    Optional<NhanVien> findByMaNVIgnoreCaseWithAuth(@Param("maNV") String maNV);
    
    Optional<NhanVien> findByMaNVIgnoreCase(String maNV);

    @Query(value = "SELECT n FROM NhanVien n LEFT JOIN FETCH n.taiKhoan", 
           countQuery = "SELECT COUNT(n) FROM NhanVien n")
    Page<NhanVien> findAllWithRelations(Pageable pageable);

    @Query(value = "SELECT n FROM NhanVien n LEFT JOIN FETCH n.taiKhoan " +
                   "WHERE LOWER(n.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                   "OR LOWER(n.sdt) LIKE LOWER(CONCAT('%', :keyword, '%'))",
           countQuery = "SELECT COUNT(n) FROM NhanVien n WHERE LOWER(n.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(n.sdt) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<NhanVien> searchByHoTenOrSdt(@Param("keyword") String keyword, Pageable pageable);
    @Query("SELECT MAX(CAST(SUBSTRING(n.maNV, 3, 6) AS int)) FROM NhanVien n")
    Integer findMaxMaNV();
}
