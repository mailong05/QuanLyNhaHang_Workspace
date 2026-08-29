package com.QuanLyDatBanNhaHang.demo.controller;

import com.QuanLyDatBanNhaHang.demo.dto.request.GiaoCaCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.GiaoCaUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.GiaoCaResponseDTO;
import com.QuanLyDatBanNhaHang.demo.service.GiaoCaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/giao-ca")
@RequiredArgsConstructor
public class GiaoCaController {

    private final GiaoCaService giaoCaService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<GiaoCaResponseDTO>>> getAllGiaoCa(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(giaoCaService.getAllGiaoCa(pageable)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<GiaoCaResponseDTO>> getGiaoCaById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(giaoCaService.getGiaoCaById(id)));
    }

    @PostMapping("/vao-ca")
    @PreAuthorize("hasAnyRole('ADMIN', 'NHAN_VIEN')")
    public ResponseEntity<ApiResponse<GiaoCaResponseDTO>> vaoCa(@Valid @RequestBody com.QuanLyDatBanNhaHang.demo.dto.request.VaoCaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(giaoCaService.vaoCa(request)));
    }

    @GetMapping("/hien-tai")
    @PreAuthorize("hasAnyRole('ADMIN', 'NHAN_VIEN')")
    public ResponseEntity<ApiResponse<GiaoCaResponseDTO>> getGiaoCaHienTai() {
        return ResponseEntity.ok(ApiResponse.success(giaoCaService.getGiaoCaHienTai()));
    }

    @PutMapping("/ket-ca")
    @PreAuthorize("hasAnyRole('ADMIN', 'NHAN_VIEN')")
    public ResponseEntity<ApiResponse<GiaoCaResponseDTO>> ketCa(@Valid @RequestBody com.QuanLyDatBanNhaHang.demo.dto.request.KetCaRequest request) {
        return ResponseEntity.ok(ApiResponse.success(giaoCaService.ketCa(request)));
    }
}
