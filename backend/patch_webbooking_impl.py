import os
import re

path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/service/impl/WebBookingServiceImpl.java'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

impl = """
    @Override
    public java.util.List<java.util.Map<String, Object>> getAvailableTables(LocalDateTime thoiGianDen) {
        LocalDateTime start = thoiGianDen.minusHours(2);
        LocalDateTime end = thoiGianDen.plusHours(2);

        List<BanAn> allTables = banAnRepository.findAll();
        List<java.util.Map<String, Object>> result = new java.util.ArrayList<>();

        for (BanAn banAn : allTables) {
            boolean isAvailable = false;
            if (banAn.getTrangThai() != com.QuanLyDatBanNhaHang.demo.enums.TrangThaiBanAn.DANG_BAO_TRI) {
                List<ChiTietPhieuDatBan> conflicts = chiTietPhieuDatBanRepository.findConflictingBookings(banAn.getMaBan(), start, end, null);
                isAvailable = conflicts.isEmpty();
            }

            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", banAn.getId());
            map.put("maBan", banAn.getMaBan());
            map.put("soGhe", banAn.getSoGhe());
            map.put("khuVuc", banAn.getKhuVuc() != null ? banAn.getKhuVuc().name() : "");
            map.put("isAvailable", isAvailable);
            result.add(map);
        }
        return result;
    }
"""

if 'getAvailableTables' not in c:
    c = c.replace('public void createWebBooking(WebBookingRequestDTO request) {', impl + '\n    @Override\n    public void createWebBooking(WebBookingRequestDTO request) {')
    with open(path, 'w', encoding='utf-8') as f: f.write(c)
