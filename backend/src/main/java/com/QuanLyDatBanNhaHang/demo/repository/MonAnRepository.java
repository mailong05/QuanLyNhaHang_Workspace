package com.QuanLyDatBanNhaHang.demo.repository;

import com.QuanLyDatBanNhaHang.demo.entity.MonAn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MonAnRepository extends JpaRepository<MonAn, Long> {
    Optional<MonAn> findByMaMonIgnoreCase(String maMon);
    boolean existsByMaMon(String maMon);
    Page<MonAn> findByTenMonContainingIgnoreCase(String tenMon, Pageable pageable);

    @Query(value = "SELECT m FROM MonAn m " +
                   "WHERE (:keyword IS NULL OR LOWER(m.tenMon) LIKE LOWER(:keyword)) " +
                   "AND (:trangThai IS NULL OR m.trangThai = :trangThai)",
           countQuery = "SELECT COUNT(m) FROM MonAn m " +
                   "WHERE (:keyword IS NULL OR LOWER(m.tenMon) LIKE LOWER(:keyword)) " +
                   "AND (:trangThai IS NULL OR m.trangThai = :trangThai)")
    Page<MonAn> searchMonAn(@Param("keyword") String keyword, @Param("trangThai") com.QuanLyDatBanNhaHang.demo.enums.TrangThaiMonAn trangThai, Pageable pageable);

    @Query("SELECT MAX(CAST(SUBSTRING(m.maMon, 3, 6) AS int)) FROM MonAn m")
    Integer findMaxMaMon();

    // --- CÁC HÀM DÀNH CHO THÙNG RÁC (NATIVE SQL) ---

    // 1. Lấy danh sách đã xóa
    @Query(value = "SELECT * FROM MonAn WHERE deleted_at IS NOT NULL", nativeQuery = true)
    java.util.List<MonAn> findAllDeleted();

    // 2. Lấy 1 bản ghi đã xóa (dùng cho Service để khôi phục)
    @Query(value = "SELECT * FROM MonAn WHERE id = :id AND deleted_at IS NOT NULL", nativeQuery = true)
    Optional<MonAn> findDeletedById(@org.springframework.data.repository.query.Param("id") Long id);
}
