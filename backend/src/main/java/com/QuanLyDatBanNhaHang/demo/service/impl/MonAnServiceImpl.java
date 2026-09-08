package com.QuanLyDatBanNhaHang.demo.service.impl;

import com.QuanLyDatBanNhaHang.demo.dto.request.MonAnCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.MonAnUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.MonAnResponseDTO;
import com.QuanLyDatBanNhaHang.demo.entity.MonAn;
import com.QuanLyDatBanNhaHang.demo.exception.DuplicateResourceException;
import com.QuanLyDatBanNhaHang.demo.exception.ResourceNotFoundException;
import com.QuanLyDatBanNhaHang.demo.repository.MonAnRepository;
import com.QuanLyDatBanNhaHang.demo.service.MonAnService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MonAnServiceImpl implements MonAnService {

    private final MonAnRepository monAnRepository;

    @Override
    public Page<MonAnResponseDTO> getAllMonAn(String keyword, String trangThai, Pageable pageable) {
        com.QuanLyDatBanNhaHang.demo.enums.TrangThaiMonAn enumTrangThai = null;
        if (trangThai != null && !trangThai.trim().isEmpty()) {
            try { enumTrangThai = com.QuanLyDatBanNhaHang.demo.enums.TrangThaiMonAn.valueOf(trangThai); } catch(Exception e) {}
        }
        String kw = (keyword != null && !keyword.trim().isEmpty()) ? "%" + keyword.trim() + "%" : null;
        return monAnRepository.searchMonAn(kw, enumTrangThai, pageable).map(this::convertToResponseDTO);
    }

    @Override
    public MonAnResponseDTO getMonAnByMa(String maMon) {
        MonAn ma = monAnRepository.findByMaMonIgnoreCase(maMon)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Món ăn với mã: " + maMon));
        return convertToResponseDTO(ma);
    }

    @Override
    public MonAnResponseDTO createMonAn(MonAnCreateRequestDTO requestDTO) {

        
        MonAn ma = MonAn.builder()
                .maMon(generateNextMaMon())
                .tenMon(requestDTO.getTenMon())
                .donGia(requestDTO.getDonGia())
                .donViTinh(requestDTO.getDonViTinh())
                .tenLoai(requestDTO.getTenLoai())
                .trangThai(requestDTO.getTrangThai())
                .urlHinhAnh(requestDTO.getUrlHinhAnh())
                .build();
                
        return convertToResponseDTO(monAnRepository.save(ma));
    }

    private String generateNextMaMon() {
        Integer maxMa = monAnRepository.findMaxMaMon();
        if (maxMa == null) {
            return String.format("MA%06d", 1);
        }
        return String.format("MA%06d", maxMa + 1);
    }

    @Override
    public MonAnResponseDTO updateMonAn(String maMon, MonAnUpdateRequestDTO requestDTO) {
        MonAn ma = monAnRepository.findByMaMonIgnoreCase(maMon)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Món ăn với mã: " + maMon));
                
        ma.setTenMon(requestDTO.getTenMon());
        ma.setDonGia(requestDTO.getDonGia());
        ma.setDonViTinh(requestDTO.getDonViTinh());
        ma.setTenLoai(requestDTO.getTenLoai());
        ma.setTrangThai(requestDTO.getTrangThai());
        ma.setUrlHinhAnh(requestDTO.getUrlHinhAnh());

        return convertToResponseDTO(monAnRepository.save(ma));
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void deleteMonAn(String maMon) {
        MonAn ma = monAnRepository.findByMaMonIgnoreCase(maMon)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Món ăn với mã: " + maMon));
                
        // Giải phóng Unique Key trước khi xóa mềm
        if (ma.getMaMon() != null && !ma.getMaMon().contains("_deleted_")) {
            ma.setMaMon(ma.getMaMon() + "_deleted_" + System.currentTimeMillis());
            monAnRepository.saveAndFlush(ma);
        }
        
        // Kích hoạt @SQLDelete
        monAnRepository.delete(ma);
    }

    @Override
    public void restoreMonAn(Long id) {
        MonAn ma = monAnRepository.findDeletedById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Món ăn đã xóa với ID: " + id));

        // Bỏ đánh dấu xóa
        ma.setDeletedAt(null);
        
        // Cắt bỏ hậu tố _deleted_ để lấy lại mã gốc
        String originalMaMon = ma.getMaMon().replaceAll("_deleted_.*", "");
        // Chủ động kiểm tra trước thay vì try-catch để tránh bẫy Rollback-Only Transaction
        if (monAnRepository.existsByMaMon(originalMaMon)) {
            // Nếu mã gốc đã bị chiếm dụng, cấp mã kèm _restored_
            ma.setMaMon(originalMaMon + "_restored_" + System.currentTimeMillis());
        } else {
            // An toàn -> Trả lại mã gốc
            ma.setMaMon(originalMaMon);
        }
        
        monAnRepository.save(ma);
    }

    private MonAnResponseDTO convertToResponseDTO(MonAn ma) {
        return MonAnResponseDTO.builder()
                .id(ma.getId())
                .maMon(ma.getMaMon())
                .tenMon(ma.getTenMon())
                .donGia(ma.getDonGia())
                .donViTinh(ma.getDonViTinh())
                .tenLoai(ma.getTenLoai())
                .trangThai(ma.getTrangThai())
                .urlHinhAnh(ma.getUrlHinhAnh())
                .build();
    }
}
