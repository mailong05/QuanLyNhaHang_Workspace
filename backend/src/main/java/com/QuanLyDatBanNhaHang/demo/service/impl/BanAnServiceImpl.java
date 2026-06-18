package com.QuanLyDatBanNhaHang.demo.service.impl;

import com.QuanLyDatBanNhaHang.demo.dto.request.BanAnCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.BanAnUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.BanAnResponseDTO;
import com.QuanLyDatBanNhaHang.demo.entity.BanAn;
import com.QuanLyDatBanNhaHang.demo.entity.KhuVuc;
import com.QuanLyDatBanNhaHang.demo.exception.DuplicateResourceException;
import com.QuanLyDatBanNhaHang.demo.exception.ResourceNotFoundException;
import com.QuanLyDatBanNhaHang.demo.repository.BanAnRepository;
import com.QuanLyDatBanNhaHang.demo.repository.KhuVucRepository;
import com.QuanLyDatBanNhaHang.demo.service.BanAnService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BanAnServiceImpl implements BanAnService {

    private final BanAnRepository banAnRepository;
    private final KhuVucRepository khuVucRepository;

    @Override
    public Page<BanAnResponseDTO> getAllBanAn(Pageable pageable) {
        return banAnRepository.findAllWithRelations(pageable).map(this::convertToResponseDTO);
    }

    @Override
    public BanAnResponseDTO getBanAnByMa(String maBan) {
        BanAn ba = banAnRepository.findByMaBanIgnoreCaseWithKhuVuc(maBan)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Bàn ăn với mã: " + maBan));
        return convertToResponseDTO(ba);
    }

    @Override
    public BanAnResponseDTO createBanAn(BanAnCreateRequestDTO requestDTO) {


        KhuVuc kv = null;
        if (requestDTO.getMaKhuVuc() != null && !requestDTO.getMaKhuVuc().isBlank()) {
            kv = khuVucRepository.findByMaKhuVucIgnoreCase(requestDTO.getMaKhuVuc())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Khu vực: " + requestDTO.getMaKhuVuc()));
        }

        BanAn ba = BanAn.builder()
                .maBan(generateNextMaBan())
                .soGhe(requestDTO.getSoGhe())
                .viTri(requestDTO.getViTri())
                .trangThai(requestDTO.getTrangThai())
                .khuVuc(kv)
                .build();
        
        return convertToResponseDTO(banAnRepository.save(ba));
    }

    @Override
    public BanAnResponseDTO updateBanAn(String maBan, BanAnUpdateRequestDTO requestDTO) {
        BanAn ba = banAnRepository.findByMaBanIgnoreCaseWithKhuVuc(maBan)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Bàn ăn với mã: " + maBan));

        KhuVuc kv = null;
        if (requestDTO.getMaKhuVuc() != null && !requestDTO.getMaKhuVuc().isBlank()) {
            kv = khuVucRepository.findByMaKhuVucIgnoreCase(requestDTO.getMaKhuVuc())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Khu vực: " + requestDTO.getMaKhuVuc()));
        }

        ba.setSoGhe(requestDTO.getSoGhe());
        ba.setViTri(requestDTO.getViTri());
        ba.setTrangThai(requestDTO.getTrangThai());
        ba.setKhuVuc(kv);

        return convertToResponseDTO(banAnRepository.save(ba));
    }

    @Override
    public void deleteBanAn(String maBan) {
        BanAn ba = banAnRepository.findByMaBanIgnoreCase(maBan)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Bàn ăn với mã: " + maBan));
        banAnRepository.delete(ba);
    }

    private BanAnResponseDTO convertToResponseDTO(BanAn ba) {
        return BanAnResponseDTO.builder()
                .id(ba.getId())
                .maBan(ba.getMaBan())
                .soGhe(ba.getSoGhe())
                .viTri(ba.getViTri())
                .trangThai(ba.getTrangThai())
                .maKhuVuc(ba.getKhuVuc() != null ? ba.getKhuVuc().getMaKhuVuc() : null)
                .tenKhuVuc(ba.getKhuVuc() != null ? ba.getKhuVuc().getTenKhuVuc() : null)
                .build();
    }

    private String generateNextMaBan() {
        Integer maxMa = banAnRepository.findMaxMaBan();
        if (maxMa == null) {
            return String.format("BA%06d", 1);
        }
        return String.format("BA%06d", maxMa + 1);
    }
}
