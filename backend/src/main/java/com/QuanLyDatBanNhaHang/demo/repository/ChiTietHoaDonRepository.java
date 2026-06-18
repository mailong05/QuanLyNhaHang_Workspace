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
}
