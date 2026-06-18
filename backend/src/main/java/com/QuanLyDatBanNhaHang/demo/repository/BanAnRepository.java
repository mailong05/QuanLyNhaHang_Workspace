package com.QuanLyDatBanNhaHang.demo.repository;

import com.QuanLyDatBanNhaHang.demo.entity.BanAn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BanAnRepository extends JpaRepository<BanAn, Long> {
    
    @Query("SELECT b FROM BanAn b LEFT JOIN FETCH b.khuVuc WHERE LOWER(b.maBan) = LOWER(:maBan)")
    Optional<BanAn> findByMaBanIgnoreCaseWithKhuVuc(@Param("maBan") String maBan);
    
    Optional<BanAn> findByMaBanIgnoreCase(String maBan);

    @Query(value = "SELECT b FROM BanAn b LEFT JOIN FETCH b.khuVuc", 
           countQuery = "SELECT COUNT(b) FROM BanAn b")
    Page<BanAn> findAllWithRelations(Pageable pageable);
    @Query("SELECT MAX(CAST(SUBSTRING(b.maBan, 3, 6) AS int)) FROM BanAn b")
    Integer findMaxMaBan();
}
