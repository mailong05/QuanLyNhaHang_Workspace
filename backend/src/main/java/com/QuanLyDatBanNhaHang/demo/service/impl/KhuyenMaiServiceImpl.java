package com.QuanLyDatBanNhaHang.demo.service.impl;

import com.QuanLyDatBanNhaHang.demo.dto.request.KhuyenMaiCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.KhuyenMaiUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.KhuyenMaiResponseDTO;
import com.QuanLyDatBanNhaHang.demo.entity.KhuyenMai;
import com.QuanLyDatBanNhaHang.demo.exception.DuplicateResourceException;
import com.QuanLyDatBanNhaHang.demo.exception.ResourceNotFoundException;
import com.QuanLyDatBanNhaHang.demo.repository.KhuyenMaiRepository;
import com.QuanLyDatBanNhaHang.demo.service.KhuyenMaiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KhuyenMaiServiceImpl implements KhuyenMaiService {

    private final KhuyenMaiRepository khuyenMaiRepository;

    @Override
    public Page<KhuyenMaiResponseDTO> getAllKhuyenMai(Pageable pageable) {
        return khuyenMaiRepository.findAll(pageable).map(this::convertToResponseDTO);
    }

    @Override
    public KhuyenMaiResponseDTO getKhuyenMaiByMa(String maKM) {
        KhuyenMai km = khuyenMaiRepository.findByMaKMIgnoreCase(maKM)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Khuyến mãi với mã: " + maKM));
        return convertToResponseDTO(km);
    }

    @Override
    public KhuyenMaiResponseDTO createKhuyenMai(KhuyenMaiCreateRequestDTO requestDTO) {

        
        KhuyenMai km = KhuyenMai.builder()
                .maKM(generateNextMaKM())
                .tenKM(requestDTO.getTenKM())
                .giaTriGiam(requestDTO.getGiaTriGiam())
                .ngayBatDau(requestDTO.getNgayBatDau())
                .ngayKetThuc(requestDTO.getNgayKetThuc())
                .dieuKienToiThieu(requestDTO.getDieuKienToiThieu())
                .trangThai(requestDTO.getTrangThai())
                .build();
                
        return convertToResponseDTO(khuyenMaiRepository.save(km));
    }

    @Override
    public KhuyenMaiResponseDTO updateKhuyenMai(String maKM, KhuyenMaiUpdateRequestDTO requestDTO) {
        KhuyenMai km = khuyenMaiRepository.findByMaKMIgnoreCase(maKM)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Khuyến mãi với mã: " + maKM));
                
        km.setTenKM(requestDTO.getTenKM());
        km.setGiaTriGiam(requestDTO.getGiaTriGiam());
        km.setNgayBatDau(requestDTO.getNgayBatDau());
        km.setNgayKetThuc(requestDTO.getNgayKetThuc());
        km.setDieuKienToiThieu(requestDTO.getDieuKienToiThieu());
        km.setTrangThai(requestDTO.getTrangThai());

        return convertToResponseDTO(khuyenMaiRepository.save(km));
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void deleteKhuyenMai(String maKM) {
        KhuyenMai km = khuyenMaiRepository.findByMaKMIgnoreCase(maKM)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Khuyến mãi với mã: " + maKM));
                
        if (km.getMaKM() != null && !km.getMaKM().contains("_deleted_")) {
            km.setMaKM(km.getMaKM() + "_deleted_" + System.currentTimeMillis());
            khuyenMaiRepository.saveAndFlush(km);
        }
        
        khuyenMaiRepository.delete(km);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void restoreKhuyenMai(Long id) {
        KhuyenMai entity = khuyenMaiRepository.findDeletedById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy dữ liệu đã xóa với ID: " + id));

        entity.setDeletedAt(null);
        String originalCode = entity.getMaKM().replaceAll("_deleted_.*", "");
        
        if (khuyenMaiRepository.existsByMaKM(originalCode)) {
            entity.setMaKM(originalCode + "_restored_" + System.currentTimeMillis());
        } else {
            entity.setMaKM(originalCode);
        }
        
        khuyenMaiRepository.save(entity);
    }

    private KhuyenMaiResponseDTO convertToResponseDTO(KhuyenMai km) {
        return KhuyenMaiResponseDTO.builder()
                .id(km.getId())
                .maKM(km.getMaKM())
                .tenKM(km.getTenKM())
                .giaTriGiam(km.getGiaTriGiam())
                .ngayBatDau(km.getNgayBatDau())
                .ngayKetThuc(km.getNgayKetThuc())
                .dieuKienToiThieu(km.getDieuKienToiThieu())
                .trangThai(km.getTrangThai())
                .build();
    }

    private String generateNextMaKM() {
        Integer maxMa = khuyenMaiRepository.findMaxMaKM();
        if (maxMa == null) {
            return String.format("KM%06d", 1);
        }
        return String.format("KM%06d", maxMa + 1);
    }
}
