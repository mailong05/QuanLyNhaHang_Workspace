import os
path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/service/impl/PhieuDatBanServiceImpl.java'
with open(path, 'r', encoding='utf-8') as f: c = f.read()

logic = """
        if (requestDTO.getTrangThai() == TrangThaiPhieuDatBan.DANG_PHUC_VU) {
            boolean hasOpenInvoice = hoaDonRepository.findByPhieuDatBan_MaPhieuDatAndTrangThai(pdb.getMaPhieuDat(), com.QuanLyDatBanNhaHang.demo.enums.TrangThaiHoaDon.CHUA_THANH_TOAN).isPresent();
            if (!hasOpenInvoice) {
                HoaDon newInvoice = HoaDon.builder()
                        .phieuDatBan(pdb)
                        .ngayTao(java.time.LocalDateTime.now())
                        .trangThai(com.QuanLyDatBanNhaHang.demo.enums.TrangThaiHoaDon.CHUA_THANH_TOAN)
                        .thueSuat(new java.math.BigDecimal("8.00"))
                        .build();
                hoaDonRepository.save(newInvoice);
            }
        }
        return convertToResponseDTO(phieuDatBanRepository.save(pdb));
"""

c = c.replace('return convertToResponseDTO(phieuDatBanRepository.save(pdb));', logic)

with open(path, 'w', encoding='utf-8') as f: f.write(c)
