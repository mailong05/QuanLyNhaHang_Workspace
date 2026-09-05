import os, re

file_path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/MenuManagement.jsx'
with open(file_path, 'r', encoding='utf-8') as f: content = f.read()

content = re.sub(
    r"dataIndex:\s*'tenLoai',\s*key:\s*'tenLoai',",
    "dataIndex: 'tenLoai',\n      key: 'tenLoai',\n      render: (val) => LOAI_MON_AN[val] || val,",
    content
)

content = re.sub(
    r"dataIndex:\s*'donViTinh',\s*key:\s*'donViTinh',",
    "dataIndex: 'donViTinh',\n      key: 'donViTinh',\n      render: (val) => DON_VI_TINH[val] || val,",
    content
)

dvt_form = """          <Form.Item
            name="donViTinh"
            label="Đơn vị tính"
          >
            <Select placeholder="Chọn đơn vị">
              {Object.entries(DON_VI_TINH).map(([key, val]) => (
                <Option key={key} value={key}>{val}</Option>
              ))}
            </Select>
          </Form.Item>"""

loai_form = """          <Form.Item
            name="tenLoai"
            label="Tên Loại Món"
            rules={[{ required: true, message: 'Vui lòng chọn phân loại!' }]}
          >
            <Select placeholder="Chọn loại món">
              {Object.entries(LOAI_MON_AN).map(([key, val]) => (
                <Option key={key} value={key}>{val}</Option>
              ))}
            </Select>
          </Form.Item>"""

content = re.sub(r'<Form\.Item[^>]*name=\"donViTinh\".*?<\/Form\.Item>', dvt_form, content, flags=re.DOTALL)
content = re.sub(r'<Form\.Item[^>]*name=\"tenLoai\".*?<\/Form\.Item>', loai_form, content, flags=re.DOTALL)

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
