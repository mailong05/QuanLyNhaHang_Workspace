import os

path = r'C:\QuanLyNhaHang_Workspace\backend\src\test\java\com\QuanLyDatBanNhaHang\demo\service\PhieuDatBanServiceTest.java'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

c = c.replace('PhieuDatBanDTO', 'PhieuDatBanResponseDTO')
c = c.replace('import com.QuanLyDatBanNhaHang.demo.dto.PhieuDatBanResponseDTO;', 'import com.QuanLyDatBanNhaHang.demo.dto.response.PhieuDatBanResponseDTO;\nimport org.springframework.data.domain.PageImpl;\nimport org.springframework.data.domain.PageRequest;\nimport org.springframework.data.domain.Pageable;\nimport static org.mockito.ArgumentMatchers.any;')

c = c.replace('phieuDatBanRepository.findAllWithRelations()', 'phieuDatBanRepository.findAllWithRelations(any(Pageable.class))')

c = c.replace('Page<PhieuDatBan>) Arrays.asList(phieu1, phieu2)', 'PageImpl<>(java.util.Arrays.asList(phieu1, phieu2))')
c = c.replace('Page<PhieuDatBan>) Arrays.asList(phieu)', 'PageImpl<>(java.util.Arrays.asList(phieu))')

c = c.replace('phieuDatBanService.getAllPhieuDatBan()', 'phieuDatBanService.getAllPhieuDatBan(PageRequest.of(0, 10))')

c = c.replace('List<PhieuDatBanResponseDTO> result = phieuDatBanService.getAllPhieuDatBan', 'Page<PhieuDatBanResponseDTO> result = phieuDatBanService.getAllPhieuDatBan')

c = c.replace('result.size()', 'result.getContent().size()')
c = c.replace('result.get(0)', 'result.getContent().get(0)')

c = c.replace('phieuDatBanRepository.findByIdWithRelations', 'phieuDatBanRepository.findByMaPhieuDatIgnoreCaseWithRelations')
c = c.replace('phieuDatBanService.getPhieuDatBanById', 'phieuDatBanService.getPhieuDatBanByMa')

c = c.replace('Optional<PhieuDatBanResponseDTO> result = phieuDatBanService.getPhieuDatBanByMa', 'PhieuDatBanResponseDTO result = phieuDatBanService.getPhieuDatBanByMa')
c = c.replace('assertTrue(result.isPresent(), "Phiếu phải tồn tại");', 'assertNotNull(result, "Phiếu phải tồn tại");')
c = c.replace('result.get().getMaPhieuDat()', 'result.getMaPhieuDat()')
c = c.replace('result.get().getTenKH()', 'result.getTenKH()')

c = c.replace('phieuDatBanService.getPhieuDatBanByMa("PDB001").orElse(null)', 'phieuDatBanService.getPhieuDatBanByMa("PDB001")')
c = c.replace('TrangThaiPhieuDatBan.valueOf("DANG_CHO")', 'TrangThaiPhieuDatBan.CHO_XAC_NHAN')
c = c.replace('TrangThaiPhieuDatBan.valueOf("DANG_SU_DUNG")', 'TrangThaiPhieuDatBan.DANG_PHUC_VU')
c = c.replace('ChucVuNhanVien.valueOf("PHUC_VU")', 'ChucVuNhanVien.PHUC_VU')

with open(path, 'w', encoding='utf-8') as f:
    f.write(c)
