package com.QuanLyDatBanNhaHang.demo.repository;

import com.QuanLyDatBanNhaHang.demo.entity.MonAn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MonAnRepository extends JpaRepository<MonAn, Long> {
    Optional<MonAn> findByMaMonIgnoreCase(String maMon);
    Page<MonAn> findByTenMonContainingIgnoreCase(String tenMon, Pageable pageable);

    
    @Query("SELECT MAX(CAST(SUBSTRING(m.maMon, 3, 6) AS int)) FROM MonAn m")
    Integer findMaxMaMon();
}
