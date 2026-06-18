import os

base_dir = r"c:\QuanLyDatBanNhaHangVerWeb\src\main\java\com\QuanLyDatBanNhaHang\demo"
dto_req_dir = os.path.join(base_dir, "dto", "request")
service_dir = os.path.join(base_dir, "service")
service_impl_dir = os.path.join(service_dir, "impl")
controller_dir = os.path.join(base_dir, "controller")
enum_dir = os.path.join(base_dir, "enums")

# 1. UPDATE ENUMS
enum_file = os.path.join(enum_dir, "TrangThaiBanAn.java")
with open(enum_file, "w", encoding="utf-8") as f:
    f.write("""package com.QuanLyDatBanNhaHang.demo.enums;

public enum TrangThaiBanAn {
    TRONG, DA_DAT, DAT_TRUOC, DANG_SUDUNG
}
""")

# 2. CREATE DTOs
dtos = {
    "WebBookingRequestDTO.java": """package com.QuanLyDatBanNhaHang.demo.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class WebBookingRequestDTO {
    @NotBlank
    @Size(max = 100)
    private String hoTen;

    @NotBlank
    @Size(max = 15)
    private String sdt;

    @Email
    @Size(max = 100)
    private String email;

    @NotNull
    @Future
    private LocalDateTime thoiGianDen;

    @Min(1)
    private Integer soLuongNguoi;

    private String ghiChu;

    @NotEmpty
    private List<Long> danhSachBanId;
}
""",
    "ChangeTableRequestDTO.java": """package com.QuanLyDatBanNhaHang.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeTableRequestDTO {
    @NotNull
    private Long oldBanId;

    @NotNull
    private Long newBanId;
}
""",
    "MergeTableRequestDTO.java": """package com.QuanLyDatBanNhaHang.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MergeTableRequestDTO {
    @NotNull
    private Long targetPhieuDatBanId;
}
"""
}
for name, content in dtos.items():
    with open(os.path.join(dto_req_dir, name), "w", encoding="utf-8") as f: f.write(content)

# 3. CREATE SERVICES INTERFACES
services = {
    "WebBookingService.java": """package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.dto.request.WebBookingRequestDTO;

public interface WebBookingService {
    void createWebBooking(WebBookingRequestDTO request);
}
""",
    "StaffOperationService.java": """package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.dto.request.ChangeTableRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.MergeTableRequestDTO;

public interface StaffOperationService {
    void changeTable(Long phieuDatBanId, ChangeTableRequestDTO request);
    void mergeTables(Long sourcePhieuDatBanId, MergeTableRequestDTO request);
}
"""
}
for name, content in services.items():
    with open(os.path.join(service_dir, name), "w", encoding="utf-8") as f: f.write(content)

