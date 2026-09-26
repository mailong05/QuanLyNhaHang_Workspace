import os

path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/CustomerManagement.jsx'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

c = c.replace('disabled={editingId !== null && !!editingCustomer?.username}', 'disabled={editingCustomer !== null && !!editingCustomer?.username}')
c = c.replace('editingId ?', 'editingCustomer !== null ?')

with open(path, 'w', encoding='utf-8') as f: f.write(c)
