import os

path_cus = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/CustomerManagement.jsx'
with open(path_cus, 'r', encoding='utf-8') as f: c = f.read()

old_req = """        const requestData = {
          hoTen: values.hoTen,
          sdt: values.sdt,
          email: values.email,
          // Fake enum if needed, or pass the existing one. Assumed backend handles it.
          loaiThanhVien: editingCustomer.loaiThanhVien || 'DONG', 
          diemTichLuy: editingCustomer.diemTichLuy
        };"""

new_req = """        const requestData = {
          hoTen: values.hoTen,
          sdt: values.sdt,
          email: values.email,
          loaiThanhVien: editingCustomer.loaiThanhVien || 'DONG', 
          diemTichLuy: editingCustomer.diemTichLuy,
          username: values.username,
          password: values.password
        };"""
c = c.replace(old_req, new_req)

with open(path_cus, 'w', encoding='utf-8') as f: f.write(c)
