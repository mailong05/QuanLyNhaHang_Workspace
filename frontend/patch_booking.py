import os

path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/BookingManagement.jsx'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

col_old = """{ title: 'Sđt', key: 'sdt', render: (_, record) => record.sdtKH || record.sdt || 'N/A' },
      { title: 'Thời gian đến', dataIndex: 'thoiGianDen', key: 'thoiGianDen', render: (time) => time ? dayjs(time).format('HH:mm DD/MM/YYYY') : '' },
      { title: 'Số người', dataIndex: 'soLuongNguoi', key: 'soLuongNguoi', render: (num) => `${num} người` },"""
      
col_new = """{ title: 'Sđt', key: 'sdt', render: (_, record) => record.sdtKH || record.sdt || 'N/A' },
      { title: 'Thời gian đến', dataIndex: 'thoiGianDen', key: 'thoiGianDen', render: (time) => time ? dayjs(time).format('HH:mm DD/MM/YYYY') : '' },
      { title: 'Số người', dataIndex: 'soLuongNguoi', key: 'soLuongNguoi', render: (num) => `${num} người` },
      { title: 'Tiền cọc', dataIndex: 'tienDatCoc', key: 'tienDatCoc', render: (val) => val ? val.toLocaleString('vi-VN') + ' đ' : '0 đ' },"""

c = c.replace(col_old, col_new)

with open(path, 'w', encoding='utf-8') as f:
    f.write(c)
print('Patched BookingManagement.jsx')
