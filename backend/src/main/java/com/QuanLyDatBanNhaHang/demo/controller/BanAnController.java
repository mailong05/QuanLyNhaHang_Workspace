package com.QuanLyDatBanNhaHang.demo.controller;

import com.QuanLyDatBanNhaHang.demo.dto.request.BanAnCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.BanAnUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.BanAnResponseDTO;
import com.QuanLyDatBanNhaHang.demo.service.BanAnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ban-an")
@RequiredArgsConstructor
public class BanAnController {

    private final BanAnService banAnService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BanAnResponseDTO>>> getAllBanAn(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(banAnService.getAllBanAn(pageable)));
    }

    @GetMapping("/{maBan}")
    public ResponseEntity<ApiResponse<BanAnResponseDTO>> getBanAnById(@PathVariable String maBan) {
        return ResponseEntity.ok(ApiResponse.success(banAnService.getBanAnByMa(maBan)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BanAnResponseDTO>> createBanAn(@Valid @RequestBody BanAnCreateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(banAnService.createBanAn(requestDTO)));
    }

    @PutMapping("/{maBan}")
    public ResponseEntity<ApiResponse<BanAnResponseDTO>> updateBanAn(@PathVariable String maBan, @Valid @RequestBody BanAnUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(ApiResponse.success(banAnService.updateBanAn(maBan, requestDTO)));
    }

    @DeleteMapping("/{maBan}")
    public ResponseEntity<ApiResponse<Void>> deleteBanAn(@PathVariable String maBan) {
        banAnService.deleteBanAn(maBan);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}
