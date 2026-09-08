import os

path_kh = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/repository/KhachHangRepository.java'
with open(path_kh, 'r', encoding='utf-8') as f:
    c = f.read()
c = c.replace('@Query("SELECT MAX(CAST(SUBSTRING(k.maKH, 3, 6) AS int)) FROM KhachHang k")', 
              '@Query("SELECT MAX(CAST(SUBSTRING(k.maKH, 3, 10) AS int)) FROM KhachHang k WHERE k.maKH LIKE \'KH%\' AND LENGTH(k.maKH) <= 10")')
with open(path_kh, 'w', encoding='utf-8') as f:
    f.write(c)

path_pdb = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/repository/PhieuDatBanRepository.java'
with open(path_pdb, 'r', encoding='utf-8') as f:
    c = f.read()
c = c.replace('@Query("SELECT MAX(CAST(SUBSTRING(p.maPhieuDat, 4, 6) AS int)) FROM PhieuDatBan p")',
              '@Query("SELECT MAX(CAST(SUBSTRING(p.maPhieuDat, 4, 10) AS int)) FROM PhieuDatBan p WHERE p.maPhieuDat LIKE \'PDB%\' AND LENGTH(p.maPhieuDat) <= 10")')
with open(path_pdb, 'w', encoding='utf-8') as f:
    f.write(c)

print('Patched repos for max ID')
