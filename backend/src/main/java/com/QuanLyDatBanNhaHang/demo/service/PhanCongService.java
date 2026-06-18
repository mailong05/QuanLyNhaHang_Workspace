package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.dto.request.PhanCongCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.PhanCongUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.PhanCongResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PhanCongService {
    Page<PhanCongResponseDTO> getAllPhanCong(Pageable pageable);
    PhanCongResponseDTO getPhanCongById(Long id);
    PhanCongResponseDTO createPhanCong(PhanCongCreateRequestDTO requestDTO);
    PhanCongResponseDTO updatePhanCong(Long id, PhanCongUpdateRequestDTO requestDTO);
    void deletePhanCong(Long id);
}
