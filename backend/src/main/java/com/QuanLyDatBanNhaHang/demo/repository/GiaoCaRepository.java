package com.QuanLyDatBanNhaHang.demo.repository;

import com.QuanLyDatBanNhaHang.demo.entity.GiaoCa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GiaoCaRepository extends JpaRepository<GiaoCa, Long> {
    
    @Query("SELECT g FROM GiaoCa g JOIN FETCH g.nhanVien JOIN FETCH g.caLamViec")
    Page<GiaoCa> findAllWithRelations(Pageable pageable);
}
