package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.dto.request.KhuVucCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.KhuVucUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.KhuVucResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KhuVucService {
    Page<KhuVucResponseDTO> getAllKhuVuc(Pageable pageable);
    KhuVucResponseDTO getKhuVucByMa(String maKhuVuc);
    KhuVucResponseDTO createKhuVuc(KhuVucCreateRequestDTO requestDTO);
    KhuVucResponseDTO updateKhuVuc(String maKhuVuc, KhuVucUpdateRequestDTO requestDTO);
    void deleteKhuVuc(String maKhuVuc);
}
