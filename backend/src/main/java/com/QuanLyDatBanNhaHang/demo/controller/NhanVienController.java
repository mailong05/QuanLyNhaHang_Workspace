package com.QuanLyDatBanNhaHang.demo.controller;

import com.QuanLyDatBanNhaHang.demo.dto.request.NhanVienCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.NhanVienUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.NhanVienResponseDTO;
import com.QuanLyDatBanNhaHang.demo.service.NhanVienService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/nhan-vien")
@RequiredArgsConstructor
public class NhanVienController {

    private final NhanVienService nhanVienService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NhanVienResponseDTO>>> getAllNhanVien(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        if (search != null && !search.trim().isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success(nhanVienService.searchNhanVien(search, pageable)));
        }
        return ResponseEntity.ok(ApiResponse.success(nhanVienService.getAllNhanVien(pageable)));
    }

    @GetMapping("/{maNV}")
    public ResponseEntity<ApiResponse<NhanVienResponseDTO>> getNhanVienById(@PathVariable String maNV) {
        return ResponseEntity.ok(ApiResponse.success(nhanVienService.getNhanVienByMa(maNV)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NhanVienResponseDTO>> createNhanVien(@Valid @RequestBody NhanVienCreateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(nhanVienService.createNhanVien(requestDTO)));
    }

    @PutMapping("/{maNV}")
    public ResponseEntity<ApiResponse<NhanVienResponseDTO>> updateNhanVien(@PathVariable String maNV, @Valid @RequestBody NhanVienUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(ApiResponse.success(nhanVienService.updateNhanVien(maNV, requestDTO)));
    }

    @DeleteMapping("/{maNV}")
    public ResponseEntity<ApiResponse<Void>> deleteNhanVien(@PathVariable String maNV) {
        nhanVienService.deleteNhanVien(maNV);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}
