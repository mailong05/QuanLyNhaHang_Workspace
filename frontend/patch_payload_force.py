import os

# 1. Update EmployeeManagement.jsx
path_emp = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/EmployeeManagement.jsx'
with open(path_emp, 'r', encoding='utf-8') as f:
    c = f.read()

old_payload_emp = """        const payload = {
          ...values,
          ngayVaoLam: values.ngayVaoLam.format('YYYY-MM-DD')
        };"""

new_payload_emp = """        const payload = {
          ...values,
          username: form.getFieldValue('username'),
          password: form.getFieldValue('password'),
          ngayVaoLam: values.ngayVaoLam.format('YYYY-MM-DD')
        };"""

c = c.replace(old_payload_emp, new_payload_emp)
with open(path_emp, 'w', encoding='utf-8') as f: f.write(c)


# 2. Update CustomerManagement.jsx
path_cus = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/CustomerManagement.jsx'
with open(path_cus, 'r', encoding='utf-8') as f:
    c = f.read()

old_payload_cus = """        const requestData = {
          hoTen: values.hoTen,
          sdt: values.sdt,
          email: values.email,
          loaiThanhVien: editingCustomer.loaiThanhVien || 'DONG', 
          diemTichLuy: editingCustomer.diemTichLuy,
          username: values.username,
          password: values.password
        };"""

new_payload_cus = """        const requestData = {
          hoTen: values.hoTen,
          sdt: values.sdt,
          email: values.email,
          loaiThanhVien: editingCustomer.loaiThanhVien || 'DONG', 
          diemTichLuy: editingCustomer.diemTichLuy,
          username: form.getFieldValue('username'),
          password: form.getFieldValue('password')
        };"""

c = c.replace(old_payload_cus, new_payload_cus)
with open(path_cus, 'w', encoding='utf-8') as f: f.write(c)

