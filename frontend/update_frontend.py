import os, re

base_path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin'

def update_frontend(file_name, fetch_func, api_endpoint, status_options, table_index=1):
    path = f"{base_path}/{file_name}"
    with open(path, 'r', encoding='utf-8') as f: c = f.read()
    
    # 1. Add states
    state_injection = """
  const [searchText, setSearchText] = useState('');
  const [filterStatus, setFilterStatus] = useState('');
"""
    if 'const [searchText, setSearchText] = useState' not in c:
        c = c.replace('const [loading, setLoading] = useState(false);', 'const [loading, setLoading] = useState(false);' + state_injection)
    
    # 2. Modify fetch function
    # e.g., apiClient.get(`/api/v1/ban-an?page=${page - 1}&size=${pageSize}`);
    old_fetch_line = f"const data = await apiClient.get(`{api_endpoint}?page=${{page - 1}}&size=${{pageSize}}`);"
    new_fetch_line = f"const data = await apiClient.get(`{api_endpoint}?page=${{page - 1}}&size=${{pageSize}}&keyword=${{searchText}}&trangThai=${{filterStatus}}`);"
    
    # If the old line is not exactly that, we can use regex
    c = re.sub(fr"const data = await apiClient\.get\(`{api_endpoint}\?page=\$\{{page - 1\}}&size=\$\{{pageSize\}}\)`\);", new_fetch_line, c)
    # Some files use different syntax?
    # e.g., `/api/v1/mon-an?page=${page - 1}&size=${pageSize}`
    
    # 3. Add UI components
    # Look for `<Table ` or `<Table\n` and inject `<Space>` before it.
    # Wait, they usually have a top bar with `<Button icon={<PlusOutlined />}>`. We should add search filters there.
    
    ui_injection = f"""
        <Space style={{ marginBottom: 16 }}>
          <Input.Search 
            placeholder="Tìm kiếm theo tên / mã..." 
            allowClear 
            onSearch={{(val) => {{ setSearchText(val); {fetch_func}(1, pagination.pageSize); }} }} 
            style={{ width: 250 }} 
          />
          <Select 
            placeholder="Lọc theo trạng thái" 
            allowClear 
            style={{ width: 200 }} 
            onChange={{(val) => {{ setFilterStatus(val || ''); {fetch_func}(1, pagination.pageSize); }} }}
          >
            {status_options}
          </Select>
        </Space>
"""
    
    # Let's just find `marginBottom: 16 }}>` and inject after it?
    # Usually: `<div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between' }}>`
    # Or `<Button type="primary" onClick={() => setIsModalVisible(true)} icon={<PlusOutlined />}>`
    
    # A safer way is to inject it right before the `<Table` element.
    if 'placeholder="Tìm kiếm theo tên' not in c:
        c = re.sub(r'(<Table\s)', ui_injection + r'\n        \1', c, count=1)
    
    # 4. Modify useEffect to NOT depend on searchText/filterStatus if we trigger manually via onChange/onSearch.
    # BUT wait! If handleTableChange calls fetchTables(current, pageSize), it needs the current searchText from state.
    # React state in handleTableChange will be fresh if it's rendered, but since it's a callback, we should pass them or they use the state.
    
    with open(path, 'w', encoding='utf-8') as f: f.write(c)

# 1. BanAn
update_frontend(
    'TableManagement.jsx', 'fetchTables', '/api/v1/ban-an',
    '''
            <Option value="TRONG">Trống</Option>
            <Option value="DANG_SUDUNG">Đang sử dụng</Option>
            <Option value="DA_DAT">Đã đặt</Option>
            <Option value="BAO_TRI">Bảo trì</Option>
    '''
)

# 2. MonAn
update_frontend(
    'MenuManagement.jsx', 'fetchMenus', '/api/v1/mon-an',
    '''
            <Option value="CON_HANG">Còn hàng</Option>
            <Option value="HET_HANG">Hết hàng</Option>
            <Option value="NGUNG_BAN">Ngừng bán</Option>
    '''
)

# 3. KhuyenMai
update_frontend(
    'VoucherManagement.jsx', 'fetchVouchers', '/api/v1/khuyen-mai',
    '''
            <Option value="DANG_HOAT_DONG">Đang hoạt động</Option>
            <Option value="DA_KET_THUC">Đã kết thúc</Option>
            <Option value="DANG_CHO">Đang chờ</Option>
    '''
)

# 4. HoaDon
update_frontend(
    'InvoiceManagement.jsx', 'fetchInvoices', '/api/v1/hoa-don',
    '''
            <Option value="CHUA_THANH_TOAN">Chưa thanh toán</Option>
            <Option value="DA_THANH_TOAN">Đã thanh toán</Option>
            <Option value="HUY">Hủy</Option>
    '''
)

print('Done frontend injection')
