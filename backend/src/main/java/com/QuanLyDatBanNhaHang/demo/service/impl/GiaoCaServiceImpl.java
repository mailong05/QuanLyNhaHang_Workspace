package com.QuanLyDatBanNhaHang.demo.service.impl;

import com.QuanLyDatBanNhaHang.demo.dto.request.GiaoCaCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.GiaoCaUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.GiaoCaResponseDTO;
import com.QuanLyDatBanNhaHang.demo.entity.CaLamViec;
import com.QuanLyDatBanNhaHang.demo.entity.GiaoCa;
import com.QuanLyDatBanNhaHang.demo.entity.NhanVien;
import com.QuanLyDatBanNhaHang.demo.exception.ResourceNotFoundException;
import com.QuanLyDatBanNhaHang.demo.repository.CaLamViecRepository;
import com.QuanLyDatBanNhaHang.demo.repository.GiaoCaRepository;
import com.QuanLyDatBanNhaHang.demo.repository.NhanVienRepository;
import com.QuanLyDatBanNhaHang.demo.service.GiaoCaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GiaoCaServiceImpl implements GiaoCaService {

    private final GiaoCaRepository giaoCaRepository;
    private final NhanVienRepository nhanVienRepository;
    private final CaLamViecRepository caLamViecRepository;

    @Override
    public Page<GiaoCaResponseDTO> getAllGiaoCa(Pageable pageable) {
        return giaoCaRepository.findAllWithRelations(pageable).map(this::convertToResponseDTO);
    }

    @Override
    public GiaoCaResponseDTO getGiaoCaById(Long id) {
        GiaoCa g = giaoCaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Giao ca với ID: " + id));
        return convertToResponseDTO(g);
    }

    @Override
    public GiaoCaResponseDTO createGiaoCa(GiaoCaCreateRequestDTO requestDTO) {
        NhanVien nv = nhanVienRepository.findByMaNVIgnoreCase(requestDTO.getMaNV())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Nhân viên với mã: " + requestDTO.getMaNV()));
        CaLamViec ca = caLamViecRepository.findByMaCaIgnoreCase(requestDTO.getMaCa())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Ca với mã: " + requestDTO.getMaCa()));

        GiaoCa g = GiaoCa.builder()
                .nhanVien(nv)
                .caLamViec(ca)
                .thoiGianVaoCa(requestDTO.getThoiGianVaoCa())
                .tienBanDau(requestDTO.getTienBanDau())
                .trangThai(requestDTO.getTrangThai())
                .build();

        return convertToResponseDTO(giaoCaRepository.save(g));
    }

    @Override
    public GiaoCaResponseDTO updateGiaoCa(Long id, GiaoCaUpdateRequestDTO requestDTO) {
        GiaoCa g = giaoCaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Giao ca với ID: " + id));
                
        NhanVien nv = nhanVienRepository.findByMaNVIgnoreCase(requestDTO.getMaNV())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Nhân viên với mã: " + requestDTO.getMaNV()));
        CaLamViec ca = caLamViecRepository.findByMaCaIgnoreCase(requestDTO.getMaCa())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Ca với mã: " + requestDTO.getMaCa()));

        g.setNhanVien(nv);
        g.setCaLamViec(ca);
        g.setThoiGianVaoCa(requestDTO.getThoiGianVaoCa());
        g.setThoiGianKetCa(requestDTO.getThoiGianKetCa());
        g.setTienBanDau(requestDTO.getTienBanDau());
        g.setTienKetCa(requestDTO.getTienKetCa());
        g.setTienHeThong(requestDTO.getTienHeThong());
        g.setGhiChu(requestDTO.getGhiChu());
        g.setTrangThai(requestDTO.getTrangThai());

        return convertToResponseDTO(giaoCaRepository.save(g));
    }

    @Override
    public void deleteGiaoCa(Long id) {
        GiaoCa g = giaoCaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Giao ca với ID: " + id));
        giaoCaRepository.delete(g);
    }

    private GiaoCaResponseDTO convertToResponseDTO(GiaoCa g) {
        return GiaoCaResponseDTO.builder()
                .id(g.getId())
                .maNV(g.getNhanVien() != null ? g.getNhanVien().getMaNV() : null)
                .hoTenNV(g.getNhanVien() != null ? g.getNhanVien().getHoTen() : null)
                .maCa(g.getCaLamViec() != null ? g.getCaLamViec().getMaCa() : null)
                .tenCa(g.getCaLamViec() != null ? g.getCaLamViec().getTenCa() : null)
                .thoiGianVaoCa(g.getThoiGianVaoCa())
                .thoiGianKetCa(g.getThoiGianKetCa())
                .tienBanDau(g.getTienBanDau())
                .tienKetCa(g.getTienKetCa())
                .tienHeThong(g.getTienHeThong())
                .ghiChu(g.getGhiChu())
                .trangThai(g.getTrangThai())
                .build();
    }
}
