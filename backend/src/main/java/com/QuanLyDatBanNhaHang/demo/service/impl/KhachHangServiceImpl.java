package com.QuanLyDatBanNhaHang.demo.service.impl;

import com.QuanLyDatBanNhaHang.demo.dto.request.KhachHangCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.KhachHangUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.KhachHangResponseDTO;
import com.QuanLyDatBanNhaHang.demo.entity.KhachHang;
import com.QuanLyDatBanNhaHang.demo.entity.TaiKhoan;
import com.QuanLyDatBanNhaHang.demo.exception.DuplicateResourceException;
import com.QuanLyDatBanNhaHang.demo.exception.ResourceNotFoundException;
import com.QuanLyDatBanNhaHang.demo.repository.KhachHangRepository;
import com.QuanLyDatBanNhaHang.demo.repository.TaiKhoanRepository;
import com.QuanLyDatBanNhaHang.demo.service.KhachHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KhachHangServiceImpl implements KhachHangService {

    private final KhachHangRepository khachHangRepository;
    private final TaiKhoanRepository taiKhoanRepository;

    @Override
    public Page<KhachHangResponseDTO> getAllKhachHang(Pageable pageable) {
        return khachHangRepository.findAllWithRelations(pageable).map(this::convertToResponseDTO);
    }

    @Override
    public Page<KhachHangResponseDTO> searchKhachHang(String keyword, Pageable pageable) {
        return khachHangRepository.searchByHoTenOrSdt(keyword, pageable).map(this::convertToResponseDTO);
    }

    @Override
    public KhachHangResponseDTO getKhachHangByMa(String maKH) {
        KhachHang kh = khachHangRepository.findByMaKHIgnoreCaseWithAuth(maKH)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Khách hàng với mã: " + maKH));
        return convertToResponseDTO(kh);
    }

    @Override
    public KhachHangResponseDTO createKhachHang(KhachHangCreateRequestDTO requestDTO) {

        TaiKhoan tk = null;
        if (requestDTO.getUsername() != null && !requestDTO.getUsername().isBlank()) {
            tk = taiKhoanRepository.findByUsernameIgnoreCase(requestDTO.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Tài khoản: " + requestDTO.getUsername()));
        }

        KhachHang kh = KhachHang.builder()
                .maKH(generateNextMaKH())
                .hoTen(requestDTO.getHoTen())
                .sdt(requestDTO.getSdt())
                .email(requestDTO.getEmail())
                .loaiThanhVien(requestDTO.getLoaiThanhVien())
                .diemTichLuy(0) // Default when creating
                .taiKhoan(tk)
                .build();
        
        return convertToResponseDTO(khachHangRepository.save(kh));
    }

    @Override
    public KhachHangResponseDTO updateKhachHang(String maKH, KhachHangUpdateRequestDTO requestDTO) {
        KhachHang kh = khachHangRepository.findByMaKHIgnoreCaseWithAuth(maKH)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Khách hàng với mã: " + maKH));

        TaiKhoan tk = null;
        if (requestDTO.getUsername() != null && !requestDTO.getUsername().isBlank()) {
            tk = taiKhoanRepository.findByUsernameIgnoreCase(requestDTO.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Tài khoản: " + requestDTO.getUsername()));
        }

        kh.setHoTen(requestDTO.getHoTen());
        kh.setSdt(requestDTO.getSdt());
        kh.setEmail(requestDTO.getEmail());
        kh.setLoaiThanhVien(requestDTO.getLoaiThanhVien());
        if (requestDTO.getDiemTichLuy() != null) {
            kh.setDiemTichLuy(requestDTO.getDiemTichLuy());
        }
        kh.setTaiKhoan(tk);

        return convertToResponseDTO(khachHangRepository.save(kh));
    }

    @Override
    public void deleteKhachHang(String maKH) {
        KhachHang kh = khachHangRepository.findByMaKHIgnoreCase(maKH)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Khách hàng với mã: " + maKH));
        khachHangRepository.delete(kh);
    }

    private KhachHangResponseDTO convertToResponseDTO(KhachHang kh) {
        return KhachHangResponseDTO.builder()
                .id(kh.getId())
                .maKH(kh.getMaKH())
                .hoTen(kh.getHoTen())
                .sdt(kh.getSdt())
                .email(kh.getEmail())
                .diemTichLuy(kh.getDiemTichLuy())
                .loaiThanhVien(kh.getLoaiThanhVien())
                .username(kh.getTaiKhoan() != null ? kh.getTaiKhoan().getUsername() : null)
                .quyenHan(kh.getTaiKhoan() != null ? kh.getTaiKhoan().getQuyenHan().name() : null)
                .build();
    }

    private String generateNextMaKH() {
        Integer maxMa = khachHangRepository.findMaxMaKH();
        if (maxMa == null) {
            return String.format("KH%06d", 1);
        }
        return String.format("KH%06d", maxMa + 1);
    }
}
