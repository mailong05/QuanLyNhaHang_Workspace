import os
path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/POSManagement.jsx'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

c = c.replace("\\'@ant-design/icons\\'", "'@ant-design/icons'")
with open(path, 'w', encoding='utf-8') as f:
    f.write(c)
