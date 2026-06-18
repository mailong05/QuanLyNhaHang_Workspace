package com.QuanLyDatBanNhaHang.demo.controller;

import com.QuanLyDatBanNhaHang.demo.dto.request.HoaDonCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.HoaDonUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.HoaDonResponseDTO;
import com.QuanLyDatBanNhaHang.demo.service.HoaDonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hoa-don")
@RequiredArgsConstructor
public class HoaDonController {

    private final HoaDonService hoaDonService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<HoaDonResponseDTO>>> getAllHoaDon(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(hoaDonService.getAllHoaDon(pageable)));
    }

    @GetMapping("/{maHD}")
    public ResponseEntity<ApiResponse<HoaDonResponseDTO>> getHoaDonById(@PathVariable String maHD) {
        return ResponseEntity.ok(ApiResponse.success(hoaDonService.getHoaDonByMa(maHD)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HoaDonResponseDTO>> createHoaDon(@Valid @RequestBody HoaDonCreateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(hoaDonService.createHoaDon(requestDTO)));
    }

    @PutMapping("/{maHD}")
    public ResponseEntity<ApiResponse<HoaDonResponseDTO>> updateHoaDon(@PathVariable String maHD, @Valid @RequestBody HoaDonUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(ApiResponse.success(hoaDonService.updateHoaDon(maHD, requestDTO)));
    }

    @DeleteMapping("/{maHD}")
    public ResponseEntity<ApiResponse<Void>> deleteHoaDon(@PathVariable String maHD) {
        hoaDonService.deleteHoaDon(maHD);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}
