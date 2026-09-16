import os
import re

path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/service/impl/PhieuDatBanServiceImpl.java'
with open(path, 'r', encoding='utf-8') as f: c = f.read()

# Add ThueRepository import
if 'import com.QuanLyDatBanNhaHang.demo.repository.ThueRepository;' not in c:
    c = c.replace('import com.QuanLyDatBanNhaHang.demo.repository.PhieuDatBanRepository;', 'import com.QuanLyDatBanNhaHang.demo.repository.PhieuDatBanRepository;\nimport com.QuanLyDatBanNhaHang.demo.repository.ThueRepository;')

# Inject ThueRepository
if 'private final ThueRepository thueRepository;' not in c:
    c = c.replace('private final com.QuanLyDatBanNhaHang.demo.repository.HoaDonRepository hoaDonRepository;', 'private final com.QuanLyDatBanNhaHang.demo.repository.HoaDonRepository hoaDonRepository;\n    private final ThueRepository thueRepository;')

# Fix the HoaDon builder to include Thue
old_builder = """                NhanVien nv = pdb.getNhanVien();
                if (nv == null) {
                    nv = nhanVienRepository.findAll().stream().findFirst().orElse(null);
                }
                HoaDon newInvoice = HoaDon.builder()
                        .maHD("HD_" + System.currentTimeMillis())
                        .phieuDatBan(pdb)
                        .nhanVien(nv)
                        .ngayTao(java.time.LocalDateTime.now())
                        .gioVao(java.time.LocalTime.now())
                        .trangThaiThanhToan(com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThanhToanHoaDon.CHUA_THANH_TOAN)
                        .thueSuat(new java.math.BigDecimal("8.00"))
                        .tienThue(java.math.BigDecimal.ZERO)
                        .tienPhiDV(java.math.BigDecimal.ZERO)
                        .tongTienGoc(java.math.BigDecimal.ZERO)
                        .tienGiamGia(java.math.BigDecimal.ZERO)
                        .tongThanhToan(java.math.BigDecimal.ZERO)
                        .build();"""

new_builder = """                NhanVien nv = pdb.getNhanVien();
                if (nv == null) {
                    nv = nhanVienRepository.findAll().stream().findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên"));
                }
                Thue thueMacDinh = thueRepository.findAll().stream().findFirst().orElseThrow(() -> new ResourceNotFoundException("Chưa cấu hình Thuế trong hệ thống"));
                HoaDon newInvoice = HoaDon.builder()
                        .maHD("HD_" + System.currentTimeMillis())
                        .phieuDatBan(pdb)
                        .nhanVien(nv)
                        .thue(thueMacDinh)
                        .ngayTao(java.time.LocalDateTime.now())
                        .gioVao(java.time.LocalTime.now())
                        .trangThaiThanhToan(com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThanhToanHoaDon.CHUA_THANH_TOAN)
                        .thueSuat(thueMacDinh.getThueSuat())
                        .tienThue(java.math.BigDecimal.ZERO)
                        .tienPhiDV(java.math.BigDecimal.ZERO)
                        .tongTienGoc(java.math.BigDecimal.ZERO)
                        .tienGiamGia(java.math.BigDecimal.ZERO)
                        .tongThanhToan(java.math.BigDecimal.ZERO)
                        .tyLePhiDV(java.math.BigDecimal.ZERO)
                        .build();"""

c = c.replace(old_builder, new_builder)
with open(path, 'w', encoding='utf-8') as f: f.write(c)
