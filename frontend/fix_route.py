import os
path = 'C:/QuanLyNhaHang_Workspace/frontend/src/layouts/AdminLayout.jsx'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

if "'/admin/statistics'" in c:
    c = c.replace("'/admin/statistics'", "'/admin/analytics'")
    with open(path, 'w', encoding='utf-8') as f: f.write(c)
    print('Fixed analytics route in AdminLayout')
