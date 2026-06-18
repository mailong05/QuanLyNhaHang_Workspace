package com.QuanLyDatBanNhaHang.demo.service.impl;

import com.QuanLyDatBanNhaHang.demo.service.ChiTietPhieuDatBanService;
import com.QuanLyDatBanNhaHang.demo.dto.request.ChiTietPhieuDatBanCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.ChiTietPhieuDatBanUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.ChiTietPhieuDatBanResponseDTO;
import com.QuanLyDatBanNhaHang.demo.entity.BanAn;
import com.QuanLyDatBanNhaHang.demo.entity.ChiTietPhieuDatBan;
import com.QuanLyDatBanNhaHang.demo.entity.PhieuDatBan;
import com.QuanLyDatBanNhaHang.demo.repository.BanAnRepository;
import com.QuanLyDatBanNhaHang.demo.repository.ChiTietPhieuDatBanRepository;
import com.QuanLyDatBanNhaHang.demo.repository.PhieuDatBanRepository;
import com.QuanLyDatBanNhaHang.demo.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChiTietPhieuDatBanServiceImpl implements ChiTietPhieuDatBanService {

    private final ChiTietPhieuDatBanRepository chiTietPhieuDatBanRepository;
    private final PhieuDatBanRepository phieuDatBanRepository;
    private final BanAnRepository banAnRepository;

    public List<ChiTietPhieuDatBanResponseDTO> getAllChiTietPhieuDatBan() {
        List<ChiTietPhieuDatBan> chiTiets = chiTietPhieuDatBanRepository.findAllWithRelations();
        return chiTiets.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public ChiTietPhieuDatBanResponseDTO getChiTietPhieuDatBanById(Long id) {
        ChiTietPhieuDatBan chiTiet = chiTietPhieuDatBanRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Chi Tiết Phiếu Đặt Bàn với ID: " + id));
        return convertToResponseDTO(chiTiet);
    }

    @Transactional
    public ChiTietPhieuDatBanResponseDTO createChiTietPhieuDatBan(ChiTietPhieuDatBanCreateRequestDTO requestDTO) {
        PhieuDatBan pdb = null;
        if (requestDTO.getMaPhieuDat() != null && !requestDTO.getMaPhieuDat().isBlank()) {
            pdb = phieuDatBanRepository.findByMaPhieuDatIgnoreCaseWithRelations(requestDTO.getMaPhieuDat())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Phiếu đặt: " + requestDTO.getMaPhieuDat()));
        }

        BanAn ba = banAnRepository.findByMaBanIgnoreCase(requestDTO.getMaBan())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Bàn ăn: " + requestDTO.getMaBan()));

        ChiTietPhieuDatBan chiTiet = ChiTietPhieuDatBan.builder()
                .phieuDatBan(pdb)
                .banAn(ba)
                .ghiChu(requestDTO.getGhiChu())
                .build();

        ChiTietPhieuDatBan saved = chiTietPhieuDatBanRepository.save(chiTiet);
        return convertToResponseDTO(saved);
    }

    @Transactional
    public ChiTietPhieuDatBanResponseDTO updateChiTietPhieuDatBan(Long id, ChiTietPhieuDatBanUpdateRequestDTO requestDTO) {
        ChiTietPhieuDatBan chiTiet = chiTietPhieuDatBanRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Chi Tiết Phiếu Đặt Bàn với ID: " + id));

        PhieuDatBan pdb = phieuDatBanRepository.findByMaPhieuDatIgnoreCaseWithRelations(requestDTO.getMaPhieuDat())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Phiếu đặt: " + requestDTO.getMaPhieuDat()));

        BanAn ba = banAnRepository.findByMaBanIgnoreCase(requestDTO.getMaBan())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Bàn ăn: " + requestDTO.getMaBan()));

        chiTiet.setPhieuDatBan(pdb);
        chiTiet.setBanAn(ba);
        chiTiet.setGhiChu(requestDTO.getGhiChu());

        ChiTietPhieuDatBan updated = chiTietPhieuDatBanRepository.save(chiTiet);
        return convertToResponseDTO(updated);
    }

    @Transactional
    public void deleteChiTietPhieuDatBan(Long id) {
        chiTietPhieuDatBanRepository.deleteById(id);
    }

    private ChiTietPhieuDatBanResponseDTO convertToResponseDTO(ChiTietPhieuDatBan chiTiet) {
        return ChiTietPhieuDatBanResponseDTO.builder()
                .id(chiTiet.getId())
                .maBan(chiTiet.getBanAn() != null ? chiTiet.getBanAn().getMaBan() : null)
                .viTri(chiTiet.getBanAn() != null ? chiTiet.getBanAn().getViTri() : null)
                .ghiChu(chiTiet.getGhiChu())
                .build();
    }
}
