import os

# 1. Update EmployeeManagement.jsx
path_emp = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/EmployeeManagement.jsx'
with open(path_emp, 'r', encoding='utf-8') as f:
    c = f.read()

old_edit_emp = """    form.setFieldsValue({
      hoTen: record.hoTen,
      sdt: record.sdt,
      email: record.email,
      chucVu: record.chucVu,
      trangThai: record.trangThai,
      luongCoBan: record.luongCoBan,
      ngayVaoLam: record.ngayVaoLam ? dayjs(record.ngayVaoLam) : null
    });"""

new_edit_emp = """    form.setFieldsValue({
      hoTen: record.hoTen,
      sdt: record.sdt,
      email: record.email,
      chucVu: record.chucVu,
      trangThai: record.trangThai,
      luongCoBan: record.luongCoBan,
      ngayVaoLam: record.ngayVaoLam ? dayjs(record.ngayVaoLam) : null,
      username: record.username
    });"""

c = c.replace(old_edit_emp, new_edit_emp)
with open(path_emp, 'w', encoding='utf-8') as f: f.write(c)

# 2. Update CustomerManagement.jsx
path_cus = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/CustomerManagement.jsx'
with open(path_cus, 'r', encoding='utf-8') as f:
    c = f.read()

old_edit_cus = """    form.setFieldsValue({
      hoTen: record.hoTen,
      sdt: record.sdt,
      email: record.email,
      loaiThanhVien: record.hangKhachHang, // Giữ nguyên hạng cũ hoặc map cho đúng Enum của backend
      diemTichLuy: record.diemTichLuy
    });"""

new_edit_cus = """    form.setFieldsValue({
      hoTen: record.hoTen,
      sdt: record.sdt,
      email: record.email,
      loaiThanhVien: record.hangKhachHang, // Giữ nguyên hạng cũ hoặc map cho đúng Enum của backend
      diemTichLuy: record.diemTichLuy,
      username: record.username
    });"""

c = c.replace(old_edit_cus, new_edit_cus)
with open(path_cus, 'w', encoding='utf-8') as f: f.write(c)
