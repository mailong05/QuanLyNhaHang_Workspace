import os

path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/service/impl/PosOperationServiceImpl.java'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

helper = """
    private void calculateTotalsAndDiscounts(HoaDon hoaDon) {
        BigDecimal tongTienGoc = hoaDon.getChiTietHoaDons() != null ? hoaDon.getChiTietHoaDons().stream()
                .map(ChiTietHoaDon::getThanhTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO;
        hoaDon.setTongTienGoc(tongTienGoc);

        BigDecimal thueSuat = hoaDon.getThueSuat() != null ? hoaDon.getThueSuat().divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP) : BigDecimal.ZERO;
        BigDecimal tienThue = tongTienGoc.multiply(thueSuat);
        hoaDon.setTienThue(tienThue);

        // Khuyến mãi repository is not in this class, wait, we need KhuyenMaiRepository.
        // Let's inject it if not exist or we can just find it using EntityManager?
"""

# Wait, KhuyenMaiRepository is not injected in PosOperationServiceImpl!
