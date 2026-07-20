import React, { useState } from 'react';
import { Typography, Card, Tabs, Form, Input, Button, message } from 'antd';
import { UserOutlined, MailOutlined, PhoneOutlined, LockOutlined } from '@ant-design/icons';

const { Title } = Typography;

const Profile = () => {
  const [loadingInfo, setLoadingInfo] = useState(false);
  const [loadingPwd, setLoadingPwd] = useState(false);

  const [formInfo] = Form.useForm();
  const [formPwd] = Form.useForm();

  // Mock initial data
  const initialValues = {
    hoTen: 'Khách Hàng VIP',
    sdt: '0901234567',
    email: 'khachhang@example.com'
  };

  const onUpdateInfo = (values) => {
    setLoadingInfo(true);
    setTimeout(() => {
      message.success('Cập nhật thông tin thành công!');
      setLoadingInfo(false);
    }, 1000);
  };

  const onChangePassword = (values) => {
    setLoadingPwd(true);
    setTimeout(() => {
      message.success('Đổi mật khẩu thành công!');
      formPwd.resetFields();
      setLoadingPwd(false);
    }, 1000);
  };

  const items = [
    {
      key: '1',
      label: 'Thông tin cá nhân',
      children: (
        <Form
          form={formInfo}
          layout="vertical"
          onFinish={onUpdateInfo}
          initialValues={initialValues}
          size="large"
          style={{ maxWidth: '500px', marginTop: '16px' }}
        >
          <Form.Item name="hoTen" label="Họ và tên" rules={[{ required: true, message: 'Vui lòng nhập họ tên!' }]}>
            <Input prefix={<UserOutlined />} />
          </Form.Item>
          <Form.Item name="sdt" label="Số điện thoại" rules={[{ required: true, message: 'Vui lòng nhập SĐT!' }]}>
            <Input prefix={<PhoneOutlined />} />
          </Form.Item>
          <Form.Item name="email" label="Email" rules={[{ required: true, message: 'Vui lòng nhập email!' }, { type: 'email' }]}>
            <Input prefix={<MailOutlined />} />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" loading={loadingInfo}>
              Lưu Thay Đổi
            </Button>
          </Form.Item>
        </Form>
      )
    },
    {
      key: '2',
      label: 'Đổi mật khẩu',
      children: (
        <Form
          form={formPwd}
          layout="vertical"
          onFinish={onChangePassword}
          size="large"
          style={{ maxWidth: '500px', marginTop: '16px' }}
        >
          <Form.Item name="oldPassword" label="Mật khẩu hiện tại" rules={[{ required: true, message: 'Vui lòng nhập mật khẩu cũ!' }]}>
            <Input.Password prefix={<LockOutlined />} />
          </Form.Item>
          <Form.Item name="newPassword" label="Mật khẩu mới" rules={[{ required: true, message: 'Vui lòng nhập mật khẩu mới!' }]}>
            <Input.Password prefix={<LockOutlined />} />
          </Form.Item>
          <Form.Item 
            name="confirmPassword" 
            label="Xác nhận mật khẩu mới" 
            dependencies={['newPassword']}
            rules={[
              { required: true, message: 'Vui lòng xác nhận mật khẩu!' },
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
            <Input.Password prefix={<LockOutlined />} />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" loading={loadingPwd}>
              Cập Nhật Mật Khẩu
            </Button>
          </Form.Item>
        </Form>
      )
    }
  ];

  return (
    <div style={{ maxWidth: '800px', margin: '0 auto', padding: '40px 24px' }}>
      <Title level={2} style={{ marginBottom: '24px' }}>Hồ Sơ Của Tôi</Title>
      <Card style={{ borderRadius: '12px', boxShadow: '0 2px 8px rgba(0,0,0,0.05)' }}>
        <Tabs defaultActiveKey="1" items={items} />
      </Card>
    </div>
  );
};

export default Profile;
