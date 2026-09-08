import os

base_path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin'

def patch_file(filename, fetch_func, api_endpoint):
    path = f'{base_path}/{filename}'
    with open(path, 'r', encoding='utf-8') as f: c = f.read()
    
    # Update fetch signature
    old_fetch_def = f'const {fetch_func} = async (page = 1, pageSize = 10) => {{'
    new_fetch_def = f'const {fetch_func} = async (page = 1, pageSize = 10, keyword = searchText, status = filterStatus) => {{'
    c = c.replace(old_fetch_def, new_fetch_def)
    
    old_fetch_def2 = f'const {fetch_func} = async (page = 1, size = 10) => {{'
    new_fetch_def2 = f'const {fetch_func} = async (page = 1, size = 10, keyword = searchText, status = filterStatus) => {{'
    c = c.replace(old_fetch_def2, new_fetch_def2)
    
    # Update API call (Table, Menu, Voucher)
    old_api = f'apiClient.get(`{api_endpoint}?page=${{page - 1}}&size=${{pageSize}}`);'
    new_api = f'apiClient.get(`{api_endpoint}?page=${{page - 1}}&size=${{pageSize}}&keyword=${{keyword}}&trangThai=${{status}}`);'
    c = c.replace(old_api, new_api)
    
    # Update API call (Invoice uses size instead of pageSize)
    old_api_inv = f'`http://localhost:8080{api_endpoint}?page=${{page - 1}}&size=${{size}}`'
    new_api_inv = f'`http://localhost:8080{api_endpoint}?page=${{page - 1}}&size=${{size}}&keyword=${{keyword}}&trangThai=${{status}}`'
    c = c.replace(old_api_inv, new_api_inv)
    
    # Update onSearch and onChange
    old_search = f'onSearch={{(val) => {{ setSearchText(val); {fetch_func}(1, pagination.pageSize); }}}}'
    new_search = f'onSearch={{(val) => {{ setSearchText(val); {fetch_func}(1, pagination.pageSize, val, filterStatus); }}}}'
    c = c.replace(old_search, new_search)
    
    old_change = f'onChange={{(val) => {{ setFilterStatus(val || \'\'); {fetch_func}(1, pagination.pageSize); }}}}'
    new_change = f'onChange={{(val) => {{ setFilterStatus(val || \'\'); {fetch_func}(1, pagination.pageSize, searchText, val || \'\'); }}}}'
    c = c.replace(old_change, new_change)
    
    with open(path, 'w', encoding='utf-8') as f: f.write(c)
    print(f'Patched {filename}')

patch_file('TableManagement.jsx', 'fetchTables', '/api/v1/ban-an')
patch_file('MenuManagement.jsx', 'fetchMenus', '/api/v1/mon-an')
patch_file('VoucherManagement.jsx', 'fetchVouchers', '/api/v1/khuyen-mai')
patch_file('InvoiceManagement.jsx', 'fetchInvoices', '/api/v1/hoa-don')
