import os

path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/service/impl/PosOperationServiceImpl.java'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

new_method = """
    @Override
    @Transactional
    public HoaDonResponseDTO gopBan(com.QuanLyDatBanNhaHang.demo.dto.request.PosGopBanRequestDTO request) {
        PhieuDatBan phieuNguon = phieuDatBanRepository.findByMaPhieuDatIgnoreCaseWithRelations(request.getMaPhieuDatNguon())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Phiếu đặt nguồn: " + request.getMaPhieuDatNguon()));

        if (phieuNguon.getTrangThai() != TrangThaiPhieuDatBan.DANG_PHUC_VU) {
            throw new IllegalArgumentException("Phiếu đặt nguồn không ở trạng thái đang phục vụ");
        }

        HoaDon hoaDonNguon = hoaDonRepository.findAll().stream()
                .filter(hd -> hd.getPhieuDatBan().getId().equals(phieuNguon.getId()) && hd.getTrangThaiThanhToan() == TrangThaiThanhToanHoaDon.CHUA_THANH_TOAN)
                .findFirst()
                .orElse(null);

        BanAn banDich = banAnRepository.findByMaBanIgnoreCase(request.getMaBanDich())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Bàn đích: " + request.getMaBanDich()));

        ChiTietPhieuDatBan ctDich = chiTietPhieuDatBanRepository.findAll().stream()
                .filter(ct -> ct.getBanAn().getId().equals(banDich.getId()) && ct.getPhieuDatBan().getTrangThai() == TrangThaiPhieuDatBan.DANG_PHUC_VU)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Bàn đích không đang phục vụ"));

        PhieuDatBan phieuDich = ctDich.getPhieuDatBan();
        
        if (phieuNguon.getId().equals(phieuDich.getId())) {
             throw new IllegalArgumentException("Hai bàn đang thuộc cùng 1 phiếu, không thể gộp");
        }

        HoaDon hoaDonDich = hoaDonRepository.findAll().stream()
                .filter(hd -> hd.getPhieuDatBan().getId().equals(phieuDich.getId()) && hd.getTrangThaiThanhToan() == TrangThaiThanhToanHoaDon.CHUA_THANH_TOAN)
                .findFirst()
                .orElse(null);

        if (hoaDonNguon != null && hoaDonDich != null) {
            for (ChiTietHoaDon ctNguon : hoaDonNguon.getChiTietHoaDons()) {
                Optional<ChiTietHoaDon> existing = hoaDonDich.getChiTietHoaDons().stream()
                        .filter(ct -> ct.getMonAn().getId().equals(ctNguon.getMonAn().getId()))
                        .findFirst();
                if (existing.isPresent()) {
                    ChiTietHoaDon ctD = existing.get();
                    ctD.setSoLuong(ctD.getSoLuong() + ctNguon.getSoLuong());
                    ctD.setThanhTien(BigDecimal.valueOf(ctD.getSoLuong()).multiply(ctD.getDonGiaLuuTru()));
                    chiTietHoaDonRepository.save(ctD);
                } else {
                    ChiTietHoaDon newCt = ChiTietHoaDon.builder()
                            .hoaDon(hoaDonDich)
                            .monAn(ctNguon.getMonAn())
                            .soLuong(ctNguon.getSoLuong())
                            .donGiaLuuTru(ctNguon.getDonGiaLuuTru())
                            .thanhTien(ctNguon.getThanhTien())
                            .ghiChu(ctNguon.getGhiChu())
                            .build();
                    hoaDonDich.getChiTietHoaDons().add(newCt);
                    chiTietHoaDonRepository.save(newCt);
                }
            }

            chiTietHoaDonRepository.deleteAll(hoaDonNguon.getChiTietHoaDons());
            hoaDonNguon.getChiTietHoaDons().clear();
            hoaDonRepository.delete(hoaDonNguon);

            BigDecimal tongTienGoc = hoaDonDich.getChiTietHoaDons().stream().map(ChiTietHoaDon::getThanhTien).reduce(BigDecimal.ZERO, BigDecimal::add);
            hoaDonDich.setTongTienGoc(tongTienGoc);
            BigDecimal thueSuat = hoaDonDich.getThueSuat() != null ? hoaDonDich.getThueSuat().divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP) : BigDecimal.ZERO;
            BigDecimal tienThue = tongTienGoc.multiply(thueSuat);
            hoaDonDich.setTienThue(tienThue);
            BigDecimal tienGiamGia = hoaDonDich.getTienGiamGia() != null ? hoaDonDich.getTienGiamGia() : BigDecimal.ZERO;
            hoaDonDich.setTongThanhToan(tongTienGoc.add(tienThue).subtract(tienGiamGia));
            hoaDonDich = hoaDonRepository.save(hoaDonDich);
        } else if (hoaDonNguon != null && hoaDonDich == null) {
            hoaDonNguon.setPhieuDatBan(phieuDich);
            hoaDonRepository.save(hoaDonNguon);
            hoaDonDich = hoaDonNguon;
        }

        phieuNguon.setTrangThai(TrangThaiPhieuDatBan.DA_GOP_BAN);
        for (ChiTietPhieuDatBan ctNguon : phieuNguon.getChiTietPhieuDatBans()) {
            ctNguon.getBanAn().setTrangThai(com.QuanLyDatBanNhaHang.demo.enums.TrangThaiBanAn.TRONG);
            banAnRepository.save(ctNguon.getBanAn());
        }
        phieuDatBanRepository.save(phieuNguon);

        if (hoaDonDich != null) {
            return mapToDTO(hoaDonDich);
        } else {
            return null; // Both didn't have invoices yet
        }
    }
"""

if "public HoaDonResponseDTO gopBan" not in c:
    old = "private HoaDonResponseDTO mapToDTO(HoaDon hd) {"
    c = c.replace(old, new_method + "\n    " + old)
    with open(path, 'w', encoding='utf-8') as f: f.write(c)
    print("Patched gopBan logic in PosOperationServiceImpl")
else:
    print("Already exists")
