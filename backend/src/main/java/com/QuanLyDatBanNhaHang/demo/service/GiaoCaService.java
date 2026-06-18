package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.dto.request.GiaoCaCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.GiaoCaUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.GiaoCaResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GiaoCaService {
    Page<GiaoCaResponseDTO> getAllGiaoCa(Pageable pageable);
    GiaoCaResponseDTO getGiaoCaById(Long id);
    GiaoCaResponseDTO createGiaoCa(GiaoCaCreateRequestDTO requestDTO);
    GiaoCaResponseDTO updateGiaoCa(Long id, GiaoCaUpdateRequestDTO requestDTO);
    void deleteGiaoCa(Long id);
}
