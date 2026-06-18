package com.QuanLyDatBanNhaHang.demo.service.impl;

import com.QuanLyDatBanNhaHang.demo.dto.request.PhanCongCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.PhanCongUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.PhanCongResponseDTO;
import com.QuanLyDatBanNhaHang.demo.entity.CaLamViec;
import com.QuanLyDatBanNhaHang.demo.entity.NhanVien;
import com.QuanLyDatBanNhaHang.demo.entity.PhanCong;
import com.QuanLyDatBanNhaHang.demo.exception.ResourceNotFoundException;
import com.QuanLyDatBanNhaHang.demo.repository.CaLamViecRepository;
import com.QuanLyDatBanNhaHang.demo.repository.NhanVienRepository;
import com.QuanLyDatBanNhaHang.demo.repository.PhanCongRepository;
import com.QuanLyDatBanNhaHang.demo.service.PhanCongService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhanCongServiceImpl implements PhanCongService {

    private final PhanCongRepository phanCongRepository;
    private final CaLamViecRepository caLamViecRepository;
    private final NhanVienRepository nhanVienRepository;

    @Override
    public Page<PhanCongResponseDTO> getAllPhanCong(Pageable pageable) {
        return phanCongRepository.findAllWithRelations(pageable).map(this::convertToResponseDTO);
    }

    @Override
    public PhanCongResponseDTO getPhanCongById(Long id) {
        PhanCong pc = phanCongRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Phân Công với ID: " + id));
        return convertToResponseDTO(pc);
    }

    @Override
    public PhanCongResponseDTO createPhanCong(PhanCongCreateRequestDTO requestDTO) {
        CaLamViec ca = caLamViecRepository.findByMaCaIgnoreCase(requestDTO.getMaCa())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Ca với mã: " + requestDTO.getMaCa()));
        NhanVien nv = nhanVienRepository.findByMaNVIgnoreCase(requestDTO.getMaNV())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Nhân viên với mã: " + requestDTO.getMaNV()));

        PhanCong pc = PhanCong.builder()
                .caLamViec(ca)
                .nhanVien(nv)
                .ngayLam(requestDTO.getNgayLam())
                .trangThai(requestDTO.getTrangThai())
                .build();
        
        return convertToResponseDTO(phanCongRepository.save(pc));
    }

    @Override
    public PhanCongResponseDTO updatePhanCong(Long id, PhanCongUpdateRequestDTO requestDTO) {
        PhanCong pc = phanCongRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Phân Công với ID: " + id));
                
        CaLamViec ca = caLamViecRepository.findByMaCaIgnoreCase(requestDTO.getMaCa())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Ca với mã: " + requestDTO.getMaCa()));
        NhanVien nv = nhanVienRepository.findByMaNVIgnoreCase(requestDTO.getMaNV())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Nhân viên với mã: " + requestDTO.getMaNV()));

        pc.setCaLamViec(ca);
        pc.setNhanVien(nv);
        pc.setNgayLam(requestDTO.getNgayLam());
        pc.setTrangThai(requestDTO.getTrangThai());

        return convertToResponseDTO(phanCongRepository.save(pc));
    }

    @Override
    public void deletePhanCong(Long id) {
        PhanCong pc = phanCongRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Phân Công với ID: " + id));
        phanCongRepository.delete(pc);
    }

    private PhanCongResponseDTO convertToResponseDTO(PhanCong pc) {
        return PhanCongResponseDTO.builder()
                .id(pc.getId())
                .maCa(pc.getCaLamViec() != null ? pc.getCaLamViec().getMaCa() : null)
                .tenCa(pc.getCaLamViec() != null ? pc.getCaLamViec().getTenCa() : null)
                .maNV(pc.getNhanVien() != null ? pc.getNhanVien().getMaNV() : null)
                .hoTenNV(pc.getNhanVien() != null ? pc.getNhanVien().getHoTen() : null)
                .ngayLam(pc.getNgayLam())
                .trangThai(pc.getTrangThai())
                .build();
    }
}
