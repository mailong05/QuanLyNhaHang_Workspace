package com.QuanLyDatBanNhaHang.demo.repository;

import com.QuanLyDatBanNhaHang.demo.entity.CaLamViec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CaLamViecRepository extends JpaRepository<CaLamViec, Long> {
    
    Optional<CaLamViec> findByMaCaIgnoreCase(String maCa);
    
    Page<CaLamViec> findByTenCaContainingIgnoreCase(String tenCa, Pageable pageable);
    @Query("SELECT MAX(CAST(SUBSTRING(c.maCa, 3, 6) AS int)) FROM CaLamViec c")
    Integer findMaxMaCa();
}
