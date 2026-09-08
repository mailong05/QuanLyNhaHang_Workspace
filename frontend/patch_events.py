import os, re

base_path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin'
files = {
    'TableManagement.jsx': 'fetchTables',
    'MenuManagement.jsx': 'fetchMenus',
    'VoucherManagement.jsx': 'fetchVouchers',
    'InvoiceManagement.jsx': 'fetchInvoices'
}

for filename, fetch_func in files.items():
    path = f'{base_path}/{filename}'
    with open(path, 'r', encoding='utf-8') as f: c = f.read()
    
    # regex for onSearch
    c = re.sub(
        r'onSearch=\{\(val\) => \{ setSearchText\(val\); ' + fetch_func + r'\(1, pagination\.pageSize\);\s*\}\s*\}',
        f'onSearch={{(val) => {{ setSearchText(val); {fetch_func}(1, pagination.pageSize, val, filterStatus); }}}}',
        c
    )
    
    # regex for onChange
    c = re.sub(
        r"onChange=\{\(val\) => \{ setFilterStatus\(val \|\| ''\); " + fetch_func + r"\(1, pagination\.pageSize\);\s*\}\s*\}",
        f"onChange={{(val) => {{ setFilterStatus(val || ''); {fetch_func}(1, pagination.pageSize, searchText, val || ''); }}}}",
        c
    )
    
    with open(path, 'w', encoding='utf-8') as f: f.write(c)
    print(f'Patched {filename}')
