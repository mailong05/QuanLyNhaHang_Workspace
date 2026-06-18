package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.dto.request.MonAnCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.MonAnUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.MonAnResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MonAnService {
    Page<MonAnResponseDTO> getAllMonAn(Pageable pageable);
    MonAnResponseDTO getMonAnByMa(String maMon);
    MonAnResponseDTO createMonAn(MonAnCreateRequestDTO requestDTO);
    MonAnResponseDTO updateMonAn(String maMon, MonAnUpdateRequestDTO requestDTO);
    void deleteMonAn(String maMon);
}
