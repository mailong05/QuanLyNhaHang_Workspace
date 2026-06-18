package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.dto.request.ChangeTableRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.MergeTableRequestDTO;

public interface StaffOperationService {
    void changeTable(Long phieuDatBanId, ChangeTableRequestDTO request);
    void mergeTables(Long sourcePhieuDatBanId, MergeTableRequestDTO request);
}
