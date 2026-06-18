package com.QuanLyDatBanNhaHang.demo.service.impl;

import com.QuanLyDatBanNhaHang.demo.dto.request.CaLamViecCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.CaLamViecUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.CaLamViecResponseDTO;
import com.QuanLyDatBanNhaHang.demo.entity.CaLamViec;
import com.QuanLyDatBanNhaHang.demo.exception.DuplicateResourceException;
import com.QuanLyDatBanNhaHang.demo.exception.ResourceNotFoundException;
import com.QuanLyDatBanNhaHang.demo.repository.CaLamViecRepository;
import com.QuanLyDatBanNhaHang.demo.service.CaLamViecService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CaLamViecServiceImpl implements CaLamViecService {

    private final CaLamViecRepository caLamViecRepository;

    @Override
    public Page<CaLamViecResponseDTO> getAllCaLamViec(Pageable pageable) {
        return caLamViecRepository.findAll(pageable).map(this::convertToResponseDTO);
    }

    @Override
    public CaLamViecResponseDTO getCaLamViecByMa(String maCa) {
        CaLamViec ca = caLamViecRepository.findByMaCaIgnoreCase(maCa)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Ca Làm Việc với mã: " + maCa));
        return convertToResponseDTO(ca);
    }

    @Override
    public CaLamViecResponseDTO createCaLamViec(CaLamViecCreateRequestDTO requestDTO) {

        if (requestDTO.getGioKetThuc().isBefore(requestDTO.getGioBatDau())) {
            throw new IllegalArgumentException("Giờ kết thúc phải lớn hơn giờ bắt đầu.");
        }


        
        CaLamViec caLamViec = CaLamViec.builder()
                .maCa(generateNextMaCa())
                .tenCa(requestDTO.getTenCa())
                .gioBatDau(requestDTO.getGioBatDau())
                .gioKetThuc(requestDTO.getGioKetThuc())
                .trangThai(requestDTO.getTrangThai())
                .build();
                
        return convertToResponseDTO(caLamViecRepository.save(caLamViec));
    }

    @Override
    public CaLamViecResponseDTO updateCaLamViec(String maCa, CaLamViecUpdateRequestDTO requestDTO) {

        if (requestDTO.getGioKetThuc().isBefore(requestDTO.getGioBatDau())) {
            throw new IllegalArgumentException("Giờ kết thúc phải lớn hơn giờ bắt đầu.");
        }

        CaLamViec ca = caLamViecRepository.findByMaCaIgnoreCase(maCa)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Ca Làm Việc với mã: " + maCa));
                
        ca.setTenCa(requestDTO.getTenCa());
        ca.setGioBatDau(requestDTO.getGioBatDau());
        ca.setGioKetThuc(requestDTO.getGioKetThuc());
        
        return convertToResponseDTO(caLamViecRepository.save(ca));
    }

    @Override
    public void deleteCaLamViec(String maCa) {
        CaLamViec ca = caLamViecRepository.findByMaCaIgnoreCase(maCa)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Ca Làm Việc với mã: " + maCa));
        caLamViecRepository.delete(ca);
    }

    private CaLamViecResponseDTO convertToResponseDTO(CaLamViec caLamViec) {
        return CaLamViecResponseDTO.builder()
                .id(caLamViec.getId())
                .maCa(caLamViec.getMaCa())
                .tenCa(caLamViec.getTenCa())
                .gioBatDau(caLamViec.getGioBatDau())
                .gioKetThuc(caLamViec.getGioKetThuc())
                .build();
    }

    private String generateNextMaCa() {
        Integer maxMa = caLamViecRepository.findMaxMaCa();
        if (maxMa == null) {
            return String.format("CA%06d", 1);
        }
        return String.format("CA%06d", maxMa + 1);
    }
}
