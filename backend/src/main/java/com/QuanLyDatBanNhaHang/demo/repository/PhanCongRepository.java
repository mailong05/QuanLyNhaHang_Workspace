package com.QuanLyDatBanNhaHang.demo.repository;

import com.QuanLyDatBanNhaHang.demo.entity.PhanCong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PhanCongRepository extends JpaRepository<PhanCong, Long> {
    
    @Query("SELECT p FROM PhanCong p JOIN FETCH p.caLamViec JOIN FETCH p.nhanVien")
    Page<PhanCong> findAllWithRelations(Pageable pageable);
}
