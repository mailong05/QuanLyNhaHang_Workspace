import React, { useState, useEffect } from 'react';
import { Layout, Typography, Button, Divider, Dropdown, Avatar, Space } from 'antd';
import { FireOutlined, UserOutlined, HistoryOutlined, LogoutOutlined } from '@ant-design/icons';
import { Outlet, useNavigate, useLocation, Link } from 'react-router-dom';

const { Header, Content, Footer } = Layout;
const { Title, Text } = Typography;

const CustomerLayout = () => {
  const navigate = useNavigate();
  const location = useLocation();
  
  // Mock Auth State - In real app, read from Context or localStorage
  const [isLoggedIn, setIsLoggedIn] = useState(false); 

  // Auto detect if token exists to flip mock state for testing purposes
  useEffect(() => {
    if (localStorage.getItem('accessToken')) {
        setIsLoggedIn(true);
    }
  }, []);

  const handleLogout = () => {
    setIsLoggedIn(false);
    localStorage.removeItem('accessToken');
    localStorage.removeItem('role');
    navigate('/login');
  };

  const userMenuItems = [
    { key: 'profile', icon: <UserOutlined />, label: 'Hồ sơ của tôi' },
    { key: 'history', icon: <HistoryOutlined />, label: 'Lịch sử đặt bàn' },
    { type: 'divider' },
    { key: 'logout', icon: <LogoutOutlined />, label: 'Đăng xuất', onClick: handleLogout },
  ];

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Header style={{ background: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '0 50px', boxShadow: '0 2px 8px rgba(0,0,0,0.1)', zIndex: 1 }}>
        {/* Cụm Trái: Logo */}
        <div style={{ display: 'flex', alignItems: 'center', cursor: 'pointer' }} onClick={() => navigate('/')}>
          <FireOutlined style={{ fontSize: '28px', color: '#fa8c16', marginRight: '10px' }} />
          <Title level={3} style={{ margin: 0, color: '#fa8c16' }}>Grand Restaurant</Title>
        </div>

        {/* Cụm Phải: Nav & Auth */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
          <Link to="/" style={{ color: location.pathname === '/' ? '#fa8c16' : '#333', fontSize: '16px', fontWeight: 500 }}>
            Trang Chủ
          </Link>
          <Link to="/menu" style={{ color: location.pathname === '/menu' ? '#fa8c16' : '#333', fontSize: '16px', fontWeight: 500 }}>
            Thực Đơn
          </Link>
          
          <Divider type="vertical" style={{ height: '24px', margin: '0' }} />

          {!isLoggedIn ? (
            <Button type="primary" onClick={() => navigate('/login')} style={{ fontSize: '16px' }}>Đăng Nhập</Button>
          ) : (
            <Dropdown menu={{ items: userMenuItems }} placement="bottomRight" trigger={['click']}>
              <div style={{ cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '8px' }}>
                <Avatar icon={<UserOutlined />} style={{ backgroundColor: '#fa8c16' }} />
                <Text strong>Chào, Nguyễn Văn A</Text>
              </div>
            </Dropdown>
          )}
        </div>
      </Header>

      <Content style={{ minHeight: 'calc(100vh - 64px - 70px)' }}>
        <Outlet />
      </Content>

      <Footer style={{ textAlign: 'center', background: '#001529', color: 'white', padding: '24px 50px' }}>
        Grand Restaurant ©{new Date().getFullYear()} Created by Tech Lead
      </Footer>
    </Layout>
  );
};

export default CustomerLayout;
