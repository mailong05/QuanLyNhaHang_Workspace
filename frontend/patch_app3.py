import os
path = 'C:/QuanLyNhaHang_Workspace/frontend/src/App.jsx'
with open(path, 'r', encoding='utf-8') as f: c = f.read()

c = c.replace(
    "import SystemSettings from './pages/admin/SystemSettings';\nimport ComingSoon from './pages/admin/ComingSoon';",
    "import SystemSettings from './pages/admin/SystemSettings';\nimport EmployeeManagement from './pages/admin/EmployeeManagement';\nimport ComingSoon from './pages/admin/ComingSoon';"
)

c = c.replace(
    '<Route path="employees" element={<ComingSoon title="Quản lý nhân viên" />} />',
    '<Route path="employees" element={<EmployeeManagement />} />'
)

with open(path, 'w', encoding='utf-8') as f: f.write(c)
print('Patched App.jsx for employees')
