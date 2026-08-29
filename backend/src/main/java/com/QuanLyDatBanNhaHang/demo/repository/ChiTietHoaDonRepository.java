package com.QuanLyDatBanNhaHang.demo.repository;

import com.QuanLyDatBanNhaHang.demo.entity.ChiTietHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@Repository
public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon, Long> {
    @Query("SELECT c FROM ChiTietHoaDon c JOIN FETCH c.hoaDon h JOIN FETCH c.monAn m")
    List<ChiTietHoaDon> findAllWithRelations();
    @Query(value = "SELECT x FROM ChiTietHoaDon x JOIN FETCH x.hoaDon h JOIN FETCH x.monAn m", countQuery = "SELECT count(x) FROM ChiTietHoaDon x")
    Page<ChiTietHoaDon> findAllWithRelations(Pageable pageable);
    @Query("SELECT x FROM ChiTietHoaDon x JOIN FETCH x.hoaDon h JOIN FETCH x.monAn m WHERE x.id = :id")
    Optional<ChiTietHoaDon> findByIdWithRelations(@Param("id") Long id);

    @Query(value = "SELECT m.tenMon as tenMon, SUM(c.soLuong) as soLuong, SUM(c.thanhTien) as doanhThu " +
                   "FROM ChiTietHoaDon c " +
                   "JOIN MonAn m ON c.maMon = m.id " +
                   "JOIN HoaDon h ON c.maHD = h.id " +
                   "WHERE h.thoiGianThanhToan BETWEEN :start AND :end AND h.trangThaiThanhToan = 'DA_THANH_TOAN' " +
                   "GROUP BY m.id, m.tenMon " +
                   "ORDER BY soLuong DESC", nativeQuery = true)
    List<com.QuanLyDatBanNhaHang.demo.dto.projection.TopItemProjection> getTopItems(@Param("start") java.time.LocalDateTime start, @Param("end") java.time.LocalDateTime end);
}
