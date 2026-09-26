import os

# 1. Update EmployeeManagement.jsx
path_emp = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/EmployeeManagement.jsx'
with open(path_emp, 'r', encoding='utf-8') as f:
    c = f.read()

# Update handleEditClick
old_edit_emp = """      ngayVaoLam: record.ngayVaoLam ? dayjs(record.ngayVaoLam) : null,
      username: record.username
    });"""

new_edit_emp = """      ngayVaoLam: record.ngayVaoLam ? dayjs(record.ngayVaoLam) : null,
      username: record.username || record.maNV
    });"""

c = c.replace(old_edit_emp, new_edit_emp)

# Update disabled logic for username
old_input_emp = """<Input disabled={editingEmployee !== null} placeholder="Ví dụ: NV0123" />"""
new_input_emp = """<Input disabled={editingEmployee !== null && !!editingEmployee.username} placeholder="Ví dụ: NV0123" />"""
c = c.replace(old_input_emp, new_input_emp)

with open(path_emp, 'w', encoding='utf-8') as f: f.write(c)

# 2. Update CustomerManagement.jsx
path_cus = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/CustomerManagement.jsx'
with open(path_cus, 'r', encoding='utf-8') as f:
    c = f.read()

old_edit_cus = """      loaiThanhVien: record.hangKhachHang, // Giữ nguyên hạng cũ hoặc map cho đúng Enum của backend
      diemTichLuy: record.diemTichLuy,
      username: record.username
    });"""

new_edit_cus = """      loaiThanhVien: record.hangKhachHang, // Giữ nguyên hạng cũ hoặc map cho đúng Enum của backend
      diemTichLuy: record.diemTichLuy,
      username: record.username || record.maKH
    });"""

c = c.replace(old_edit_cus, new_edit_cus)

old_input_cus = """<Input disabled={editingId !== null} placeholder="Nhập username nếu muốn cấp tài khoản" />"""
new_input_cus = """<Input disabled={editingId !== null && !!editingCustomer?.username} placeholder="Nhập username nếu muốn cấp tài khoản" />"""
c = c.replace(old_input_cus, new_input_cus)

with open(path_cus, 'w', encoding='utf-8') as f: f.write(c)
