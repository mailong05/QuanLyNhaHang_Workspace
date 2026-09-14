import os
import re

path_layout = 'C:/QuanLyNhaHang_Workspace/frontend/src/layouts/AdminLayout.jsx'
with open(path_layout, 'r', encoding='utf-8') as f:
    c = f.read()

# Replace the categories menu item
c = re.sub(r",\s*\{\s*key:\s*'/admin/categories',\s*label:\s*'Danh mục món',\s*roles:\s*\['ADMIN',\s*'ROLE_ADMIN',\s*'QUAN_LY'\]\s*\}", "", c)

with open(path_layout, 'w', encoding='utf-8') as f: f.write(c)
print('Removed from AdminLayout')

path_app = 'C:/QuanLyNhaHang_Workspace/frontend/src/App.jsx'
with open(path_app, 'r', encoding='utf-8') as f:
    c2 = f.read()

# Remove import
c2 = re.sub(r"import CategoryManagement from '\./pages/admin/CategoryManagement';\n", "", c2)
# Remove Route
c2 = re.sub(r"<Route path=\"categories\" element=\{<CategoryManagement />\} />\n\s*", "", c2)

with open(path_app, 'w', encoding='utf-8') as f: f.write(c2)
print('Removed from App.jsx')
