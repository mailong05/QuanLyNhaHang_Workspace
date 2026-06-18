package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.dto.request.PhieuDatBanCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.PhieuDatBanUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.PhieuDatBanResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PhieuDatBanService {
    Page<PhieuDatBanResponseDTO> getAllPhieuDatBan(Pageable pageable);
    PhieuDatBanResponseDTO getPhieuDatBanByMa(String maPhieuDat);
    PhieuDatBanResponseDTO createPhieuDatBan(PhieuDatBanCreateRequestDTO requestDTO);
    PhieuDatBanResponseDTO updatePhieuDatBan(String maPhieuDat, PhieuDatBanUpdateRequestDTO requestDTO);
    void deletePhieuDatBan(String maPhieuDat);
}
