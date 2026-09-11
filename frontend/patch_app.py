import os
path = 'C:/QuanLyNhaHang_Workspace/frontend/src/App.jsx'
with open(path, 'r', encoding='utf-8') as f: c = f.read()

import_str = """import Analytics from './pages/admin/Analytics';
import CustomerManagement from './pages/admin/CustomerManagement';
import ComingSoon from './pages/admin/ComingSoon';"""

c = c.replace("import Analytics from './pages/admin/Analytics';", import_str)

routes_str = """            <Route path="invoices" element={<InvoiceManagement />} />
            <Route path="customers" element={<CustomerManagement />} />
            <Route path="categories" element={<ComingSoon title="Danh mục món ăn" />} />
            <Route path="areas" element={<ComingSoon title="Quản lý khu vực" />} />
            <Route path="employees" element={<ComingSoon title="Quản lý nhân viên" />} />
            <Route path="settings" element={<ComingSoon title="Cài đặt hệ thống" />} />"""

c = c.replace("<Route path=\"invoices\" element={<InvoiceManagement />} />", routes_str)

with open(path, 'w', encoding='utf-8') as f: f.write(c)
print('Patched App.jsx')
