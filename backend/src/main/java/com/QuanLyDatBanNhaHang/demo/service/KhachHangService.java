package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.dto.request.KhachHangCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.KhachHangUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.KhachHangResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KhachHangService {
    Page<KhachHangResponseDTO> getAllKhachHang(Pageable pageable);
    Page<KhachHangResponseDTO> searchKhachHang(String keyword, Pageable pageable);
    KhachHangResponseDTO getKhachHangByMa(String maKH);
    KhachHangResponseDTO createKhachHang(KhachHangCreateRequestDTO requestDTO);
    KhachHangResponseDTO updateKhachHang(String maKH, KhachHangUpdateRequestDTO requestDTO);
    void deleteKhachHang(String maKH);
}
