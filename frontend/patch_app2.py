import os
path = 'C:/QuanLyNhaHang_Workspace/frontend/src/App.jsx'
with open(path, 'r', encoding='utf-8') as f: c = f.read()

import_str = """import Analytics from './pages/admin/Analytics';
import CustomerManagement from './pages/admin/CustomerManagement';
import AreaManagement from './pages/admin/AreaManagement';
import CategoryManagement from './pages/admin/CategoryManagement';
import SystemSettings from './pages/admin/SystemSettings';
import ComingSoon from './pages/admin/ComingSoon';"""

c = c.replace(
    "import Analytics from './pages/admin/Analytics';\nimport CustomerManagement from './pages/admin/CustomerManagement';\nimport ComingSoon from './pages/admin/ComingSoon';",
    import_str
)

routes_str = """            <Route path="invoices" element={<InvoiceManagement />} />
            <Route path="customers" element={<CustomerManagement />} />
            <Route path="categories" element={<CategoryManagement />} />
            <Route path="areas" element={<AreaManagement />} />
            <Route path="employees" element={<ComingSoon title="Quản lý nhân viên" />} />
            <Route path="settings" element={<SystemSettings />} />"""

c = c.replace(
    """            <Route path="invoices" element={<InvoiceManagement />} />
            <Route path="customers" element={<CustomerManagement />} />
            <Route path="categories" element={<ComingSoon title="Danh mục món ăn" />} />
            <Route path="areas" element={<ComingSoon title="Quản lý khu vực" />} />
            <Route path="employees" element={<ComingSoon title="Quản lý nhân viên" />} />
            <Route path="settings" element={<ComingSoon title="Cài đặt hệ thống" />} />""",
    routes_str
)

with open(path, 'w', encoding='utf-8') as f: f.write(c)
print('Patched App.jsx')
