import os

filepath = r"c:\QuanLyDatBanNhaHangVerWeb\src\main\java\com\QuanLyDatBanNhaHang\demo\service\impl\HoaDonServiceImpl.java"
with open(filepath, "r", encoding="utf-8") as f: content = f.read()

content = content.replace("if (tongTienGoc < km.getDieuKienToiThieu())", "if (requestDTO.getTongTienGoc() < km.getDieuKienToiThieu())")

with open(filepath, "w", encoding="utf-8") as f: f.write(content)
print("Fixed HoaDonServiceImpl")
