import os
import re

path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/service/impl/PhieuDatBanServiceImpl.java'
with open(path, 'r', encoding='utf-8') as f: c = f.read()

old_logic = "boolean hasOpenInvoice = hoaDonRepository.findByPhieuDatBan_MaPhieuDatAndTrangThai(pdb.getMaPhieuDat(), com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThanhToanHoaDon.CHUA_THANH_TOAN).isPresent();"
new_logic = """boolean hasOpenInvoice = hoaDonRepository.findAll().stream()
                    .anyMatch(hd -> hd.getPhieuDatBan() != null && hd.getPhieuDatBan().getId().equals(pdb.getId()) && hd.getTrangThaiThanhToan() == com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThanhToanHoaDon.CHUA_THANH_TOAN);"""

c = c.replace(old_logic, new_logic)

with open(path, 'w', encoding='utf-8') as f: f.write(c)