# 4. CREATE SERVICE IMPL
service_impls = {
    "WebBookingServiceImpl.java": """package com.QuanLyDatBanNhaHang.demo.service.impl;

import com.QuanLyDatBanNhaHang.demo.dto.request.WebBookingRequestDTO;
import com.QuanLyDatBanNhaHang.demo.entity.*;
import com.QuanLyDatBanNhaHang.demo.enums.*;
import com.QuanLyDatBanNhaHang.demo.repository.*;
import com.QuanLyDatBanNhaHang.demo.service.WebBookingService;
import com.QuanLyDatBanNhaHang.demo.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WebBookingServiceImpl implements WebBookingService {

    private final KhachHangRepository khachHangRepository;
    private final PhieuDatBanRepository phieuDatBanRepository;
    private final BanAnRepository banAnRepository;
    private final ChiTietPhieuDatBanRepository chiTietPhieuDatBanRepository;

    private String generateNextMaPhieu() {
        Integer maxId = phieuDatBanRepository.findMaxId();
        int nextId = (maxId == null) ? 1 : maxId + 1;
        return String.format("PDB%06d", nextId);
    }
    
    private String generateNextMaKH() {
        Integer maxId = khachHangRepository.findMaxId();
        int nextId = (maxId == null) ? 1 : maxId + 1;
        return String.format("KH%06d", nextId);
    }

    @Override
    @Transactional
    public void createWebBooking(WebBookingRequestDTO request) {
        // 1. Tìm hoặc tạo Khách Hàng
        KhachHang khachHang = khachHangRepository.findAll().stream()
                .filter(kh -> request.getSdt().equals(kh.getSdt()))
                .findFirst()
                .orElseGet(() -> {
                    KhachHang newKh = KhachHang.builder()
                            .maKH(generateNextMaKH())
                            .hoTen(request.getHoTen())
                            .sdt(request.getSdt())
                            .email(request.getEmail())
                            .diemTichLuy(0)
                            .loaiThanhVien(LoaiThanhVienKhachHang.THUONG)
                            .build();
                    return khachHangRepository.save(newKh);
                });

        // 2. Validate chống trùng lịch bàn ăn
        LocalDateTime start = request.getThoiGianDen().minusHours(2);
        LocalDateTime end = request.getThoiGianDen().plusHours(2);

        for (Long banId : request.getDanhSachBanId()) {
            BanAn banAn = banAnRepository.findById(banId)
                    .orElseThrow(() -> new ResourceNotFoundException("Bàn ăn không tồn tại: " + banId));

            List<ChiTietPhieuDatBan> conflicts = chiTietPhieuDatBanRepository.findConflictingBookings(banId, start, end);
            if (!conflicts.isEmpty()) {
                throw new IllegalArgumentException("Bàn " + banAn.getMaBan() + " đã có người đặt trong khung giờ này.");
            }
        }

        // 3. Tạo Phiếu đặt bàn
        PhieuDatBan phieuDatBan = PhieuDatBan.builder()
                .maPhieuDat(generateNextMaPhieu())
                .ngayLapPhieu(LocalDateTime.now())
                .thoiGianDen(request.getThoiGianDen())
                .soLuongNguoi(request.getSoLuongNguoi())
                .ghiChu("Khách đặt qua Web. " + (request.getGhiChu() != null ? request.getGhiChu() : ""))
                .trangThai(TrangThaiPhieuDatBan.CHO_XAC_NHAN)
                .tienDatCoc(0.0)
                .khachHang(khachHang)
                .nhanVien(null) // Cho phép NULL vì khách tự đặt
                .build();

        PhieuDatBan savedPhieu = phieuDatBanRepository.save(phieuDatBan);

        // 4. Tạo ChiTietPhieuDatBan
        for (Long banId : request.getDanhSachBanId()) {
            BanAn banAn = banAnRepository.findById(banId).orElseThrow();
            
            ChiTietPhieuDatBan ct = ChiTietPhieuDatBan.builder()
                    .phieuDatBan(savedPhieu)
                    .banAn(banAn)
                    .ghiChu("")
                    .build();
            chiTietPhieuDatBanRepository.save(ct);
        }
    }
}
""",
    "StaffOperationServiceImpl.java": """package com.QuanLyDatBanNhaHang.demo.service.impl;

import com.QuanLyDatBanNhaHang.demo.dto.request.ChangeTableRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.MergeTableRequestDTO;
import com.QuanLyDatBanNhaHang.demo.entity.*;
import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiBanAn;
import com.QuanLyDatBanNhaHang.demo.enums.TrangThaiPhieuDatBan;
import com.QuanLyDatBanNhaHang.demo.repository.*;
import com.QuanLyDatBanNhaHang.demo.service.StaffOperationService;
import com.QuanLyDatBanNhaHang.demo.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffOperationServiceImpl implements StaffOperationService {

    private final PhieuDatBanRepository phieuDatBanRepository;
    private final ChiTietPhieuDatBanRepository chiTietPhieuDatBanRepository;
    private final BanAnRepository banAnRepository;

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    @Transactional
    public void changeTable(Long phieuDatBanId, ChangeTableRequestDTO request) {
        String auditUser = getCurrentUsername();

        PhieuDatBan phieu = phieuDatBanRepository.findById(phieuDatBanId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu đặt bàn"));

        BanAn oldBan = banAnRepository.findById(request.getOldBanId())
                .orElseThrow(() -> new ResourceNotFoundException("Bàn cũ không tồn tại"));

        BanAn newBan = banAnRepository.findById(request.getNewBanId())
                .orElseThrow(() -> new ResourceNotFoundException("Bàn mới không tồn tại"));

        // Validate bàn mới phải đang TRONG
        if (newBan.getTrangThai() != TrangThaiBanAn.TRONG) {
            throw new IllegalArgumentException("Bàn mới không khả dụng để chuyển!");
        }

        // Cập nhật trạng thái Bàn
        oldBan.setTrangThai(TrangThaiBanAn.TRONG);
        newBan.setTrangThai(TrangThaiBanAn.DANG_SUDUNG);
        banAnRepository.save(oldBan);
        banAnRepository.save(newBan);

        // Đổi ChiTietPhieuDatBan sang bàn mới
        ChiTietPhieuDatBan chiTiet = chiTietPhieuDatBanRepository.findAll().stream()
                .filter(ct -> ct.getPhieuDatBan().getId().equals(phieu.getId()) && ct.getBanAn().getId().equals(oldBan.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Phiếu đặt bàn này không sử dụng bàn cũ được chỉ định!"));

        chiTiet.setBanAn(newBan);
        chiTietPhieuDatBanRepository.save(chiTiet);

        // Ghi Audit Log vào ghiChu
        String log = "\\n[AUDIT] Nhân viên " + auditUser + " đổi bàn từ " + oldBan.getMaBan() + " sang " + newBan.getMaBan();
        phieu.setGhiChu((phieu.getGhiChu() == null ? "" : phieu.getGhiChu()) + log);
        phieuDatBanRepository.save(phieu);
    }

    @Override
    @Transactional
    public void mergeTables(Long sourcePhieuDatBanId, MergeTableRequestDTO request) {
        String auditUser = getCurrentUsername();

        if (sourcePhieuDatBanId.equals(request.getTargetPhieuDatBanId())) {
            throw new IllegalArgumentException("Không thể gộp phiếu vào chính nó!");
        }

        PhieuDatBan sourcePhieu = phieuDatBanRepository.findById(sourcePhieuDatBanId)
                .orElseThrow(() -> new ResourceNotFoundException("Phiếu nguồn không tồn tại"));
        
        PhieuDatBan targetPhieu = phieuDatBanRepository.findById(request.getTargetPhieuDatBanId())
                .orElseThrow(() -> new ResourceNotFoundException("Phiếu đích không tồn tại"));

        // Giải phóng các bàn của phiếu Nguồn (Set TRONG)
        List<ChiTietPhieuDatBan> sourceChiTiets = chiTietPhieuDatBanRepository.findAll().stream()
                .filter(ct -> ct.getPhieuDatBan().getId().equals(sourcePhieu.getId())).toList();

        for (ChiTietPhieuDatBan ct : sourceChiTiets) {
            BanAn ban = ct.getBanAn();
            ban.setTrangThai(TrangThaiBanAn.TRONG);
            banAnRepository.save(ban);
            
            // Xóa chi tiết bàn của phiếu nguồn
            chiTietPhieuDatBanRepository.delete(ct);
        }

        // Hủy phiếu nguồn
        sourcePhieu.setTrangThai(TrangThaiPhieuDatBan.DA_HUY);
        String logSource = "\\n[AUDIT] Nhân viên " + auditUser + " gộp phiếu này vào phiếu " + targetPhieu.getMaPhieuDat();
        sourcePhieu.setGhiChu((sourcePhieu.getGhiChu() == null ? "" : sourcePhieu.getGhiChu()) + logSource);
        phieuDatBanRepository.save(sourcePhieu);

        // Log trên phiếu đích
        String logTarget = "\\n[AUDIT] Nhân viên " + auditUser + " gộp phiếu " + sourcePhieu.getMaPhieuDat() + " vào phiếu này.";
        targetPhieu.setGhiChu((targetPhieu.getGhiChu() == null ? "" : targetPhieu.getGhiChu()) + logTarget);
        phieuDatBanRepository.save(targetPhieu);
    }
}
"""
}
for name, content in service_impls.items():
    with open(os.path.join(service_impl_dir, name), "w", encoding="utf-8") as f: f.write(content)

