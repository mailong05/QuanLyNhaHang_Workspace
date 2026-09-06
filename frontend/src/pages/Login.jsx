import React, { useState, useEffect } from 'react';
import { Form, Input, Button, Card, message, Modal, Typography } from 'antd';
import { UserOutlined, LockOutlined, SafetyCertificateOutlined, MailOutlined } from '@ant-design/icons';
import { useNavigate, Link } from 'react-router-dom';
import apiClient from '../services/apiClient';

const { Text } = Typography;

const Login = () => {
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  // Forgot Password State
  const [isForgotVisible, setIsForgotVisible] = useState(false);
  const [forgotStep, setForgotStep] = useState(1);
  const [countdown, setCountdown] = useState(0);
  const [verifyLoading, setVerifyLoading] = useState(false);
  const [resetLoading, setResetLoading] = useState(false);
  const [forgotForm] = Form.useForm();
  
  // Lấy giá trị input realtime để disable/enable nút
  const usernameValue = Form.useWatch('username', forgotForm);
  const emailOrPhoneValue = Form.useWatch('emailOrPhone', forgotForm);
  const codeValue = Form.useWatch('code', forgotForm);

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

  useEffect(() => {
    let timer;
    if (countdown > 0) {
      timer = setInterval(() => {
        setCountdown((prev) => prev - 1);
      }, 1000);
    }
    return () => clearInterval(timer);
  }, [countdown]);

  const onFinish = async (values) => {
    setLoading(true);
    try {
      const data = await apiClient.post('/api/auth/login', {
        username: values.username,
        password: values.password,
      });
      
      const role = data.role || 'ROLE_CUSTOMER';
      sessionStorage.setItem('accessToken', data.accessToken);
      sessionStorage.setItem('role', role);
      
      message.success('Đăng nhập thành công!');
      
      if (role === 'ROLE_CUSTOMER' || role === 'CUSTOMER') {
        navigate('/');
      } else if (['ROLE_ADMIN', 'ROLE_STAFF', 'ADMIN', 'STAFF', 'NHAN_VIEN', 'QUAN_LY'].includes(role)) {
        navigate('/admin');
      } else {
        navigate('/');
      }
    } catch (error) {
      message.error(error.message || 'Đăng nhập thất bại, vui lòng kiểm tra lại thông tin.');
    } finally {
      setLoading(false);
    }
  };

  const handleSendCode = async () => {
    try {
      // 1. Kiểm tra validate form rỗng trên frontend
      const values = await forgotForm.validateFields(['username', 'emailOrPhone']);
      
      // 2. Gọi API kiểm tra tính hợp lệ của tài khoản trước khi gửi mã
      setVerifyLoading(true);
      await apiClient.post('/api/auth/verify-account', {
        username: values.username,
        emailOrPhone: values.emailOrPhone
      });
      
      // 3. Nếu thành công (API không throw lỗi) thì bắt đầu đếm ngược
      setCountdown(60);
      message.success('Mã xác nhận đã được gửi! (Hệ thống giả lập: Nhập 6 số bất kỳ)');
    } catch (error) {
      if (error.errorFields) {
        // Lỗi validate form của antd, không làm gì thêm
      } else {
        message.error(error.message || 'Không tìm thấy thông tin tài khoản hoặc số điện thoại/email không khớp.');
      }
    } finally {
      setVerifyLoading(false);
    }
  };

  const handleVerifyCode = () => {
    if (codeValue && codeValue.length === 6) {
      setForgotStep(2);
    } else {
      message.error('Vui lòng nhập mã xác nhận hợp lệ (đúng 6 ký tự)');
    }
  };

  const onForgotFinish = async (values) => {
    if (values.newPassword !== values.confirmPassword) {
      message.error('Mật khẩu xác nhận không khớp!');
      return;
    }
    setResetLoading(true);
    try {
      await apiClient.post('/api/auth/forgot-password', {
        username: values.username,
        emailOrPhone: values.emailOrPhone,
        newPassword: values.newPassword
      });
      message.success('Đổi mật khẩu thành công! Bạn có thể đăng nhập bằng mật khẩu mới.');
      setIsForgotVisible(false);
      forgotForm.resetFields();
      setForgotStep(1);
      setCountdown(0);
    } catch (error) {
      message.error(error.message || 'Có lỗi xảy ra, không thể đổi mật khẩu.');
    } finally {
      setResetLoading(false);
    }
  };

  const closeForgotModal = () => {
    setIsForgotVisible(false);
    forgotForm.resetFields();
    setForgotStep(1);
    setCountdown(0);
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
            style={{ marginBottom: '10px' }}
          >
            <Input.Password prefix={<LockOutlined />} placeholder="Mật khẩu" />
          </Form.Item>

          <div style={{ textAlign: 'right', marginBottom: '24px' }}>
            <a onClick={() => setIsForgotVisible(true)}>Quên mật khẩu?</a>
          </div>

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

      <Modal
        title="Khôi Phục Mật Khẩu"
        open={isForgotVisible}
        onCancel={closeForgotModal}
        footer={null}
        destroyOnClose
      >
        <Form
          form={forgotForm}
          layout="vertical"
          onFinish={onForgotFinish}
        >
          {forgotStep === 1 && (
            <>
              <Form.Item
                name="username"
                label="Tên đăng nhập"
                rules={[{ required: true, message: 'Vui lòng nhập tên đăng nhập!' }]}
              >
                <Input prefix={<UserOutlined />} placeholder="Nhập tên đăng nhập..." />
              </Form.Item>

              <Form.Item
                name="emailOrPhone"
                label="Số điện thoại hoặc Email"
                rules={[{ required: true, message: 'Vui lòng nhập số điện thoại hoặc email!' }]}
              >
                <Input prefix={<MailOutlined />} placeholder="Nhập số điện thoại hoặc email..." />
              </Form.Item>

              <div style={{ display: 'flex', gap: '10px', marginBottom: '24px' }}>
                <Button 
                  type="default" 
                  onClick={handleSendCode} 
                  disabled={countdown > 0 || !usernameValue || !emailOrPhoneValue}
                  loading={verifyLoading}
                  style={{ flex: 1 }}
                >
                  {countdown > 0 ? `Gửi lại mã (${countdown}s)` : 'Gửi mã xác nhận'}
                </Button>
              </div>

              {countdown > 0 && (
                <>
                  <Form.Item
                    name="code"
                    label="Mã xác nhận (6 số)"
                    rules={[{ required: true, min: 6, max: 6, message: 'Vui lòng nhập đúng 6 số!' }]}
                  >
                    <Input prefix={<SafetyCertificateOutlined />} placeholder="Nhập mã 6 số..." maxLength={6} />
                  </Form.Item>
                  <Button type="primary" onClick={handleVerifyCode} block disabled={!codeValue || codeValue.length < 6}>
                    Xác nhận mã
                  </Button>
                </>
              )}
            </>
          )}

          {forgotStep === 2 && (
            <>
              <Text type="success" style={{ display: 'block', marginBottom: '16px' }}>
                Mã xác nhận hợp lệ. Vui lòng nhập mật khẩu mới.
              </Text>
              
              <Form.Item style={{ display: 'none' }} name="username">
                  <Input />
              </Form.Item>
              <Form.Item style={{ display: 'none' }} name="emailOrPhone">
                  <Input />
              </Form.Item>

              <Form.Item
                name="newPassword"
                label="Mật khẩu mới"
                rules={[{ required: true, message: 'Vui lòng nhập mật khẩu mới!' }]}
              >
                <Input.Password prefix={<LockOutlined />} placeholder="Mật khẩu mới" />
              </Form.Item>

              <Form.Item
                name="confirmPassword"
                label="Xác nhận mật khẩu mới"
                rules={[{ required: true, message: 'Vui lòng xác nhận mật khẩu mới!' }]}
              >
                <Input.Password prefix={<LockOutlined />} placeholder="Nhập lại mật khẩu mới" />
              </Form.Item>

              <Button type="primary" htmlType="submit" block loading={resetLoading}>
                Đổi mật khẩu
              </Button>
            </>
          )}
        </Form>
      </Modal>
    </div>
  );
};

export default Login;
