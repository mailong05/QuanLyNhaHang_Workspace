import React, { useState, useEffect } from 'react';
import { Form, Input, Button, Card, message } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import { useNavigate, Link } from 'react-router-dom';
import apiClient from '../services/apiClient';

// HMR Force Reload
const Login = () => {
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  // Kiểm tra nếu đã đăng nhập thì đá văng ra khỏi trang Login
  useEffect(() => {
    const token = sessionStorage.getItem('accessToken');
    if (token) {
      const role = sessionStorage.getItem('role');
      if (['ROLE_ADMIN', 'ROLE_STAFF', 'ADMIN', 'STAFF', 'NHAN_VIEN', 'QUAN_LY'].includes(role)) {
        navigate('/admin');
      } else {
        navigate('/');
      }
    }
  }, [navigate]);

  const onFinish = async (values) => {
    setLoading(true);
    try {
      // apiClient đã được bọc interceptor, nó sẽ trả về trực tiếp response.data.data nếu thành công
      const data = await apiClient.post('/api/auth/login', {
        username: values.username,
        password: values.password,
      });
      
      // Giả định backend trả về token và role, hoặc fallback
      const role = data.role || 'ROLE_CUSTOMER'; // Nếu không có, gán mặc định là CUSTOMER
      
      // Lưu token và role vào sessionStorage
      sessionStorage.setItem('accessToken', data.accessToken);
      sessionStorage.setItem('role', role);
      
      message.success('Đăng nhập thành công!');
      
      // Điều hướng dựa trên role
      if (role === 'ROLE_CUSTOMER' || role === 'CUSTOMER') {
        navigate('/');
      } else if (['ROLE_ADMIN', 'ROLE_STAFF', 'ADMIN', 'STAFF', 'NHAN_VIEN', 'QUAN_LY'].includes(role)) {
        navigate('/admin');
      } else {
        navigate('/');
      }
    } catch (error) {
      // Hiển thị thông báo lỗi từ Backend
      message.error(error.message || 'Đăng nhập thất bại, vui lòng kiểm tra lại thông tin.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      height: '100vh',
      backgroundColor: '#f0f2f5'
    }}>
      <Card 
        title={<h2 style={{ textAlign: 'center', margin: 0 }}>Đăng Nhập</h2>} 
        style={{ 
          width: 400, 
          boxShadow: '0 4px 12px rgba(0,0,0,0.1)',
          borderRadius: '8px'
        }}
      >
        <Form
          name="login_form"
          initialValues={{ remember: true }}
          onFinish={onFinish}
          layout="vertical"
          size="large"
        >
          <Form.Item
            name="username"
            rules={[{ required: true, message: 'Vui lòng nhập tên đăng nhập!' }]}
          >
            <Input prefix={<UserOutlined />} placeholder="Tên đăng nhập" />
          </Form.Item>

          <Form.Item
            name="password"
            rules={[{ required: true, message: 'Vui lòng nhập mật khẩu!' }]}
          >
            <Input.Password prefix={<LockOutlined />} placeholder="Mật khẩu" />
          </Form.Item>

          <Form.Item style={{ marginBottom: '12px' }}>
            <Button type="primary" htmlType="submit" style={{ width: '100%' }} loading={loading}>
              Đăng nhập
            </Button>
          </Form.Item>
          
          <div style={{ textAlign: 'center' }}>
            <span style={{ color: '#000000a6' }}>Bạn chưa có tài khoản? </span>
            <Link to="/register">Đăng ký ngay</Link>
          </div>
        </Form>
      </Card>
    </div>
  );
};

export default Login;
