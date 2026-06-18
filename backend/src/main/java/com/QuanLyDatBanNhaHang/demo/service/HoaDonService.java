package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.dto.request.HoaDonCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.HoaDonUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.HoaDonResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HoaDonService {
    Page<HoaDonResponseDTO> getAllHoaDon(Pageable pageable);
    HoaDonResponseDTO getHoaDonByMa(String maHD);
    HoaDonResponseDTO createHoaDon(HoaDonCreateRequestDTO requestDTO);
    HoaDonResponseDTO updateHoaDon(String maHD, HoaDonUpdateRequestDTO requestDTO);
    void deleteHoaDon(String maHD);
}
