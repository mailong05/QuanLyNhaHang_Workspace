package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.dto.request.NhanVienCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.NhanVienUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.NhanVienResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NhanVienService {
    Page<NhanVienResponseDTO> getAllNhanVien(Pageable pageable);
    Page<NhanVienResponseDTO> searchNhanVien(String keyword, Pageable pageable);
    NhanVienResponseDTO getNhanVienByMa(String maNV);
    NhanVienResponseDTO createNhanVien(NhanVienCreateRequestDTO requestDTO);
    NhanVienResponseDTO updateNhanVien(String maNV, NhanVienUpdateRequestDTO requestDTO);
    void deleteNhanVien(String maNV);
}
