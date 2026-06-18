package com.QuanLyDatBanNhaHang.demo.controller;

import com.QuanLyDatBanNhaHang.demo.dto.request.MonAnCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.MonAnUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.MonAnResponseDTO;
import com.QuanLyDatBanNhaHang.demo.service.MonAnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mon-an")
@RequiredArgsConstructor
public class MonAnController {

    private final MonAnService monAnService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<MonAnResponseDTO>>> getAllMonAn(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(monAnService.getAllMonAn(pageable)));
    }

    @GetMapping("/{maMon}")
    public ResponseEntity<ApiResponse<MonAnResponseDTO>> getMonAnById(@PathVariable String maMon) {
        return ResponseEntity.ok(ApiResponse.success(monAnService.getMonAnByMa(maMon)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MonAnResponseDTO>> createMonAn(@Valid @RequestBody MonAnCreateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(monAnService.createMonAn(requestDTO)));
    }

    @PutMapping("/{maMon}")
    public ResponseEntity<ApiResponse<MonAnResponseDTO>> updateMonAn(@PathVariable String maMon, @Valid @RequestBody MonAnUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(ApiResponse.success(monAnService.updateMonAn(maMon, requestDTO)));
    }

    @DeleteMapping("/{maMon}")
    public ResponseEntity<ApiResponse<Void>> deleteMonAn(@PathVariable String maMon) {
        monAnService.deleteMonAn(maMon);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}
