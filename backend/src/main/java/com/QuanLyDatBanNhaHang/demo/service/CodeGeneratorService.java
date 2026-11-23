package com.QuanLyDatBanNhaHang.demo.service;

import com.QuanLyDatBanNhaHang.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CodeGeneratorService {

    private final PhieuDatBanRepository phieuDatBanRepository;
    private final KhachHangRepository khachHangRepository;
    private final NhanVienRepository nhanVienRepository;
    private final HoaDonRepository hoaDonRepository;
    private final BanAnRepository banAnRepository;

    private static final Object lockPDB = new Object();
    private static final Object lockKH = new Object();
    private static final Object lockNV = new Object();
    private static final Object lockHD = new Object();
    private static final Object lockBA = new Object();

    public String generateNextMaPhieuDat() {
        synchronized (lockPDB) {
            Integer maxId = phieuDatBanRepository.findMaxMaPhieuDat();
            int nextId = (maxId == null) ? 1 : maxId + 1;
            return String.format("PDB%06d", nextId);
        }
    }

    public String generateNextMaKH() {
        synchronized (lockKH) {
            Integer maxId = khachHangRepository.findMaxMaKH();
            int nextId = (maxId == null) ? 1 : maxId + 1;
            return String.format("KH%06d", nextId);
        }
    }

    public String generateNextMaNV() {
        synchronized (lockNV) {
            Integer maxId = nhanVienRepository.findMaxMaNV();
            int nextId = (maxId == null) ? 1 : maxId + 1;
            return String.format("NV%06d", nextId);
        }
    }

    public String generateNextMaHD() {
        synchronized (lockHD) {
            Integer maxId = hoaDonRepository.findMaxMaHD();
            int nextId = (maxId == null) ? 1 : maxId + 1;
            return String.format("HD%06d", nextId);
        }
    }

    public String generateNextMaBan() {
        synchronized (lockBA) {
            Integer maxId = banAnRepository.findMaxMaBan();
            int nextId = (maxId == null) ? 1 : maxId + 1;
            return String.format("BA%06d", nextId);
        }
    }
}
