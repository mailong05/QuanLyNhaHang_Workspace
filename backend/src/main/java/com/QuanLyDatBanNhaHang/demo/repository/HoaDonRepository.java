package com.QuanLyDatBanNhaHang.demo.repository;

import com.QuanLyDatBanNhaHang.demo.entity.HoaDon;
import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThanhToanHoaDon;
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

    @Query(value = "SELECT h FROM HoaDon h LEFT JOIN FETCH h.nhanVien " +
                   "WHERE (:keyword IS NULL OR LOWER(h.maHD) LIKE LOWER(:keyword)) " +
                   "AND (:trangThai IS NULL OR h.trangThaiThanhToan = :trangThai)",
           countQuery = "SELECT COUNT(h) FROM HoaDon h " +
                   "WHERE (:keyword IS NULL OR LOWER(h.maHD) LIKE LOWER(:keyword)) " +
                   "AND (:trangThai IS NULL OR h.trangThaiThanhToan = :trangThai)")
    Page<HoaDon> searchHoaDon(@Param("keyword") String keyword, @Param("trangThai") com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThanhToanHoaDon trangThai, Pageable pageable);
    @Query("SELECT MAX(CAST(SUBSTRING(h.maHD, 3, 6) AS int)) FROM HoaDon h")
    Integer findMaxMaHD();
    @Query("SELECT SUM(h.tongThanhToan) FROM HoaDon h WHERE h.thoiGianThanhToan BETWEEN :start AND :end AND h.trangThaiThanhToan = com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThanhToanHoaDon.DA_THANH_TOAN")
    java.math.BigDecimal sumDoanhThuByDateRange(@org.springframework.data.repository.query.Param("start") java.time.LocalDateTime start, @org.springframework.data.repository.query.Param("end") java.time.LocalDateTime end);

    @Query("SELECT COUNT(h) FROM HoaDon h WHERE h.thoiGianThanhToan BETWEEN :start AND :end AND h.trangThaiThanhToan = com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThanhToanHoaDon.DA_THANH_TOAN")
    Long countDonHangByDateRange(@org.springframework.data.repository.query.Param("start") java.time.LocalDateTime start, @org.springframework.data.repository.query.Param("end") java.time.LocalDateTime end);

    @Query("SELECT SUM(h.tongThanhToan) FROM HoaDon h WHERE h.thoiGianThanhToan BETWEEN :start AND :end AND h.trangThaiThanhToan = com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThanhToanHoaDon.DA_THANH_TOAN AND h.phuongThucTT = com.QuanLyDatBanNhaHang.demo.enums.PhuongThucThanhToanHoaDon.TIEN_MAT")
    java.math.BigDecimal sumTienMatByDateRange(@org.springframework.data.repository.query.Param("start") java.time.LocalDateTime start, @org.springframework.data.repository.query.Param("end") java.time.LocalDateTime end);

    @Query("SELECT h FROM HoaDon h WHERE h.trangThaiThanhToan = com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThanhToanHoaDon.DA_THANH_TOAN ORDER BY h.thoiGianThanhToan DESC")
    java.util.List<HoaDon> findRecentTransactions(Pageable pageable);

}
