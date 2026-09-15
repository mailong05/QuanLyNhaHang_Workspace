import os
path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/service/impl/PhieuDatBanServiceImpl.java'
with open(path, 'r', encoding='utf-8') as f: c = f.read()

c = c.replace(
    'boolean hasOpenInvoice = hoaDonRepository.findByPhieuDatBan_MaPhieuDatAndTrangThai(pdb.getMaPhieuDat(), com.QuanLyDatBanNhaHang.demo.enums.TrangThaiHoaDon.CHUA_THANH_TOAN).isPresent();',
    'boolean hasOpenInvoice = hoaDonRepository.findByPhieuDatBan_MaPhieuDatAndTrangThai(pdb.getMaPhieuDat(), com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThanhToanHoaDon.CHUA_THANH_TOAN).isPresent();'
)

c = c.replace(
    '.trangThai(com.QuanLyDatBanNhaHang.demo.enums.TrangThaiHoaDon.CHUA_THANH_TOAN)',
    '.trangThaiThanhToan(com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThanhToanHoaDon.CHUA_THANH_TOAN)'
)

with open(path, 'w', encoding='utf-8') as f: f.write(c)
