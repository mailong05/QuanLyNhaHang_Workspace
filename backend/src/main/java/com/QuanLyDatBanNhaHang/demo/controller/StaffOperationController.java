package com.QuanLyDatBanNhaHang.demo.controller;

import com.QuanLyDatBanNhaHang.demo.dto.request.ChangeTableRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.MergeTableRequestDTO;
import com.QuanLyDatBanNhaHang.demo.service.StaffOperationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff/orders")
@RequiredArgsConstructor
public class StaffOperationController {

    private final StaffOperationService staffOperationService;

    @PostMapping("/{id}/change-table")
    public ResponseEntity<ApiResponse<String>> changeTable(@PathVariable Long id, @Valid @RequestBody ChangeTableRequestDTO request) {
        staffOperationService.changeTable(id, request);
        return ResponseEntity.ok(ApiResponse.success("Đổi bàn thành công!", null));
    }

    @PostMapping("/{id}/merge-tables")
    public ResponseEntity<ApiResponse<String>> mergeTables(@PathVariable Long id, @Valid @RequestBody MergeTableRequestDTO request) {
        staffOperationService.mergeTables(id, request);
        return ResponseEntity.ok(ApiResponse.success("Gộp phiếu đặt bàn thành công!", null));
    }
}
