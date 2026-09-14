import React from 'react';
import { Form, Input, Button, Card, Divider, message, InputNumber, Switch } from 'antd';
import { SaveOutlined } from '@ant-design/icons';

const SystemSettings = () => {
  const [form] = Form.useForm();

  const handleSave = () => {
    // Fake loading or just show message
    message.warning('Tính năng cấu hình đang được phát triển. Vui lòng thử lại trong bản cập nhật sau.');
  };

  return (
    <div>
      <div style={{ marginBottom: 16 }}>
        <h2>Cài Đặt Hệ Thống</h2>
      </div>

      <Card>
        <Form
          form={form}
          layout="vertical"
          onFinish={handleSave}
          initialValues={{
            restaurantName: 'Nhà hàng VerWeb',
            hotline: '1900 8080',
            address: '123 Đường Cầu Giấy, Hà Nội',
            vat: 8,
            serviceCharge: 0,
            autoCancel: true,
            cancelTime: 30
          }}
        >
          <Divider orientation="left">Thông tin chung</Divider>
          <div style={{ display: 'flex', gap: '24px', flexWrap: 'wrap' }}>
            <Form.Item name="restaurantName" label="Tên nhà hàng" style={{ flex: 1, minWidth: '250px' }}>
              <Input />
            </Form.Item>
            <Form.Item name="hotline" label="Hotline liên hệ" style={{ flex: 1, minWidth: '250px' }}>
              <Input />
            </Form.Item>
            <Form.Item name="address" label="Địa chỉ" style={{ flex: 2, minWidth: '350px' }}>
              <Input />
            </Form.Item>
          </div>

          <Divider orientation="left">Cấu hình Hóa đơn / Phụ phí</Divider>
          <div style={{ display: 'flex', gap: '24px' }}>
            <Form.Item name="vat" label="Thuế VAT mặc định (%)">
              <InputNumber min={0} max={100} style={{ width: 150 }} />
            </Form.Item>
            <Form.Item name="serviceCharge" label="Phí dịch vụ mặc định (%)">
              <InputNumber min={0} max={100} style={{ width: 150 }} />
            </Form.Item>
          </div>

          <Divider orientation="left">Tự động hóa (Đặt bàn)</Divider>
          <div style={{ display: 'flex', gap: '24px', alignItems: 'center' }}>
            <Form.Item name="autoCancel" label="Tự động hủy phiếu quá giờ" valuePropName="checked">
              <Switch />
            </Form.Item>
            <Form.Item name="cancelTime" label="Thời gian chờ tối đa (Phút)">
              <InputNumber min={5} max={120} style={{ width: 150 }} />
            </Form.Item>
          </div>

          <Divider />
          <Form.Item>
            <Button type="primary" htmlType="submit" icon={<SaveOutlined />} size="large">
              Lưu Cấu Hình
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  );
};

export default SystemSettings;
