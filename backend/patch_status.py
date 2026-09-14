import os

path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/service/impl/PhieuDatBanServiceImpl.java'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

old_code = """        // Xóa chi tiết cũ và map chi tiết mới
        if (requestDTO.getChiTiets() != null) {
            pdb.getChiTietPhieuDatBans().clear();
            for (ChiTietPhieuDatBanCreateRequestDTO cReq : requestDTO.getChiTiets()) {
                BanAn ba = banAnRepository.findByMaBanIgnoreCase(cReq.getMaBan())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Bàn ăn: " + cReq.getMaBan()));
                
                ChiTietPhieuDatBan ct = ChiTietPhieuDatBan.builder()
                        .phieuDatBan(pdb)
                        .banAn(ba)
                        .ghiChu(cReq.getGhiChu())
                        .build();
                pdb.getChiTietPhieuDatBans().add(ct);
            }
        }"""

new_code = """        // Xóa chi tiết cũ và map chi tiết mới
        if (requestDTO.getChiTiets() != null) {
            // Restore trạng thái bàn cũ về TRONG nếu phiếu đang phục vụ
            if (pdb.getTrangThai() == TrangThaiPhieuDatBan.DANG_PHUC_VU || pdb.getTrangThai() == TrangThaiPhieuDatBan.DA_GOP_BAN || pdb.getTrangThai() == TrangThaiPhieuDatBan.HOAN_THANH || pdb.getTrangThai() == TrangThaiPhieuDatBan.DA_HUY) {
                for (ChiTietPhieuDatBan oldCt : pdb.getChiTietPhieuDatBans()) {
                    oldCt.getBanAn().setTrangThai(com.QuanLyDatBanNhaHang.demo.enums.TrangThaiBanAn.TRONG);
                    banAnRepository.save(oldCt.getBanAn());
                }
            }

            pdb.getChiTietPhieuDatBans().clear();
            for (ChiTietPhieuDatBanCreateRequestDTO cReq : requestDTO.getChiTiets()) {
                BanAn ba = banAnRepository.findByMaBanIgnoreCase(cReq.getMaBan())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Bàn ăn: " + cReq.getMaBan()));
                
                if (requestDTO.getTrangThai() == TrangThaiPhieuDatBan.DANG_PHUC_VU) {
                    ba.setTrangThai(com.QuanLyDatBanNhaHang.demo.enums.TrangThaiBanAn.DANG_SUDUNG);
                    banAnRepository.save(ba);
                } else if (requestDTO.getTrangThai() == TrangThaiPhieuDatBan.DA_XAC_NHAN) {
                    ba.setTrangThai(com.QuanLyDatBanNhaHang.demo.enums.TrangThaiBanAn.DA_DAT);
                    banAnRepository.save(ba);
                }

                ChiTietPhieuDatBan ct = ChiTietPhieuDatBan.builder()
                        .phieuDatBan(pdb)
                        .banAn(ba)
                        .ghiChu(cReq.getGhiChu())
                        .build();
                pdb.getChiTietPhieuDatBans().add(ct);
            }
        } else {
             // Cập nhật trạng thái phiếu mà không đổi bàn (ví dụ Hủy phiếu, Thanh toán)
             if (requestDTO.getTrangThai() == TrangThaiPhieuDatBan.HOAN_THANH || requestDTO.getTrangThai() == TrangThaiPhieuDatBan.DA_HUY || requestDTO.getTrangThai() == TrangThaiPhieuDatBan.DA_GOP_BAN) {
                 for (ChiTietPhieuDatBan oldCt : pdb.getChiTietPhieuDatBans()) {
                     oldCt.getBanAn().setTrangThai(com.QuanLyDatBanNhaHang.demo.enums.TrangThaiBanAn.TRONG);
                     banAnRepository.save(oldCt.getBanAn());
                 }
             } else if (requestDTO.getTrangThai() == TrangThaiPhieuDatBan.DANG_PHUC_VU) {
                 for (ChiTietPhieuDatBan oldCt : pdb.getChiTietPhieuDatBans()) {
                     oldCt.getBanAn().setTrangThai(com.QuanLyDatBanNhaHang.demo.enums.TrangThaiBanAn.DANG_SUDUNG);
                     banAnRepository.save(oldCt.getBanAn());
                 }
             }
        }"""

if old_code in c:
    c = c.replace(old_code, new_code)
    with open(path, 'w', encoding='utf-8') as f: f.write(c)
    print('Patched chiTiets sync in backend')
else:
    print('Not found')
