import os

path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/service/impl/PhieuDatBanServiceImpl.java'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

target = """        pdb.setThoiGianDen(requestDTO.getThoiGianDen());
        pdb.setSoLuongNguoi(requestDTO.getSoLuongNguoi());
        pdb.setGhiChu(requestDTO.getGhiChu());
        pdb.setTrangThai(requestDTO.getTrangThai());
        pdb.setTienDatCoc(requestDTO.getTienDatCoc());"""
        
replacement = """        pdb.setThoiGianDen(requestDTO.getThoiGianDen());
        pdb.setSoLuongNguoi(requestDTO.getSoLuongNguoi());
        pdb.setGhiChu(requestDTO.getGhiChu());
        pdb.setTrangThai(requestDTO.getTrangThai());
        pdb.setTienDatCoc(requestDTO.getTienDatCoc());

        // Update KhachHang if requested
        if (pdb.getKhachHang() != null) {
            if (requestDTO.getHoTenKH() != null && !requestDTO.getHoTenKH().isBlank()) {
                pdb.getKhachHang().setHoTen(requestDTO.getHoTenKH());
            }
            if (requestDTO.getSdtKH() != null && !requestDTO.getSdtKH().isBlank()) {
                pdb.getKhachHang().setSdt(requestDTO.getSdtKH());
            }
            khachHangRepository.save(pdb.getKhachHang());
        }"""

if target in c:
    c = c.replace(target, replacement)
    with open(path, 'w', encoding='utf-8') as f:
        f.write(c)
    print("Patched PhieuDatBanServiceImpl")
else:
    print("Target not found in PhieuDatBanServiceImpl")
