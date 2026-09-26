import os

path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/EmployeeManagement.jsx'
with open(path, 'r', encoding='utf-8') as f: c = f.read()

# 1. Update columns
old_col = """      { title: 'SĐT', dataIndex: 'sdt', key: 'sdt' },
      { 
        title: 'Lương Cơ Bản',"""
new_col = """      { title: 'SĐT', dataIndex: 'sdt', key: 'sdt' },
      { title: 'Email', dataIndex: 'email', key: 'email' },
      { title: 'Tên đăng nhập', dataIndex: 'username', key: 'username', render: val => val ? val : <span style={{color: '#aaa'}}>Không có</span> },
      { 
        title: 'Lương Cơ Bản',"""
c = c.replace(old_col, new_col)

# 2. Update form
old_form_email = """          <Form.Item name="email" label="Email (Không bắt buộc)">
            <Input type="email" placeholder="example@gmail.com" />
          </Form.Item>
        </Form>"""
new_form_email = """          <Form.Item name="email" label="Email (Không bắt buộc)">
            <Input type="email" placeholder="example@gmail.com" />
          </Form.Item>

          <div style={{ borderTop: '1px solid #f0f0f0', margin: '20px 0', paddingTop: '10px' }}>
            <h4 style={{ marginBottom: 16 }}>Thông tin cấp quyền đăng nhập</h4>
            <div style={{ display: 'flex', gap: '16px' }}>
              <Form.Item name="username" label="Tên đăng nhập" rules={[{ required: true, message: 'Bắt buộc' }]} style={{ flex: 1 }}>
                <Input disabled={editingEmployee !== null} placeholder="Ví dụ: NV0123" />
              </Form.Item>
              <Form.Item name="password" label="Mật khẩu" rules={[{ required: !editingEmployee, message: 'Bắt buộc khi tạo mới' }]} style={{ flex: 1 }}>
                <Input.Password placeholder={editingEmployee ? "Bỏ trống nếu không đổi" : "Nhập mật khẩu"} />
              </Form.Item>
            </div>
          </div>
        </Form>"""
c = c.replace(old_form_email, new_form_email)

with open(path, 'w', encoding='utf-8') as f: f.write(c)
