package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.dto.request.BanAnCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.BanAnUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.BanAnResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BanAnService {
    Page<BanAnResponseDTO> getAllBanAn(Pageable pageable);
    BanAnResponseDTO getBanAnByMa(String maBan);
    BanAnResponseDTO createBanAn(BanAnCreateRequestDTO requestDTO);
    BanAnResponseDTO updateBanAn(String maBan, BanAnUpdateRequestDTO requestDTO);
    void deleteBanAn(String maBan);
}
