package com.QuanLyDatBanNhaHang.demo.service.impl;

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
        String log = "\n[AUDIT] Nhân viên " + auditUser + " đổi bàn từ " + oldBan.getMaBan() + " sang " + newBan.getMaBan();
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
        String logSource = "\n[AUDIT] Nhân viên " + auditUser + " gộp phiếu này vào phiếu " + targetPhieu.getMaPhieuDat();
        sourcePhieu.setGhiChu((sourcePhieu.getGhiChu() == null ? "" : sourcePhieu.getGhiChu()) + logSource);
        phieuDatBanRepository.save(sourcePhieu);

        // Log trên phiếu đích
        String logTarget = "\n[AUDIT] Nhân viên " + auditUser + " gộp phiếu " + sourcePhieu.getMaPhieuDat() + " vào phiếu này.";
        targetPhieu.setGhiChu((targetPhieu.getGhiChu() == null ? "" : targetPhieu.getGhiChu()) + logTarget);
        phieuDatBanRepository.save(targetPhieu);
    }
}
