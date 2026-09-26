package com.QuanLyDatBanNhaHang.demo.service.impl;

import com.QuanLyDatBanNhaHang.demo.dto.request.NhanVienCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.NhanVienUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.NhanVienResponseDTO;
import com.QuanLyDatBanNhaHang.demo.entity.NhanVien;
import com.QuanLyDatBanNhaHang.demo.entity.TaiKhoan;
import com.QuanLyDatBanNhaHang.demo.exception.DuplicateResourceException;
import com.QuanLyDatBanNhaHang.demo.exception.ResourceNotFoundException;
import com.QuanLyDatBanNhaHang.demo.repository.NhanVienRepository;
import com.QuanLyDatBanNhaHang.demo.repository.TaiKhoanRepository;
import com.QuanLyDatBanNhaHang.demo.service.NhanVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.QuanLyDatBanNhaHang.demo.enums.QuyenHanTaiKhoan;

@Service
@RequiredArgsConstructor
public class NhanVienServiceImpl implements NhanVienService {

    private final NhanVienRepository nhanVienRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<NhanVienResponseDTO> getAllNhanVien(Pageable pageable) {
        return nhanVienRepository.findAllWithRelations(pageable).map(this::convertToResponseDTO);
    }

    @Override
    public Page<NhanVienResponseDTO> searchNhanVien(String keyword, Pageable pageable) {
        return nhanVienRepository.searchByHoTenOrSdt(keyword, pageable).map(this::convertToResponseDTO);
    }

    @Override
    public NhanVienResponseDTO getNhanVienByMa(String maNV) {
        NhanVien nv = nhanVienRepository.findByMaNVIgnoreCaseWithAuth(maNV)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Nhân viên với mã: " + maNV));
        return convertToResponseDTO(nv);
    }

    @Override
    public NhanVienResponseDTO createNhanVien(NhanVienCreateRequestDTO requestDTO) {

        TaiKhoan tk = null;
        if (requestDTO.getUsername() != null && !requestDTO.getUsername().isBlank()) {
            if (taiKhoanRepository.findByUsernameIgnoreCase(requestDTO.getUsername()).isPresent()) {
                throw new DuplicateResourceException("Tên đăng nhập đã tồn tại trong hệ thống");
            }
            if (requestDTO.getPassword() == null || requestDTO.getPassword().isBlank()) {
                throw new IllegalArgumentException("Mật khẩu không được để trống khi tạo tài khoản");
            }
            tk = new TaiKhoan();
            tk.setUsername(requestDTO.getUsername());
            tk.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
            tk.setQuyenHan(QuyenHanTaiKhoan.NHAN_VIEN);
            tk = taiKhoanRepository.save(tk);
        }

        NhanVien nv = NhanVien.builder()
                .maNV(generateNextMaNV())
                .hoTen(requestDTO.getHoTen())
                .sdt(requestDTO.getSdt())
                .email(requestDTO.getEmail())
                .chucVu(requestDTO.getChucVu())
                .ngayVaoLam(requestDTO.getNgayVaoLam())
                .luongCoBan(requestDTO.getLuongCoBan())
                .trangThai(requestDTO.getTrangThai())
                .taiKhoan(tk)
                .build();
        
        return convertToResponseDTO(nhanVienRepository.save(nv));
    }

    @Override
    public NhanVienResponseDTO updateNhanVien(String maNV, NhanVienUpdateRequestDTO requestDTO) {
        NhanVien nv = nhanVienRepository.findByMaNVIgnoreCaseWithAuth(maNV)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Nhân viên với mã: " + maNV));

        TaiKhoan tk = nv.getTaiKhoan();
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isBlank()) {
            if (tk != null) {
                tk.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
                taiKhoanRepository.save(tk);
            } else {
                if (requestDTO.getUsername() != null && !requestDTO.getUsername().isBlank()) {
                    if (taiKhoanRepository.findByUsernameIgnoreCase(requestDTO.getUsername()).isPresent()) {
                        throw new DuplicateResourceException("Tên đăng nhập đã tồn tại trong hệ thống");
                    }
                    tk = new TaiKhoan();
                    tk.setUsername(requestDTO.getUsername());
                    tk.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
                    tk.setQuyenHan(QuyenHanTaiKhoan.NHAN_VIEN);
                    tk = taiKhoanRepository.save(tk);
                }
            }
        }

        nv.setHoTen(requestDTO.getHoTen());
        nv.setSdt(requestDTO.getSdt());
        nv.setEmail(requestDTO.getEmail());
        nv.setChucVu(requestDTO.getChucVu());
        nv.setNgayVaoLam(requestDTO.getNgayVaoLam());
        nv.setLuongCoBan(requestDTO.getLuongCoBan());
        nv.setTrangThai(requestDTO.getTrangThai());
        nv.setTaiKhoan(tk);

        return convertToResponseDTO(nhanVienRepository.save(nv));
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void deleteNhanVien(String maNV) {
        NhanVien nv = nhanVienRepository.findByMaNVIgnoreCase(maNV)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Nhân viên với mã: " + maNV));
                
        if (nv.getMaNV() != null && !nv.getMaNV().contains("_deleted_")) {
            nv.setMaNV(nv.getMaNV() + "_deleted_" + System.currentTimeMillis());
            nhanVienRepository.saveAndFlush(nv);
        }
        
        nhanVienRepository.delete(nv);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void restoreNhanVien(Long id) {
        NhanVien entity = nhanVienRepository.findDeletedById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy dữ liệu đã xóa với ID: " + id));

        entity.setDeletedAt(null);
        String originalCode = entity.getMaNV().replaceAll("_deleted_.*", "");
        
        if (nhanVienRepository.existsByMaNV(originalCode)) {
            entity.setMaNV(originalCode + "_restored_" + System.currentTimeMillis());
        } else {
            entity.setMaNV(originalCode);
        }
        
        nhanVienRepository.save(entity);
    }

    private NhanVienResponseDTO convertToResponseDTO(NhanVien nv) {
        return NhanVienResponseDTO.builder()
                .id(nv.getId())
                .maNV(nv.getMaNV())
                .hoTen(nv.getHoTen())
                .sdt(nv.getSdt())
                .email(nv.getEmail())
                .chucVu(nv.getChucVu())
                .ngayVaoLam(nv.getNgayVaoLam())
                .luongCoBan(nv.getLuongCoBan())
                .trangThai(nv.getTrangThai())
                .username(nv.getTaiKhoan() != null ? nv.getTaiKhoan().getUsername() : null)
                .quyenHan(nv.getTaiKhoan() != null ? nv.getTaiKhoan().getQuyenHan().name() : null)
                .build();
    }

    private String generateNextMaNV() {
        Integer maxMa = nhanVienRepository.findMaxMaNV();
        if (maxMa == null) {
            return String.format("NV%06d", 1);
        }
        return String.format("NV%06d", maxMa + 1);
    }
}
