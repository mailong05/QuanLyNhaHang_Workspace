import React, { useState } from 'react';
import { Layout, Menu, Button, Dropdown, Avatar, Typography, Modal, Form, Input, Space } from 'antd';
import { DashboardOutlined, TableOutlined, LogoutOutlined, CoffeeOutlined, TagOutlined, ScheduleOutlined, AppstoreAddOutlined, ClockCircleOutlined, LineChartOutlined, UserOutlined, KeyOutlined } from '@ant-design/icons';
import { Outlet, useNavigate, useLocation } from 'react-router-dom';

const { Header, Sider, Content } = Layout;
const { Text } = Typography;

const AdminLayout = () => {
  const [collapsed, setCollapsed] = useState(false);
  const [isViewProfileModalVisible, setIsViewProfileModalVisible] = useState(false);
  const [isEditProfileModalVisible, setIsEditProfileModalVisible] = useState(false);
  const [isPasswordModalVisible, setIsPasswordModalVisible] = useState(false);
  
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    // Xóa token và role khỏi sessionStorage
    sessionStorage.removeItem('accessToken');
    sessionStorage.removeItem('role');
    // Điều hướng về trang chủ
    navigate('/');
  };

  const role = sessionStorage.getItem('role') || 'STAFF';

  const confirmLogout = () => {
    Modal.confirm({
      title: 'Xác nhận đăng xuất',
      content: 'Bạn có chắc chắn muốn đăng xuất khỏi hệ thống?',
      okText: 'Đăng xuất',
      cancelText: 'Hủy',
      okButtonProps: { danger: true },
      onOk: handleLogout
    });
  };

  const userMenuItems = [
    {
      key: 'profile',
      icon: <UserOutlined />,
      label: 'Thông tin cá nhân',
      onClick: () => setIsViewProfileModalVisible(true)
    },
    {
      key: 'password',
      icon: <KeyOutlined />,
      label: 'Đổi mật khẩu',
      onClick: () => setIsPasswordModalVisible(true)
    },
    {
      type: 'divider'
    },
    {
      key: 'logout',
      icon: <LogoutOutlined style={{ color: 'red' }} />,
      label: <span style={{ color: 'red' }}>Đăng xuất</span>,
      onClick: confirmLogout
    }
  ];

  const menuItems = [
    {
      key: '/admin',
      icon: <DashboardOutlined />,
      label: 'Dashboard',
      roles: ['ADMIN', 'ROLE_ADMIN', 'STAFF', 'ROLE_STAFF', 'NHAN_VIEN', 'QUAN_LY']
    },
    {
      key: '/admin/pos',
      icon: <AppstoreAddOutlined />,
      label: 'Bán Hàng (POS)',
      roles: ['ADMIN', 'ROLE_ADMIN', 'STAFF', 'ROLE_STAFF', 'NHAN_VIEN']
    },
    {
      key: '/admin/bookings',
      icon: <ScheduleOutlined />,
      label: 'Phiếu Đặt Bàn',
      roles: ['ADMIN', 'ROLE_ADMIN', 'STAFF', 'ROLE_STAFF', 'NHAN_VIEN']
    },
    {
      key: '/admin/tables',
      icon: <TableOutlined />,
      label: 'Quản lý Bàn',
      roles: ['ADMIN', 'ROLE_ADMIN', 'STAFF', 'ROLE_STAFF', 'NHAN_VIEN']
    },
    {
      key: '/admin/menu',
      icon: <CoffeeOutlined />,
      label: 'Quản lý Món ăn',
      roles: ['ADMIN', 'ROLE_ADMIN', 'STAFF', 'ROLE_STAFF', 'NHAN_VIEN']
    },
    {
      key: '/admin/vouchers',
      icon: <TagOutlined />,
      label: 'Khuyến Mãi',
      roles: ['ADMIN', 'ROLE_ADMIN']
    },
    {
      key: '/admin/shifts',
      icon: <ClockCircleOutlined />,
      label: 'Giao Ca',
      roles: ['ADMIN', 'ROLE_ADMIN', 'STAFF', 'ROLE_STAFF', 'NHAN_VIEN', 'QUAN_LY']
    },
    {
      key: '/admin/analytics',
      icon: <LineChartOutlined />,
      label: 'Thống Kê',
      roles: ['ADMIN', 'ROLE_ADMIN']
    },
  ];

  // Lọc menu theo role
  const filteredMenu = menuItems.filter(item => item.roles.includes(role));

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
          borderRadius: 6,
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          color: 'white',
          fontWeight: 'bold',
          fontSize: '16px',
          overflow: 'hidden',
          whiteSpace: 'nowrap'
        }}>
          {collapsed ? 'VW' : 'VerWeb'}
        </div>
        <Menu 
          theme="dark" 
          defaultSelectedKeys={[location.pathname]} 
          mode="inline" 
          items={filteredMenu} 
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
          <Dropdown menu={{ items: userMenuItems }} placement="bottomRight" trigger={['click']}>
            <div style={{ cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '8px' }}>
              <Avatar icon={<UserOutlined />} style={{ backgroundColor: '#1890ff' }} />
              <Text strong>Chào, Admin</Text>
            </div>
          </Dropdown>
        </Header>
        <Content style={{ margin: '16px' }}>
          <div style={{ padding: 24, minHeight: 360, background: '#fff', borderRadius: 8 }}>
            <Outlet />
          </div>
        </Content>
      </Layout>

      {/* View Profile Modal */}
      <Modal
        title="Thông tin cá nhân"
        open={isViewProfileModalVisible}
        onCancel={() => setIsViewProfileModalVisible(false)}
        footer={[
          <Button key="close" onClick={() => setIsViewProfileModalVisible(false)}>Đóng</Button>,
          <Button key="edit" type="primary" onClick={() => {
            setIsViewProfileModalVisible(false);
            setIsEditProfileModalVisible(true);
          }}>
            Chỉnh sửa thông tin
          </Button>
        ]}
      >
        <div style={{ padding: '16px 0', fontSize: '16px' }}>
          <p style={{ marginBottom: '12px' }}><strong>Họ và tên:</strong> Admin</p>
          <p style={{ marginBottom: '12px' }}><strong>Số điện thoại:</strong> 0123456789</p>
          <p style={{ marginBottom: '12px' }}><strong>Email:</strong> admin@grandrestaurant.com</p>
          <p style={{ marginBottom: '0' }}><strong>Vai trò:</strong> Quản trị viên</p>
        </div>
      </Modal>

      {/* Edit Profile Modal */}
      <Modal
        title="Chỉnh sửa thông tin cá nhân"
        open={isEditProfileModalVisible}
        onCancel={() => setIsEditProfileModalVisible(false)}
        footer={[
          <Button key="cancel" onClick={() => setIsEditProfileModalVisible(false)}>Hủy</Button>,
          <Button key="submit" type="primary" onClick={() => setIsEditProfileModalVisible(false)}>Lưu thay đổi</Button>
        ]}
      >
        <Form layout="vertical">
          <Form.Item label="Họ và tên" initialValue="Admin">
            <Input />
          </Form.Item>
          <Form.Item label="Số điện thoại" initialValue="0123456789">
            <Input />
          </Form.Item>
          <Form.Item label="Email" initialValue="admin@grandrestaurant.com">
            <Input />
          </Form.Item>
        </Form>
      </Modal>

      {/* Password Modal */}
      <Modal
        title="Đổi mật khẩu"
        open={isPasswordModalVisible}
        onCancel={() => setIsPasswordModalVisible(false)}
        footer={[
          <Button key="cancel" onClick={() => setIsPasswordModalVisible(false)}>Hủy</Button>,
          <Button key="submit" type="primary" onClick={() => setIsPasswordModalVisible(false)}>Cập nhật</Button>
        ]}
      >
        <Form layout="vertical">
          <Form.Item 
            label="Mật khẩu cũ" 
            name="oldPassword"
            rules={[{ required: true, message: 'Vui lòng nhập mật khẩu cũ!' }]}
          >
            <Input.Password />
          </Form.Item>
          <Form.Item 
            label="Mật khẩu mới" 
            name="newPassword"
            rules={[{ required: true, message: 'Vui lòng nhập mật khẩu mới!' }]}
          >
            <Input.Password />
          </Form.Item>
          <Form.Item 
            label="Xác nhận mật khẩu mới" 
            name="confirmPassword"
            dependencies={['newPassword']}
            rules={[
              { required: true, message: 'Vui lòng xác nhận mật khẩu mới!' },
              ({ getFieldValue }) => ({
                validator(_, value) {
                  if (!value || getFieldValue('newPassword') === value) {
                    return Promise.resolve();
                  }
                  return Promise.reject(new Error('Mật khẩu xác nhận không khớp!'));
                },
              }),
            ]}
          >
            <Input.Password />
          </Form.Item>
        </Form>
      </Modal>
    </Layout>
  );
};

export default AdminLayout;
