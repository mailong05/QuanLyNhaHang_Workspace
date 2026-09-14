import os

path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/service/impl/PosOperationServiceImpl.java'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

old_logic = """        phieuNguon.setTrangThai(TrangThaiPhieuDatBan.DA_GOP_BAN);
        for (ChiTietPhieuDatBan ctNguon : phieuNguon.getChiTietPhieuDatBans()) {
            ctNguon.getBanAn().setTrangThai(com.QuanLyDatBanNhaHang.demo.enums.TrangThaiBanAn.TRONG);
            banAnRepository.save(ctNguon.getBanAn());
        }
        phieuDatBanRepository.save(phieuNguon);"""

new_logic = """        phieuNguon.setTrangThai(TrangThaiPhieuDatBan.DA_GOP_BAN);
        // Chuyển quyền sở hữu các bàn từ Phiếu Nguồn sang Phiếu Đích để giữ nguyên trạng thái Đang phục vụ
        for (ChiTietPhieuDatBan ctNguon : phieuNguon.getChiTietPhieuDatBans()) {
            ChiTietPhieuDatBan newCtPhieu = ChiTietPhieuDatBan.builder()
                    .phieuDatBan(phieuDich)
                    .banAn(ctNguon.getBanAn())
                    .ghiChu("Bàn gộp từ phiếu " + phieuNguon.getMaPhieuDat())
                    .build();
            phieuDich.getChiTietPhieuDatBans().add(newCtPhieu);
            chiTietPhieuDatBanRepository.save(newCtPhieu);
        }
        phieuDatBanRepository.save(phieuNguon);
        phieuDatBanRepository.save(phieuDich);"""

if old_logic in c:
    c = c.replace(old_logic, new_logic)
    with open(path, 'w', encoding='utf-8') as f: f.write(c)
    print('Patched gopBan to transfer table ownership')
else:
    print('Not found')
