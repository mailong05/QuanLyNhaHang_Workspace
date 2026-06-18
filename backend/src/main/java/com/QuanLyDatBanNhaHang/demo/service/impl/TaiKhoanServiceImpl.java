package com.QuanLyDatBanNhaHang.demo.service.impl;

import com.QuanLyDatBanNhaHang.demo.dto.request.TaiKhoanCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.TaiKhoanUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.TaiKhoanResponseDTO;
import com.QuanLyDatBanNhaHang.demo.entity.TaiKhoan;
import com.QuanLyDatBanNhaHang.demo.exception.DuplicateResourceException;
import com.QuanLyDatBanNhaHang.demo.exception.ResourceNotFoundException;
import com.QuanLyDatBanNhaHang.demo.repository.TaiKhoanRepository;
import com.QuanLyDatBanNhaHang.demo.service.TaiKhoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaiKhoanServiceImpl implements TaiKhoanService {

    private final TaiKhoanRepository taiKhoanRepository;

    @Override
    public Page<TaiKhoanResponseDTO> getAllTaiKhoan(Pageable pageable) {
        return taiKhoanRepository.findAll(pageable).map(this::convertToResponseDTO);
    }

    @Override
    public TaiKhoanResponseDTO getTaiKhoanByUsername(String username) {
        TaiKhoan tk = taiKhoanRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Tài khoản với username: " + username));
        return convertToResponseDTO(tk);
    }

    @Override
    public TaiKhoanResponseDTO createTaiKhoan(TaiKhoanCreateRequestDTO requestDTO) {
        if (taiKhoanRepository.findByUsernameIgnoreCase(requestDTO.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username đã tồn tại");
        }
        
        TaiKhoan tk = TaiKhoan.builder()
                .username(requestDTO.getUsername())
                .password(requestDTO.getPassword()) // Trong thực tế cần encode (VD: BCrypt)
                .quyenHan(requestDTO.getQuyenHan())
                .build();
                
        return convertToResponseDTO(taiKhoanRepository.save(tk));
    }

    @Override
    public TaiKhoanResponseDTO updateTaiKhoan(String username, TaiKhoanUpdateRequestDTO requestDTO) {
        TaiKhoan tk = taiKhoanRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Tài khoản với username: " + username));
                
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().trim().isEmpty()) {
            tk.setPassword(requestDTO.getPassword());
        }
        tk.setQuyenHan(requestDTO.getQuyenHan());
        
        return convertToResponseDTO(taiKhoanRepository.save(tk));
    }

    @Override
    public void deleteTaiKhoan(String username) {
        TaiKhoan tk = taiKhoanRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Tài khoản với username: " + username));
        taiKhoanRepository.delete(tk);
    }

    private TaiKhoanResponseDTO convertToResponseDTO(TaiKhoan tk) {
        return TaiKhoanResponseDTO.builder()
                .id(tk.getId())
                .username(tk.getUsername())
                .quyenHan(tk.getQuyenHan())
                .build();
    }
}
