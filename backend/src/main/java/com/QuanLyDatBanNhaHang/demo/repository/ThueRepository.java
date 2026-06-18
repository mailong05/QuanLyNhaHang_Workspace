package com.QuanLyDatBanNhaHang.demo.repository;

import com.QuanLyDatBanNhaHang.demo.entity.Thue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThueRepository extends JpaRepository<Thue, Long> {
    Optional<Thue> findByMaThueIgnoreCase(String maThue);

    
    @Query("SELECT MAX(CAST(SUBSTRING(t.maThue, 3, 6) AS int)) FROM Thue t")
    Integer findMaxMaThue();
}
