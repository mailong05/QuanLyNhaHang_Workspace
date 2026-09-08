import os

controller_path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/controller/PhieuDatBanController.java'
with open(controller_path, 'r', encoding='utf-8') as f:
    c = f.read()

new_endpoint = """
    @GetMapping("/check-availability")
    public ResponseEntity<ApiResponse<Boolean>> checkAvailability(
            @RequestParam String maBan,
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime thoiGianDen) {
        boolean isAvailable = phieuDatBanService.checkTableAvailability(maBan, thoiGianDen);
        return ResponseEntity.ok(ApiResponse.success("Success", isAvailable));
    }
"""
if "/check-availability" not in c:
    c = c.replace('public class PhieuDatBanController {', 'public class PhieuDatBanController {' + new_endpoint)
    with open(controller_path, 'w', encoding='utf-8') as f: f.write(c)
    print('Added endpoint to controller')

service_interface_path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/service/PhieuDatBanService.java'
with open(service_interface_path, 'r', encoding='utf-8') as f:
    s = f.read()
if "checkTableAvailability" not in s:
    s = s.replace('void deletePhieuDatBan(String maPhieuDat);', 'void deletePhieuDatBan(String maPhieuDat);\n    boolean checkTableAvailability(String maBan, java.time.LocalDateTime thoiGianDen);')
    with open(service_interface_path, 'w', encoding='utf-8') as f: f.write(s)
    print('Added method to interface')

service_impl_path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/service/impl/PhieuDatBanServiceImpl.java'
with open(service_impl_path, 'r', encoding='utf-8') as f:
    si = f.read()
new_impl = """
    @Override
    public boolean checkTableAvailability(String maBan, java.time.LocalDateTime thoiGianDen) {
        java.time.LocalDateTime start = thoiGianDen.minusHours(2);
        java.time.LocalDateTime end = thoiGianDen.plusHours(2);
        return chiTietPhieuDatBanRepository.findConflictingBookings(maBan, start, end, null).isEmpty();
    }
"""
if "checkTableAvailability" not in si:
    si = si.replace('public void deletePhieuDatBan(String maPhieuDat) {', new_impl + '\n    @Override\n    @org.springframework.transaction.annotation.Transactional\n    public void deletePhieuDatBan(String maPhieuDat) {')
    with open(service_impl_path, 'w', encoding='utf-8') as f: f.write(si)
    print('Added method to impl')
