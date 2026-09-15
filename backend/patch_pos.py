import os
import re

path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/service/impl/PosOperationServiceImpl.java'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

if "import com.QuanLyDatBanNhaHang.demo.entity.KhuyenMai;" not in c:
    c = c.replace("import com.QuanLyDatBanNhaHang.demo.entity.HoaDon;", "import com.QuanLyDatBanNhaHang.demo.entity.HoaDon;\nimport com.QuanLyDatBanNhaHang.demo.entity.KhuyenMai;\nimport com.QuanLyDatBanNhaHang.demo.repository.KhuyenMaiRepository;")

if "private final KhuyenMaiRepository" not in c:
    c = c.replace("private final ChiTietHoaDonRepository chiTietHoaDonRepository;", "private final ChiTietHoaDonRepository chiTietHoaDonRepository;\n    private final KhuyenMaiRepository khuyenMaiRepository;")

helper_method = """
    private void calculateTotalsAndDiscounts(HoaDon hoaDon) {
        BigDecimal tongTienGoc = hoaDon.getChiTietHoaDons() != null ? hoaDon.getChiTietHoaDons().stream()
                .map(ChiTietHoaDon::getThanhTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO;
        hoaDon.setTongTienGoc(tongTienGoc);

        BigDecimal thueSuat = hoaDon.getThueSuat() != null ? hoaDon.getThueSuat().divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP) : BigDecimal.ZERO;
        BigDecimal tienThue = tongTienGoc.multiply(thueSuat);
        hoaDon.setTienThue(tienThue);

        LocalDate today = LocalDate.now();
        List<KhuyenMai> khuyenMais = khuyenMaiRepository.findAll().stream()
                .filter(km -> km.getTrangThai() == com.QuanLyDatBanNhaHang.demo.enums.TrangThaiKhuyenMai.DANG_HOAT_DONG)
                .filter(km -> !today.isBefore(km.getNgayBatDau()) && !today.isAfter(km.getNgayKetThuc()))
                .filter(km -> km.getDieuKienToiThieu() == null || tongTienGoc.compareTo(km.getDieuKienToiThieu()) >= 0)
                .sorted((k1, k2) -> k2.getGiaTriGiam().compareTo(k1.getGiaTriGiam())) // Descending
                .toList();

        BigDecimal tienGiamGia = BigDecimal.ZERO;
        if (!khuyenMais.isEmpty()) {
            KhuyenMai bestKm = khuyenMais.get(0);
            hoaDon.setKhuyenMai(bestKm);
            if (bestKm.getGiaTriGiam().compareTo(BigDecimal.valueOf(100)) <= 0) {
                 tienGiamGia = tongTienGoc.multiply(bestKm.getGiaTriGiam()).divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);
            } else {
                 tienGiamGia = bestKm.getGiaTriGiam();
            }
        } else {
            hoaDon.setKhuyenMai(null);
        }
        hoaDon.setTienGiamGia(tienGiamGia);
        
        BigDecimal tongThanhToan = tongTienGoc.add(tienThue).subtract(tienGiamGia);
        if (tongThanhToan.compareTo(BigDecimal.ZERO) < 0) tongThanhToan = BigDecimal.ZERO;
        hoaDon.setTongThanhToan(tongThanhToan);
    }
"""

if "calculateTotalsAndDiscounts" not in c:
    c = c.replace("private HoaDonResponseDTO mapToDTO(HoaDon hd) {", helper_method + "\n    private HoaDonResponseDTO mapToDTO(HoaDon hd) {")

# Add xoaMon method
xoa_mon_method = """
    @Override
    @Transactional
    public HoaDonResponseDTO xoaMon(Long hoaDonId, Long chiTietId) {
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Hóa đơn"));
        
        ChiTietHoaDon chiTiet = chiTietHoaDonRepository.findById(chiTietId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Chi tiết món"));
        
        if (!chiTiet.getHoaDon().getId().equals(hoaDonId)) {
            throw new IllegalArgumentException("Chi tiết món không thuộc Hóa đơn này");
        }

        hoaDon.getChiTietHoaDons().remove(chiTiet);
        chiTietHoaDonRepository.delete(chiTiet);
        
        calculateTotalsAndDiscounts(hoaDon);
        
        hoaDon = hoaDonRepository.save(hoaDon);
        return mapToDTO(hoaDon);
    }
"""
if "public HoaDonResponseDTO xoaMon(" not in c:
    c = c.replace("public HoaDonResponseDTO gopBan(", xoa_mon_method + "\n    @Override\n    @Transactional\n    public HoaDonResponseDTO gopBan(")

# Update themMon to use calculateTotalsAndDiscounts
import re
c = re.sub(r'hoaDon\.setTongTienGoc\(tongTienGoc\);\s*BigDecimal thueSuat = .*?;\s*BigDecimal tienThue = .*?;\s*hoaDon\.setTienThue\(tienThue\);\s*BigDecimal tienGiamGia = .*?;\s*hoaDon\.setTongThanhToan\(.*?\);\s*', 'calculateTotalsAndDiscounts(hoaDon);\n        ', c, flags=re.DOTALL)

# Update gopBan to use calculateTotalsAndDiscounts
c = re.sub(r'BigDecimal tongTienGoc = hoaDonDich\.getChiTietHoaDons\(\)\.stream\(\)\.map\(ChiTietHoaDon::getThanhTien\)\.reduce\(BigDecimal\.ZERO, BigDecimal::add\);\s*hoaDonDich\.setTongTienGoc\(tongTienGoc\);\s*BigDecimal thueSuat = .*?;\s*BigDecimal tienThue = .*?;\s*hoaDonDich\.setTienThue\(tienThue\);\s*BigDecimal tienGiamGia = .*?;\s*hoaDonDich\.setTongThanhToan\(.*?\);\s*', 'calculateTotalsAndDiscounts(hoaDonDich);\n            ', c, flags=re.DOTALL)

with open(path, 'w', encoding='utf-8') as f: f.write(c)
