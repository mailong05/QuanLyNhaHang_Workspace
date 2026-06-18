package com.QuanLyDatBanNhaHang.demo.controller;

import com.QuanLyDatBanNhaHang.demo.dto.request.CaLamViecCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.CaLamViecUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.CaLamViecResponseDTO;
import com.QuanLyDatBanNhaHang.demo.service.CaLamViecService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ca-lam-viec")
@RequiredArgsConstructor
public class CaLamViecController {

    private final CaLamViecService caLamViecService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CaLamViecResponseDTO>>> getAllCaLamViec(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(caLamViecService.getAllCaLamViec(pageable)));
    }

    @GetMapping("/{maCa}")
    public ResponseEntity<ApiResponse<CaLamViecResponseDTO>> getCaLamViecById(@PathVariable String maCa) {
        return ResponseEntity.ok(ApiResponse.success(caLamViecService.getCaLamViecByMa(maCa)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CaLamViecResponseDTO>> createCaLamViec(@Valid @RequestBody CaLamViecCreateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(caLamViecService.createCaLamViec(requestDTO)));
    }

    @PutMapping("/{maCa}")
    public ResponseEntity<ApiResponse<CaLamViecResponseDTO>> updateCaLamViec(@PathVariable String maCa, @Valid @RequestBody CaLamViecUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(ApiResponse.success(caLamViecService.updateCaLamViec(maCa, requestDTO)));
    }

    @DeleteMapping("/{maCa}")
    public ResponseEntity<ApiResponse<Void>> deleteCaLamViec(@PathVariable String maCa) {
        caLamViecService.deleteCaLamViec(maCa);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}
