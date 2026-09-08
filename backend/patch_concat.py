import os

repo_dir = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/repository'
for file in ['BanAnRepository.java', 'MonAnRepository.java', 'KhuyenMaiRepository.java', 'HoaDonRepository.java']:
    path = f'{repo_dir}/{file}'
    with open(path, 'r', encoding='utf-8') as f: c = f.read()
    c = c.replace("LOWER(CONCAT('%', :keyword, '%'))", "LOWER(:keyword)")
    with open(path, 'w', encoding='utf-8') as f: f.write(c)

svc_dir = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/service/impl'
for file in ['BanAnServiceImpl.java', 'MonAnServiceImpl.java', 'KhuyenMaiServiceImpl.java', 'HoaDonServiceImpl.java']:
    path = f'{svc_dir}/{file}'
    with open(path, 'r', encoding='utf-8') as f: c = f.read()
    c = c.replace('String kw = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;', 
                  'String kw = (keyword != null && !keyword.trim().isEmpty()) ? "%" + keyword.trim() + "%" : null;')
    with open(path, 'w', encoding='utf-8') as f: f.write(c)

print('Patched Backend Repositories and Services')
