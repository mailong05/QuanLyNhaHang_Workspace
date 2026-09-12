import os
import re

path = 'C:/QuanLyNhaHang_Workspace/frontend/src/layouts/AdminLayout.jsx'
with open(path, 'r', encoding='utf-8') as f: 
    c = f.read()

# Replace Icon imports
icon_import_pattern = r"import \{([^}]+)\} from '@ant-design/icons';"
match = re.search(icon_import_pattern, c)
if match:
    old_imports = match.group(1)
    new_imports = old_imports + ", SettingOutlined, UsergroupAddOutlined, TeamOutlined"
    c = c.replace(match.group(0), f"import {{{new_imports}}} from '@ant-design/icons';")

# Find menuItems block
menu_start = c.find('const menuItems = [')
menu_end = c.find('const filteredMenu =', menu_start)

if menu_start != -1 and menu_end != -1:
    old_menu_code = c[menu_start:menu_end]
    new_menu_code = """const menuItems = [
    {
      key: 'grp_dashboard',
      label: 'TỔNG QUAN & BÁO CÁO',
      type: 'group',
      roles: ['ADMIN', 'ROLE_ADMIN', 'QUAN_LY'],
      children: [
        { key: '/admin', icon: <DashboardOutlined />, label: 'Dashboard', roles: ['ADMIN', 'ROLE_ADMIN', 'QUAN_LY'] },
        { key: '/admin/statistics', icon: <LineChartOutlined />, label: 'Thống Kê', roles: ['ADMIN', 'ROLE_ADMIN', 'QUAN_LY'] },
      ]
    },
    {
      key: 'grp_operations',
      label: 'VẬN HÀNH QUÁN',
      type: 'group',
      roles: ['ADMIN', 'ROLE_ADMIN', 'STAFF', 'ROLE_STAFF', 'NHAN_VIEN', 'QUAN_LY'],
      children: [
        { key: '/admin/pos', icon: <AppstoreAddOutlined />, label: 'Bán Hàng (POS)', roles: ['ADMIN', 'ROLE_ADMIN', 'STAFF', 'ROLE_STAFF', 'NHAN_VIEN', 'QUAN_LY'] },
        { key: '/admin/bookings', icon: <ScheduleOutlined />, label: 'Phiếu Đặt Bàn', roles: ['ADMIN', 'ROLE_ADMIN', 'STAFF', 'ROLE_STAFF', 'NHAN_VIEN', 'QUAN_LY'] },
        { key: '/admin/invoices', icon: <FileTextOutlined />, label: 'Quản lý Hóa đơn', roles: ['ADMIN', 'ROLE_ADMIN', 'STAFF', 'ROLE_STAFF', 'NHAN_VIEN', 'QUAN_LY'] },
        { key: '/admin/shifts', icon: <ClockCircleOutlined />, label: 'Giao Ca', roles: ['ADMIN', 'ROLE_ADMIN', 'STAFF', 'ROLE_STAFF', 'NHAN_VIEN', 'QUAN_LY'] },
      ]
    },
    {
      key: 'grp_catalog',
      label: 'DANH MỤC HÀNG HÓA',
      type: 'group',
      roles: ['ADMIN', 'ROLE_ADMIN', 'QUAN_LY'],
      children: [
        { 
          key: 'sub_menu', 
          icon: <CoffeeOutlined />, 
          label: 'Thực đơn', 
          roles: ['ADMIN', 'ROLE_ADMIN', 'QUAN_LY'],
          children: [
            { key: '/admin/menu', label: 'Món ăn', roles: ['ADMIN', 'ROLE_ADMIN', 'QUAN_LY'] },
            { key: '/admin/categories', label: 'Danh mục món', roles: ['ADMIN', 'ROLE_ADMIN', 'QUAN_LY'] }
          ]
        },
        { 
          key: 'sub_table', 
          icon: <TableOutlined />, 
          label: 'Sơ đồ Bàn', 
          roles: ['ADMIN', 'ROLE_ADMIN', 'QUAN_LY'],
          children: [
            { key: '/admin/tables', label: 'Quản lý Bàn', roles: ['ADMIN', 'ROLE_ADMIN', 'QUAN_LY'] },
            { key: '/admin/areas', label: 'Quản lý Khu vực', roles: ['ADMIN', 'ROLE_ADMIN', 'QUAN_LY'] }
          ]
        },
      ]
    },
    {
      key: 'grp_crm',
      label: 'KHÁCH HÀNG & MARKETING',
      type: 'group',
      roles: ['ADMIN', 'ROLE_ADMIN', 'STAFF', 'ROLE_STAFF', 'NHAN_VIEN', 'QUAN_LY'],
      children: [
        { key: '/admin/customers', icon: <TeamOutlined />, label: 'Khách hàng', roles: ['ADMIN', 'ROLE_ADMIN', 'STAFF', 'ROLE_STAFF', 'NHAN_VIEN', 'QUAN_LY'] },
        { key: '/admin/vouchers', icon: <TagOutlined />, label: 'Khuyến Mãi', roles: ['ADMIN', 'ROLE_ADMIN', 'QUAN_LY'] },
      ]
    },
    {
      key: 'grp_system',
      label: 'NHÂN SỰ & HỆ THỐNG',
      type: 'group',
      roles: ['ADMIN', 'ROLE_ADMIN'],
      children: [
        { key: '/admin/employees', icon: <UsergroupAddOutlined />, label: 'Quản lý Nhân viên', roles: ['ADMIN', 'ROLE_ADMIN'] },
        { key: '/admin/settings', icon: <SettingOutlined />, label: 'Cài đặt hệ thống', roles: ['ADMIN', 'ROLE_ADMIN'] },
      ]
    }
  ];

  """
    c = c.replace(old_menu_code, new_menu_code)

# Replace filter logic
old_filter = "const filteredMenu = menuItems.filter(item => item.roles.includes(role));"
new_filter = """const filterMenuByRole = (items, userRole) => {
    return items
      .filter(item => item.roles.includes(userRole))
      .map(item => {
        if (item.children) {
          return { ...item, children: filterMenuByRole(item.children, userRole) };
        }
        return item;
      });
  };
  const filteredMenu = filterMenuByRole(menuItems, role);"""
c = c.replace(old_filter, new_filter)

with open(path, 'w', encoding='utf-8') as f: 
    f.write(c)
print('Patched menu successfully')
