package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.dto.request.PosMoBanRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.PosThemMonRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.HoaDonResponseDTO;

public interface PosOperationService {
    HoaDonResponseDTO moBan(PosMoBanRequestDTO request);
    HoaDonResponseDTO layHoaDonTheoBan(Long banId);
    HoaDonResponseDTO xoaMon(Long hoaDonId, Long chiTietId);
    HoaDonResponseDTO themMon(PosThemMonRequestDTO request);
    HoaDonResponseDTO thanhToanHoaDon(Long hoaDonId, com.QuanLyDatBanNhaHang.demo.dto.request.PosThanhToanRequestDTO request);
    HoaDonResponseDTO gopBan(com.QuanLyDatBanNhaHang.demo.dto.request.PosGopBanRequestDTO request);
}
