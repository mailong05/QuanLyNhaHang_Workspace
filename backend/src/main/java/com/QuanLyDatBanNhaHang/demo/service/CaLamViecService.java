package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.dto.request.CaLamViecCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.CaLamViecUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.CaLamViecResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CaLamViecService {
    Page<CaLamViecResponseDTO> getAllCaLamViec(Pageable pageable);
    CaLamViecResponseDTO getCaLamViecByMa(String maCa);
    CaLamViecResponseDTO createCaLamViec(CaLamViecCreateRequestDTO requestDTO);
    CaLamViecResponseDTO updateCaLamViec(String maCa, CaLamViecUpdateRequestDTO requestDTO);
    void deleteCaLamViec(String maCa);
}
