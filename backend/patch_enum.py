import os

path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/repository/HoaDonRepository.java'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

c = c.replace(\"'DA_THANH_TOAN'\", \"com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThanhToanHoaDon.DA_THANH_TOAN\")
c = c.replace(\"'TIEN_MAT'\", \"com.QuanLyDatBanNhaHang.demo.enums.PhuongThucThanhToanHoaDon.TIEN_MAT\")

with open(path, 'w', encoding='utf-8') as f:
    f.write(c)

path2 = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/repository/ChiTietHoaDonRepository.java'
with open(path2, 'r', encoding='utf-8') as f:
    c2 = f.read()

# For native query, the string 'DA_THANH_TOAN' works! Because it is native query (SQL).
# Wait, let's check ChiTietHoaDonRepository.
c2 = c2.replace(\"AND h.trangThaiThanhToan = 'DA_THANH_TOAN'\", \"AND h.trangThaiThanhToan = 'DA_THANH_TOAN'\")

with open(path2, 'w', encoding='utf-8') as f:
    f.write(c2)
