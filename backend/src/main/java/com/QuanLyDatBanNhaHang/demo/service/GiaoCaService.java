package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.dto.request.GiaoCaCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.GiaoCaUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.GiaoCaResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.QuanLyDatBanNhaHang.demo.dto.request.VaoCaRequest;
import com.QuanLyDatBanNhaHang.demo.dto.request.KetCaRequest;

public interface GiaoCaService {
    Page<GiaoCaResponseDTO> getAllGiaoCa(Pageable pageable);
    GiaoCaResponseDTO getGiaoCaById(Long id);
    GiaoCaResponseDTO vaoCa(VaoCaRequest request);
    GiaoCaResponseDTO getGiaoCaHienTai();
    GiaoCaResponseDTO ketCa(KetCaRequest request);
}
