package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.dto.request.PosMoBanRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.PosThemMonRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.HoaDonResponseDTO;

public interface PosOperationService {
    HoaDonResponseDTO moBan(PosMoBanRequestDTO request);
    HoaDonResponseDTO layHoaDonTheoBan(Long banId);
    HoaDonResponseDTO themMon(PosThemMonRequestDTO request);
}
