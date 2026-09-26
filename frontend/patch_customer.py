import os

path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/CustomerManagement.jsx'
with open(path, 'r', encoding='utf-8') as f: c = f.read()

# Add Username to columns
if "title: 'Tên đăng nhập'" not in c:
    c = c.replace("{ title: 'Email', dataIndex: 'email', key: 'email' },",
                  "{ title: 'Email', dataIndex: 'email', key: 'email' },\n    { title: 'Tên đăng nhập', dataIndex: 'username', key: 'username', render: val => val ? val : <span style={{color: '#aaa'}}>Không có</span> },")

# Add form items for username/password
old_modal_items = """          <Form.Item name="sdt" label="Số điện thoại" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="email" label="Email">
            <Input type="email" />
          </Form.Item>"""
new_modal_items = """          <Form.Item name="sdt" label="Số điện thoại" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="email" label="Email">
            <Input type="email" />
          </Form.Item>
          
          <div style={{ borderTop: '1px solid #f0f0f0', margin: '20px 0', paddingTop: '10px' }}>
            <h4 style={{ marginBottom: 16 }}>Tài khoản Web (Tùy chọn)</h4>
            <Form.Item name="username" label="Tên đăng nhập">
              <Input disabled={editingId !== null} placeholder="Nhập username nếu muốn cấp tài khoản" />
            </Form.Item>
            <Form.Item name="password" label="Mật khẩu">
              <Input.Password placeholder={editingId ? "Bỏ trống nếu không muốn đổi mật khẩu" : "Nhập mật khẩu"} />
            </Form.Item>
          </div>"""
c = c.replace(old_modal_items, new_modal_items)

with open(path, 'w', encoding='utf-8') as f: f.write(c)
