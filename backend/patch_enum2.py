import os

path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/repository/HoaDonRepository.java'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

c = c.replace("'DA_THANH_TOAN'", "com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThanhToanHoaDon.DA_THANH_TOAN")
c = c.replace("'TIEN_MAT'", "com.QuanLyDatBanNhaHang.demo.enums.PhuongThucThanhToanHoaDon.TIEN_MAT")

with open(path, 'w', encoding='utf-8') as f:
    f.write(c)
