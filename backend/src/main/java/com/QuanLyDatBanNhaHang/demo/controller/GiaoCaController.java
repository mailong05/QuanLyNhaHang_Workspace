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
    public ResponseEntity<ApiResponse<GiaoCaResponseDTO>> getGiaoCaById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(giaoCaService.getGiaoCaById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GiaoCaResponseDTO>> createGiaoCa(@Valid @RequestBody GiaoCaCreateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(giaoCaService.createGiaoCa(requestDTO)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GiaoCaResponseDTO>> updateGiaoCa(@PathVariable Long id, @Valid @RequestBody GiaoCaUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(ApiResponse.success(giaoCaService.updateGiaoCa(id, requestDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGiaoCa(@PathVariable Long id) {
        giaoCaService.deleteGiaoCa(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}
