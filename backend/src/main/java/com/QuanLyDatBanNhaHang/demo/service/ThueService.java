package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.dto.request.ThueCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.ThueUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.ThueResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ThueService {
    Page<ThueResponseDTO> getAllThue(Pageable pageable);
    ThueResponseDTO getThueByMa(String maThue);
    ThueResponseDTO createThue(ThueCreateRequestDTO requestDTO);
    ThueResponseDTO updateThue(String maThue, ThueUpdateRequestDTO requestDTO);
    void deleteThue(String maThue);
}
