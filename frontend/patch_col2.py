import os

path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/BookingManagement.jsx'
with open(path, 'r', encoding='utf-8') as f:
    lines = f.readlines()

new_lines = []
for line in lines:
    new_lines.append(line)
    if "dataIndex: 'soLuongNguoi'" in line:
        new_lines.append("      { title: 'Tiền cọc', dataIndex: 'tienDatCoc', key: 'tienDatCoc', render: (val) => val ? val.toLocaleString('vi-VN') + ' đ' : '0 đ' },\n")
        new_lines.append("      { title: 'Mã bàn', key: 'maBan', render: (_, record) => record.chiTiets && record.chiTiets.length > 0 ? record.chiTiets.map(ct => ct.maBan).join(', ') : 'Chưa xếp' },\n")

with open(path, 'w', encoding='utf-8') as f:
    f.writelines(new_lines)
print('Patched successfully')
