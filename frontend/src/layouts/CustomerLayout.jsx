import React from 'react';
import { Layout, Menu, Dropdown, Button, Avatar } from 'antd';
import { FireOutlined, UserOutlined, HistoryOutlined, LogoutOutlined, HomeOutlined, CoffeeOutlined } from '@ant-design/icons';
import { Outlet, useNavigate, useLocation, Link } from 'react-router-dom';

const { Header, Content, Footer } = Layout;

const CustomerLayout = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('role');
    navigate('/login');
  };

  const userMenu = {
    items: [
      {
        key: 'profile',
        icon: <UserOutlined />,
        label: 'Hồ sơ của tôi',
        onClick: () => navigate('/profile')
      },
      {
        key: 'bookings',
        icon: <HistoryOutlined />,
        label: 'Lịch sử đặt bàn',
        onClick: () => navigate('/my-bookings')
      },
      {
        type: 'divider',
      },
      {
        key: 'logout',
        icon: <LogoutOutlined />,
        label: 'Đăng xuất',
        danger: true,
        onClick: handleLogout
      },
    ]
  };

  // Giả lập trạng thái đăng nhập
  const isLoggedIn = !!localStorage.getItem('accessToken');
  const role = localStorage.getItem('role') || 'CUSTOMER';

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Header style={{ 
        background: '#fff', 
        display: 'flex', 
        alignItems: 'center', 
        justifyContent: 'space-between', 
        padding: '0 50px', 
        boxShadow: '0 2px 8px rgba(0,0,0,0.1)', 
        position: 'sticky', 
        top: 0, 
        zIndex: 10 
      }}>
        <div style={{ display: 'flex', alignItems: 'center', cursor: 'pointer' }} onClick={() => navigate('/')}>
          <FireOutlined style={{ fontSize: '28px', color: '#fa8c16', marginRight: '10px' }} />
          <h3 style={{ margin: 0, color: '#fa8c16', fontSize: '20px' }}>Grand Restaurant</h3>
        </div>

        <div style={{ display: 'flex', alignItems: 'center', gap: '24px' }}>
          <Menu 
            mode="horizontal" 
            selectedKeys={[location.pathname]} 
            style={{ borderBottom: 'none', minWidth: '300px', justifyContent: 'flex-end' }}
            items={[
              { key: '/', icon: <HomeOutlined />, label: <Link to="/">Trang chủ</Link> },
              { key: '/#menu', icon: <CoffeeOutlined />, label: 'Thực đơn' }
            ]}
          />
          
          {isLoggedIn ? (
            <Dropdown menu={userMenu} placement="bottomRight" trigger={['click']}>
              <div style={{ cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '8px' }}>
                <Avatar style={{ backgroundColor: '#1890ff' }} icon={<UserOutlined />} />
                <span style={{ fontWeight: 500 }}>{role === 'MANAGER' ? 'Quản Lý' : 'Khách Hàng'}</span>
              </div>
            </Dropdown>
          ) : (
            <div style={{ display: 'flex', gap: '12px' }}>
              <Button type="text" onClick={() => navigate('/login')}>Đăng nhập</Button>
              <Button type="primary" onClick={() => navigate('/register')}>Đăng ký</Button>
            </div>
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
