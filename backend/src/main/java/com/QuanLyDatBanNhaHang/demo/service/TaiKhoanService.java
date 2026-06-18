package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.dto.request.TaiKhoanCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.TaiKhoanUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.TaiKhoanResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaiKhoanService {
    Page<TaiKhoanResponseDTO> getAllTaiKhoan(Pageable pageable);
    TaiKhoanResponseDTO getTaiKhoanByUsername(String username);
    TaiKhoanResponseDTO createTaiKhoan(TaiKhoanCreateRequestDTO requestDTO);
    TaiKhoanResponseDTO updateTaiKhoan(String username, TaiKhoanUpdateRequestDTO requestDTO);
    void deleteTaiKhoan(String username);
}
