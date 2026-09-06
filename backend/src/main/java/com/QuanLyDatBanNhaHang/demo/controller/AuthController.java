package com.QuanLyDatBanNhaHang.demo.controller;

import com.QuanLyDatBanNhaHang.demo.dto.request.LoginRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.ResetPasswordRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.VerifyAccountRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.JwtAuthResponseDTO;
import com.QuanLyDatBanNhaHang.demo.entity.TaiKhoan;
import com.QuanLyDatBanNhaHang.demo.entity.NhanVien;
import com.QuanLyDatBanNhaHang.demo.entity.KhachHang;
import com.QuanLyDatBanNhaHang.demo.repository.TaiKhoanRepository;
import com.QuanLyDatBanNhaHang.demo.repository.NhanVienRepository;
import com.QuanLyDatBanNhaHang.demo.repository.KhachHangRepository;
import com.QuanLyDatBanNhaHang.demo.security.CustomUserDetails;
import com.QuanLyDatBanNhaHang.demo.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final TaiKhoanRepository taiKhoanRepository;
    private final NhanVienRepository nhanVienRepository;
    private final KhachHangRepository khachHangRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtAuthResponseDTO>> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        JwtAuthResponseDTO responseDTO = JwtAuthResponseDTO.builder()
                .accessToken(jwt)
                .username(userDetails.getUsername())
                .role(userDetails.getTaiKhoan().getQuyenHan().name())
                .build();

        return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công", responseDTO));
    }
    
    @PostMapping("/verify-account")
    public ResponseEntity<ApiResponse<Void>> verifyAccount(@Valid @RequestBody VerifyAccountRequestDTO request) {
        boolean matched = checkAccountMatch(request.getUsername(), request.getEmailOrPhone());
        if (!matched) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, "Không tìm thấy thông tin tài khoản hoặc số điện thoại/email không khớp."));
        }
        return ResponseEntity.ok(ApiResponse.success("Thông tin hợp lệ", null));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody ResetPasswordRequestDTO request) {
        boolean matched = checkAccountMatch(request.getUsername(), request.getEmailOrPhone());
        if (!matched) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, "Không tìm thấy thông tin tài khoản hoặc số điện thoại/email không khớp."));
        }

        TaiKhoan taiKhoan = taiKhoanRepository.findByUsernameIgnoreCase(request.getUsername()).get();
        taiKhoan.setPassword(passwordEncoder.encode(request.getNewPassword()));
        taiKhoanRepository.save(taiKhoan);

        return ResponseEntity.ok(ApiResponse.success("Đổi mật khẩu thành công!", null));
    }
    
    private boolean checkAccountMatch(String username, String emailOrPhone) {
        Optional<TaiKhoan> optionalTaiKhoan = taiKhoanRepository.findByUsernameIgnoreCase(username);
        if (optionalTaiKhoan.isEmpty()) {
            return false;
        }

        TaiKhoan taiKhoan = optionalTaiKhoan.get();
        
        Optional<NhanVien> optNv = nhanVienRepository.findByTaiKhoanUsername(taiKhoan.getUsername());
        if (optNv.isPresent()) {
            NhanVien nv = optNv.get();
            if ((nv.getSdt() != null && nv.getSdt().equals(emailOrPhone)) || 
                (nv.getEmail() != null && nv.getEmail().equals(emailOrPhone))) {
                return true;
            }
        }

        Optional<KhachHang> optKh = khachHangRepository.findByTaiKhoanUsername(taiKhoan.getUsername());
        if (optKh.isPresent()) {
            KhachHang kh = optKh.get();
            if ((kh.getSdt() != null && kh.getSdt().equals(emailOrPhone)) || 
                (kh.getEmail() != null && kh.getEmail().equals(emailOrPhone))) {
                return true;
            }
        }

        return false;
    }
}
