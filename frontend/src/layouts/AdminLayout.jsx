import React, { useState } from 'react';
import { Layout, Menu, Button } from 'antd';
import { DashboardOutlined, TableOutlined, LogoutOutlined } from '@ant-design/icons';
import { Outlet, useNavigate, useLocation } from 'react-router-dom';

const { Header, Sider, Content } = Layout;

const AdminLayout = () => {
  const [collapsed, setCollapsed] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    // Xóa token khỏi localStorage
    localStorage.removeItem('accessToken');
    // Điều hướng về trang đăng nhập
    navigate('/login');
  };

  const menuItems = [
    {
      key: '/admin',
      icon: <DashboardOutlined />,
      label: 'Dashboard',
    },
    {
      key: '/admin/tables',
      icon: <TableOutlined />,
      label: 'Quản lý Bàn',
    },
  ];

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider 
        collapsible 
        collapsed={collapsed} 
        onCollapse={(value) => setCollapsed(value)}
        theme="dark"
      >
        <div style={{ 
          height: 32, 
          margin: 16, 
          background: 'rgba(255, 255, 255, 0.2)',
          borderRadius: 6
        }} />
        <Menu 
          theme="dark" 
          defaultSelectedKeys={[location.pathname]} 
          mode="inline" 
          items={menuItems} 
          onClick={({ key }) => navigate(key)}
        />
      </Sider>
      <Layout>
        <Header style={{ 
          padding: '0 24px', 
          background: '#fff', 
          display: 'flex', 
          justifyContent: 'flex-end', 
          alignItems: 'center',
          boxShadow: '0 1px 4px rgba(0,21,41,.08)'
        }}>
          <Button 
            type="primary" 
            danger 
            icon={<LogoutOutlined />} 
            onClick={handleLogout}
          >
            Đăng xuất
          </Button>
        </Header>
        <Content style={{ margin: '16px' }}>
          <div style={{ padding: 24, minHeight: 360, background: '#fff', borderRadius: 8 }}>
            <Outlet />
          </div>
        </Content>
      </Layout>
    </Layout>
  );
};

export default AdminLayout;
