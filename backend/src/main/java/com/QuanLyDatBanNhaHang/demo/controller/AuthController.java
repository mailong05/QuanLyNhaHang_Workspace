package com.QuanLyDatBanNhaHang.demo.controller;

import com.QuanLyDatBanNhaHang.demo.dto.request.LoginRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.JwtAuthResponseDTO;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

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
        // Lỗi AuthenticationException sẽ được GlobalExceptionHandler bắt và trả về 401 tự động.
    }
}
