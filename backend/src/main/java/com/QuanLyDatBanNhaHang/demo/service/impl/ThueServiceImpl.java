package com.QuanLyDatBanNhaHang.demo.service.impl;

import com.QuanLyDatBanNhaHang.demo.dto.request.ThueCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.ThueUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.ThueResponseDTO;
import com.QuanLyDatBanNhaHang.demo.entity.Thue;
import com.QuanLyDatBanNhaHang.demo.exception.DuplicateResourceException;
import com.QuanLyDatBanNhaHang.demo.exception.ResourceNotFoundException;
import com.QuanLyDatBanNhaHang.demo.repository.ThueRepository;
import com.QuanLyDatBanNhaHang.demo.service.ThueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ThueServiceImpl implements ThueService {

    private final ThueRepository thueRepository;

    @Override
    public Page<ThueResponseDTO> getAllThue(Pageable pageable) {
        return thueRepository.findAll(pageable).map(this::convertToResponseDTO);
    }

    @Override
    public ThueResponseDTO getThueByMa(String maThue) {
        Thue t = thueRepository.findByMaThueIgnoreCase(maThue)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Thuế với mã: " + maThue));
        return convertToResponseDTO(t);
    }

    @Override
    public ThueResponseDTO createThue(ThueCreateRequestDTO requestDTO) {

        
        Thue t = Thue.builder()
                .maThue(generateNextMaThue())
                .tenThue(requestDTO.getTenThue())
                .thueSuat(requestDTO.getThueSuat())
                .trangThai(requestDTO.getTrangThai())
                .build();
                
        return convertToResponseDTO(thueRepository.save(t));
    }

    private String generateNextMaThue() {
        Integer maxMa = thueRepository.findMaxMaThue();
        if (maxMa == null) {
            return String.format("TH%06d", 1);
        }
        return String.format("TH%06d", maxMa + 1);
    }

    @Override
    public ThueResponseDTO updateThue(String maThue, ThueUpdateRequestDTO requestDTO) {
        Thue t = thueRepository.findByMaThueIgnoreCase(maThue)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Thuế với mã: " + maThue));
                
        t.setTenThue(requestDTO.getTenThue());
        t.setThueSuat(requestDTO.getThueSuat());
        t.setTrangThai(requestDTO.getTrangThai());

        return convertToResponseDTO(thueRepository.save(t));
    }

    @Override
    public void deleteThue(String maThue) {
        Thue t = thueRepository.findByMaThueIgnoreCase(maThue)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Thuế với mã: " + maThue));
        thueRepository.delete(t);
    }

    private ThueResponseDTO convertToResponseDTO(Thue t) {
        return ThueResponseDTO.builder()
                .id(t.getId())
                .maThue(t.getMaThue())
                .tenThue(t.getTenThue())
                .thueSuat(t.getThueSuat())
                .trangThai(t.getTrangThai())
                .build();
    }
}
