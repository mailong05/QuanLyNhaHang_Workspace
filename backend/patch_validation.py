import os

path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/service/impl/PhieuDatBanServiceImpl.java'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

old_code = """        PhieuDatBan pdb = phieuDatBanRepository.findByMaPhieuDatIgnoreCaseWithRelations(maPhieuDat)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Phiếu đặt với mã: " + maPhieuDat));

        if (requestDTO.getThoiGianDen().isBefore(LocalDateTime.now().plusMinutes(30))) {
            throw new IllegalArgumentException("Thời gian đến phải lớn hơn thời gian hiện tại ít nhất 30 phút.");
        }"""

new_code = """        PhieuDatBan pdb = phieuDatBanRepository.findByMaPhieuDatIgnoreCaseWithRelations(maPhieuDat)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Phiếu đặt với mã: " + maPhieuDat));

        if (!requestDTO.getThoiGianDen().equals(pdb.getThoiGianDen()) && requestDTO.getThoiGianDen().isBefore(LocalDateTime.now().plusMinutes(30))) {
            throw new IllegalArgumentException("Thời gian đến phải lớn hơn thời gian hiện tại ít nhất 30 phút.");
        }"""

if old_code in c:
    c = c.replace(old_code, new_code)
    with open(path, 'w', encoding='utf-8') as f: f.write(c)
    print('Patched time validation in updatePhieuDatBan')
else:
    print('Could not find exact block')
