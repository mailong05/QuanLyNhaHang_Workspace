import os
path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/service/WebBookingService.java'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

if 'getAvailableTables' not in c:
    c = c.replace('void createWebBooking(WebBookingRequestDTO request);', 'void createWebBooking(WebBookingRequestDTO request);\n    java.util.List<java.util.Map<String, Object>> getAvailableTables(java.time.LocalDateTime thoiGianDen);')
    with open(path, 'w', encoding='utf-8') as f: f.write(c)
