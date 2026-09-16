import os

path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/service/impl/PhieuDatBanServiceImpl.java'
with open(path, 'r', encoding='utf-8') as f: c = f.read()

if 'private final ThueRepository thueRepository;' not in c:
    c = c.replace('private final HoaDonRepository hoaDonRepository;', 'private final HoaDonRepository hoaDonRepository;\n    private final ThueRepository thueRepository;')

with open(path, 'w', encoding='utf-8') as f: f.write(c)