# 5. CREATE CONTROLLERS
controllers = {
    "WebBookingController.java": """package com.QuanLyDatBanNhaHang.demo.controller;

import com.QuanLyDatBanNhaHang.demo.dto.request.WebBookingRequestDTO;
import com.QuanLyDatBanNhaHang.demo.service.WebBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/web/booking")
@RequiredArgsConstructor
public class WebBookingController {

    private final WebBookingService webBookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody WebBookingRequestDTO request) {
        webBookingService.createWebBooking(request);
        return ResponseEntity.ok("Đặt bàn thành công! Hệ thống đang xử lý và chờ xác nhận.");
    }
}
""",
    "StaffOperationController.java": """package com.QuanLyDatBanNhaHang.demo.controller;

import com.QuanLyDatBanNhaHang.demo.dto.request.ChangeTableRequestDTO;
import com.QuanLyDatBanNhaHang.demo.dto.request.MergeTableRequestDTO;
import com.QuanLyDatBanNhaHang.demo.service.StaffOperationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff/orders")
@RequiredArgsConstructor
public class StaffOperationController {

    private final StaffOperationService staffOperationService;

    @PostMapping("/{id}/change-table")
    public ResponseEntity<?> changeTable(@PathVariable Long id, @Valid @RequestBody ChangeTableRequestDTO request) {
        staffOperationService.changeTable(id, request);
        return ResponseEntity.ok("Đổi bàn thành công!");
    }

    @PostMapping("/{id}/merge-tables")
    public ResponseEntity<?> mergeTables(@PathVariable Long id, @Valid @RequestBody MergeTableRequestDTO request) {
        staffOperationService.mergeTables(id, request);
        return ResponseEntity.ok("Gộp phiếu đặt bàn thành công!");
    }
}
"""
}
for name, content in controllers.items():
    with open(os.path.join(controller_dir, name), "w", encoding="utf-8") as f: f.write(content)

print("Phase 3.2 completed.")
