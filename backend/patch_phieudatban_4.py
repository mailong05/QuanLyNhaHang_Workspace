import os
path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/service/impl/PhieuDatBanServiceImpl.java'
with open(path, 'r', encoding='utf-8') as f: c = f.read()

old_builder = """                HoaDon newInvoice = HoaDon.builder()
                        .phieuDatBan(pdb)
                        .ngayTao(java.time.LocalDateTime.now())
                        .trangThaiThanhToan(com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThanhToanHoaDon.CHUA_THANH_TOAN)
                        .thueSuat(new java.math.BigDecimal("8.00"))
                        .build();"""

new_builder = """                HoaDon newInvoice = HoaDon.builder()
                        .maHD("HD_" + System.currentTimeMillis())
                        .phieuDatBan(pdb)
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

c = c.replace(old_builder, new_builder)
with open(path, 'w', encoding='utf-8') as f: f.write(c)
