package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.dto.request.ChiTietPhieuDatBanCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.ChiTietPhieuDatBanUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.ChiTietPhieuDatBanResponseDTO;
import java.util.List;
import java.util.stream.Collectors;

public interface ChiTietPhieuDatBanService {
    List<ChiTietPhieuDatBanResponseDTO> getAllChiTietPhieuDatBan();
    ChiTietPhieuDatBanResponseDTO getChiTietPhieuDatBanById(Long id);
    ChiTietPhieuDatBanResponseDTO createChiTietPhieuDatBan(ChiTietPhieuDatBanCreateRequestDTO requestDTO);
    ChiTietPhieuDatBanResponseDTO updateChiTietPhieuDatBan(Long id, ChiTietPhieuDatBanUpdateRequestDTO requestDTO);
    void deleteChiTietPhieuDatBan(Long id);
}
