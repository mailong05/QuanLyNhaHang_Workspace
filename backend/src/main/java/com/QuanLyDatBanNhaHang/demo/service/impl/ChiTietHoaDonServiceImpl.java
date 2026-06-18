package com.QuanLyDatBanNhaHang.demo.service.impl;

import com.QuanLyDatBanNhaHang.demo.service.ChiTietHoaDonService;
import com.QuanLyDatBanNhaHang.demo.dto.request.ChiTietHoaDonCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.ChiTietHoaDonUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.ChiTietHoaDonResponseDTO;
import com.QuanLyDatBanNhaHang.demo.entity.ChiTietHoaDon;
import com.QuanLyDatBanNhaHang.demo.entity.HoaDon;
import com.QuanLyDatBanNhaHang.demo.entity.MonAn;
import com.QuanLyDatBanNhaHang.demo.repository.ChiTietHoaDonRepository;
import com.QuanLyDatBanNhaHang.demo.repository.HoaDonRepository;
import com.QuanLyDatBanNhaHang.demo.repository.MonAnRepository;
import com.QuanLyDatBanNhaHang.demo.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChiTietHoaDonServiceImpl implements ChiTietHoaDonService {

    private final ChiTietHoaDonRepository chiTietHoaDonRepository;
    private final HoaDonRepository hoaDonRepository;
    private final MonAnRepository monAnRepository;

    public List<ChiTietHoaDonResponseDTO> getAllChiTietHoaDon() {
        List<ChiTietHoaDon> chiTiets = chiTietHoaDonRepository.findAllWithRelations();
        return chiTiets.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public ChiTietHoaDonResponseDTO getChiTietHoaDonById(Long id) {
        ChiTietHoaDon chiTiet = chiTietHoaDonRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Chi Tiết Hóa Đơn với ID: " + id));
        return convertToResponseDTO(chiTiet);
    }

    @Transactional
    public ChiTietHoaDonResponseDTO createChiTietHoaDon(ChiTietHoaDonCreateRequestDTO requestDTO) {
        HoaDon hd = null;
        if (requestDTO.getMaHD() != null && !requestDTO.getMaHD().isBlank()) {
            hd = hoaDonRepository.findByMaHDIgnoreCaseWithRelations(requestDTO.getMaHD())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Hóa đơn: " + requestDTO.getMaHD()));
        }

        MonAn ma = monAnRepository.findByMaMonIgnoreCase(requestDTO.getMaMon())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Món ăn: " + requestDTO.getMaMon()));

        Double thanhTien = ma.getDonGia() * requestDTO.getSoLuong();

        ChiTietHoaDon chiTiet = ChiTietHoaDon.builder()
                .hoaDon(hd)
                .monAn(ma)
                .soLuong(requestDTO.getSoLuong())
                .donGiaLuuTru(ma.getDonGia())
                .ghiChu(requestDTO.getGhiChu())
                .thanhTien(thanhTien)
                .build();

        ChiTietHoaDon saved = chiTietHoaDonRepository.save(chiTiet);
        return convertToResponseDTO(saved);
    }

    @Transactional
    public ChiTietHoaDonResponseDTO updateChiTietHoaDon(Long id, ChiTietHoaDonUpdateRequestDTO requestDTO) {
        ChiTietHoaDon chiTiet = chiTietHoaDonRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Chi Tiết Hóa Đơn với ID: " + id));

        HoaDon hd = hoaDonRepository.findByMaHDIgnoreCaseWithRelations(requestDTO.getMaHD())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Hóa đơn: " + requestDTO.getMaHD()));

        MonAn ma = monAnRepository.findByMaMonIgnoreCase(requestDTO.getMaMon())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Món ăn: " + requestDTO.getMaMon()));

        chiTiet.setHoaDon(hd);
        chiTiet.setMonAn(ma);
        chiTiet.setSoLuong(requestDTO.getSoLuong());
        chiTiet.setDonGiaLuuTru(requestDTO.getDonGiaLuuTru());
        chiTiet.setGhiChu(requestDTO.getGhiChu());
        chiTiet.setThanhTien(requestDTO.getThanhTien());

        ChiTietHoaDon updated = chiTietHoaDonRepository.save(chiTiet);
        return convertToResponseDTO(updated);
    }

    @Transactional
    public void deleteChiTietHoaDon(Long id) {
        chiTietHoaDonRepository.deleteById(id);
    }

    private ChiTietHoaDonResponseDTO convertToResponseDTO(ChiTietHoaDon chiTiet) {
        return ChiTietHoaDonResponseDTO.builder()
                .id(chiTiet.getId())
                .maMon(chiTiet.getMonAn() != null ? chiTiet.getMonAn().getMaMon() : null)
                .tenMon(chiTiet.getMonAn() != null ? chiTiet.getMonAn().getTenMon() : null)
                .soLuong(chiTiet.getSoLuong())
                .donGiaLuuTru(chiTiet.getDonGiaLuuTru())
                .ghiChu(chiTiet.getGhiChu())
                .thanhTien(chiTiet.getThanhTien())
                .build();
    }
}
