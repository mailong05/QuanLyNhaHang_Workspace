import os
path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/security/SecurityConfig.java'
with open(path, 'r', encoding='utf-8') as f: c = f.read()
c = c.replace('"/api/web/booking"', '"/api/web/booking/**"')
with open(path, 'w', encoding='utf-8') as f: f.write(c)
