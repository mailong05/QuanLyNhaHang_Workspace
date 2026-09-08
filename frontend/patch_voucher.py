import os

path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/VoucherManagement.jsx'
with open(path, 'r', encoding='utf-8') as f: c = f.read()

c = c.replace('const fetchVouchers = async () => {', 'const fetchVouchers = async (page = 1, pageSize = 10, keyword = searchText, status = filterStatus) => {')
c = c.replace("apiClient.get('/api/v1/khuyen-mai');", 'apiClient.get(`/api/v1/khuyen-mai?keyword=${keyword}&trangThai=${status}`);')

c = c.replace('onSearch={(val) => { setSearchText(val); fetchVouchers(1, pagination.pageSize); }}', 'onSearch={(val) => { setSearchText(val); fetchVouchers(1, 10, val, filterStatus); }}')
c = c.replace("onChange={(val) => { setFilterStatus(val || ''); fetchVouchers(1, pagination.pageSize); }}", "onChange={(val) => { setFilterStatus(val || ''); fetchVouchers(1, 10, searchText, val || ''); }}")

with open(path, 'w', encoding='utf-8') as f: f.write(c)
