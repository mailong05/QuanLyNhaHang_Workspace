package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.dto.request.ChiTietHoaDonCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.ChiTietHoaDonUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.ChiTietHoaDonResponseDTO;
import java.util.List;
import java.util.stream.Collectors;

public interface ChiTietHoaDonService {
    List<ChiTietHoaDonResponseDTO> getAllChiTietHoaDon();
    ChiTietHoaDonResponseDTO getChiTietHoaDonById(Long id);
    ChiTietHoaDonResponseDTO createChiTietHoaDon(ChiTietHoaDonCreateRequestDTO requestDTO);
    ChiTietHoaDonResponseDTO updateChiTietHoaDon(Long id, ChiTietHoaDonUpdateRequestDTO requestDTO);
    void deleteChiTietHoaDon(Long id);
}
