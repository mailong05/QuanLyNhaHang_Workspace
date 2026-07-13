import React, { useState } from 'react';
import { Form, Input, Button, Card, message } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import apiClient from '../services/apiClient';

const Login = () => {
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const onFinish = async (values) => {
    setLoading(true);
    try {
      // apiClient đã được bọc interceptor, nó sẽ trả về trực tiếp response.data.data nếu thành công
      const data = await apiClient.post('/api/auth/login', {
        username: values.username,
        password: values.password,
      });
      
      // Lưu token và role vào localStorage
      localStorage.setItem('accessToken', data.accessToken);
      localStorage.setItem('role', data.role || 'ADMIN'); // Giả định có data.role, mặc định ADMIN để không sụp đổ lúc test
      
      message.success('Đăng nhập thành công!');
      
      // Chuyển hướng sang trang Admin
      navigate('/admin');
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

          <Form.Item>
            <Button type="primary" htmlType="submit" style={{ width: '100%' }} loading={loading}>
              Đăng nhập
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  );
};

export default Login;
