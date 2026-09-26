import React, { useState } from 'react';
import { Form, Input, Button, Card, message, Typography } from 'antd';
import { UserOutlined, LockOutlined, MailOutlined, PhoneOutlined } from '@ant-design/icons';
import { useNavigate, Link } from 'react-router-dom';

const { Title, Text } = Typography;

const Register = () => {
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const onFinish = (values) => {
    setLoading(true);
    // Mock API call
    setTimeout(() => {
      setLoading(false);
      message.success('Đăng ký tài khoản thành công! Vui lòng đăng nhập.');
      navigate('/login');
    }, 1000);
  };

  return (
    <div style={{
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      minHeight: '100vh',
      backgroundColor: '#f0f2f5',
      padding: '24px'
    }}>
      <Card 
        style={{ 
          width: 450, 
          boxShadow: '0 4px 12px rgba(0,0,0,0.1)',
          borderRadius: '8px'
        }}
      >
        <div style={{ textAlign: 'center', marginBottom: '24px' }}>
          <Title level={2} style={{ margin: 0 }}>Đăng Ký Tài Khoản</Title>
          <Text type="secondary">Trở thành thành viên của Ha Long Restaurant</Text>
        </div>

        <Form
          name="register_form"
          onFinish={onFinish}
          layout="vertical"
          size="large"
        >
          <Form.Item
            name="username"
            label="Tên đăng nhập"
            rules={[
              { required: true, message: 'Vui lòng nhập tên đăng nhập!' },
              { pattern: /^\S+$/, message: 'Tên đăng nhập không được chứa khoảng trắng!' }
            ]}
          >
            <Input prefix={<UserOutlined />} placeholder="Ví dụ: nguyenvana" />
          </Form.Item>

          <Form.Item
            name="hoTen"
            label="Họ và tên"
            rules={[{ required: true, message: 'Vui lòng nhập họ tên!' }]}
          >
            <Input prefix={<UserOutlined />} placeholder="Nguyễn Văn A" />
          </Form.Item>

          <Form.Item
            name="sdt"
            label="Số điện thoại"
            rules={[
              { required: true, message: 'Vui lòng nhập số điện thoại!' },
              { pattern: /^[0-9]{10,11}$/, message: 'Số điện thoại không hợp lệ!' }
            ]}
          >
            <Input prefix={<PhoneOutlined />} placeholder="0901234567" />
          </Form.Item>

          <Form.Item
            name="email"
            label="Email"
            rules={[
              { required: true, message: 'Vui lòng nhập email!' },
              { type: 'email', message: 'Email không hợp lệ!' }
            ]}
          >
            <Input prefix={<MailOutlined />} placeholder="email@example.com" />
          </Form.Item>

          <Form.Item
            name="password"
            label="Mật khẩu"
            rules={[{ required: true, message: 'Vui lòng nhập mật khẩu!' }]}
          >
            <Input.Password prefix={<LockOutlined />} placeholder="Mật khẩu (ít nhất 6 ký tự)" />
          </Form.Item>

          <Form.Item
            name="confirmPassword"
            label="Xác nhận mật khẩu"
            dependencies={['password']}
            rules={[
              { required: true, message: 'Vui lòng xác nhận mật khẩu!' },
              ({ getFieldValue }) => ({
                validator(_, value) {
                  if (!value || getFieldValue('password') === value) {
                    return Promise.resolve();
                  }
                  return Promise.reject(new Error('Mật khẩu xác nhận không khớp!'));
                },
              }),
            ]}
          >
            <Input.Password prefix={<LockOutlined />} placeholder="Nhập lại mật khẩu" />
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit" style={{ width: '100%' }} loading={loading}>
              Đăng Ký
            </Button>
          </Form.Item>
          
          <div style={{ textAlign: 'center' }}>
            <Text>Bạn đã có tài khoản? <Link to="/login">Đăng nhập ngay</Link></Text>
          </div>
        </Form>
      </Card>
    </div>
  );
};

export default Register;
