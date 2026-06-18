package com.QuanLyDatBanNhaHang.demo.controller;

import com.QuanLyDatBanNhaHang.demo.dto.request.ThueCreateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.ThueUpdateRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.response.ThueResponseDTO;
import com.QuanLyDatBanNhaHang.demo.service.ThueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/thue")
@RequiredArgsConstructor
public class ThueController {

    private final ThueService thueService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ThueResponseDTO>>> getAllThue(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(thueService.getAllThue(pageable)));
    }

    @GetMapping("/{maThue}")
    public ResponseEntity<ApiResponse<ThueResponseDTO>> getThueById(@PathVariable String maThue) {
        return ResponseEntity.ok(ApiResponse.success(thueService.getThueByMa(maThue)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ThueResponseDTO>> createThue(@Valid @RequestBody ThueCreateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(thueService.createThue(requestDTO)));
    }

    @PutMapping("/{maThue}")
    public ResponseEntity<ApiResponse<ThueResponseDTO>> updateThue(@PathVariable String maThue, @Valid @RequestBody ThueUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(ApiResponse.success(thueService.updateThue(maThue, requestDTO)));
    }

    @DeleteMapping("/{maThue}")
    public ResponseEntity<ApiResponse<Void>> deleteThue(@PathVariable String maThue) {
        thueService.deleteThue(maThue);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}
