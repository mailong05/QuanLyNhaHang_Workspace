import React, { useState } from 'react';
import { Typography, Row, Col, Card, Button, Table, Modal, Form, InputNumber, Input, Tag, message } from 'antd';
import { LoginOutlined, LogoutOutlined } from '@ant-design/icons';

const { Title, Text } = Typography;

const mockShiftHistory = [
  { key: '1', caLamViec: 'Ca Sáng (06:00 - 14:00)', nhanVien: 'Nguyễn Văn A', gioVao: '05:50:00', gioRa: '14:05:00', tienDauCa: 500000, tienKetCa: 3200000, trangThai: 'Đã đóng' },
  { key: '2', caLamViec: 'Ca Chiều (14:00 - 22:00)', nhanVien: 'Trần Thị B', gioVao: '13:55:00', gioRa: '22:15:00', tienDauCa: 500000, tienKetCa: 4800000, trangThai: 'Đã đóng' },
  { key: '3', caLamViec: 'Ca Sáng (06:00 - 14:00)', nhanVien: 'Lê Văn C', gioVao: '05:58:00', gioRa: null, tienDauCa: 500000, tienKetCa: null, trangThai: 'Đang mở' },
];

const historyColumns = [
  { title: 'Ca làm việc', dataIndex: 'caLamViec', key: 'caLamViec', strong: true },
  { title: 'Nhân viên', dataIndex: 'nhanVien', key: 'nhanVien' },
  { title: 'Giờ vào', dataIndex: 'gioVao', key: 'gioVao' },
  { title: 'Giờ ra', dataIndex: 'gioRa', key: 'gioRa', render: val => val ? val : '-' },
  { title: 'Tiền đầu ca', dataIndex: 'tienDauCa', key: 'tienDauCa', render: val => `${val?.toLocaleString('vi-VN')} đ` },
  { title: 'Tiền kết ca', dataIndex: 'tienKetCa', key: 'tienKetCa', render: val => val ? `${val.toLocaleString('vi-VN')} đ` : '-' },
  { title: 'Trạng thái', dataIndex: 'trangThai', key: 'trangThai', render: val => (
    <Tag color={val === 'Đang mở' ? 'processing' : 'default'}>{val}</Tag>
  )},
];

const ShiftManagement = () => {
  const [isClockInVisible, setIsClockInVisible] = useState(false);
  const [isClockOutVisible, setIsClockOutVisible] = useState(false);
  const [formClockIn] = Form.useForm();
  const [formClockOut] = Form.useForm();

  const handleClockIn = () => {
    formClockIn.validateFields().then(values => {
      message.success(`Đã vào ca thành công với số tiền đầu ca: ${values.tienDauCa.toLocaleString('vi-VN')} đ`);
      setIsClockInVisible(false);
      formClockIn.resetFields();
    });
  };

  const handleClockOut = () => {
    formClockOut.validateFields().then(values => {
      message.success(`Đã kết ca thành công! Tiền thực tế: ${values.tienThucTe.toLocaleString('vi-VN')} đ`);
      setIsClockOutVisible(false);
      formClockOut.resetFields();
    });
  };

  const systemMoney = 3500000; // Mock tiền hệ thống tính

  return (
    <div style={{ padding: '24px', background: '#f0f2f5', minHeight: '100vh' }}>
      <Title level={2} style={{ marginBottom: '24px' }}>Quản lý Giao Ca</Title>

      <Row gutter={[24, 24]} style={{ marginBottom: '24px' }}>
        <Col xs={24} sm={12}>
          <Button 
            type="primary" 
            icon={<LoginOutlined />} 
            size="large" 
            block 
            style={{ height: '80px', fontSize: '24px', backgroundColor: '#52c41a', borderColor: '#52c41a', borderRadius: '12px' }}
            onClick={() => setIsClockInVisible(true)}
          >
            VÀO CA
          </Button>
        </Col>
        <Col xs={24} sm={12}>
          <Button 
            type="primary" 
            danger 
            icon={<LogoutOutlined />} 
            size="large" 
            block 
            style={{ height: '80px', fontSize: '24px', borderRadius: '12px' }}
            onClick={() => setIsClockOutVisible(true)}
          >
            KẾT CA
          </Button>
        </Col>
      </Row>

      <Card title="Lịch sử Ca Làm Việc" bordered={false} style={{ borderRadius: '12px', boxShadow: '0 2px 8px rgba(0,0,0,0.05)' }}>
        <Table 
          columns={historyColumns} 
          dataSource={mockShiftHistory} 
          pagination={false}
          size="middle"
        />
      </Card>

      {/* Modal Vào Ca */}
      <Modal
        title="Xác Nhận Vào Ca"
        open={isClockInVisible}
        onCancel={() => setIsClockInVisible(false)}
        onOk={handleClockIn}
        okText="Bắt đầu ca"
        cancelText="Hủy"
      >
        <Form form={formClockIn} layout="vertical" style={{ marginTop: '16px' }}>
          <Form.Item 
            name="tienDauCa" 
            label="Tiền mặt đầu ca (Tiền lẻ)" 
            rules={[{ required: true, message: 'Vui lòng nhập tiền đầu ca!' }]}
          >
            <InputNumber 
              style={{ width: '100%' }} 
              size="large" 
              formatter={value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
              parser={value => value.replace(/\$\s?|(,*)/g, '')}
              min={0}
              placeholder="VD: 500,000"
            />
          </Form.Item>
        </Form>
      </Modal>

      {/* Modal Kết Ca */}
      <Modal
        title="Xác Nhận Kết Ca"
        open={isClockOutVisible}
        onCancel={() => setIsClockOutVisible(false)}
        onOk={handleClockOut}
        okText="Đóng Ca"
        cancelText="Hủy"
        okButtonProps={{ danger: true }}
      >
        <div style={{ marginBottom: '16px', padding: '12px', background: '#e6f7ff', borderRadius: '8px', border: '1px solid #91d5ff' }}>
          <Text strong>Tổng tiền hệ thống ghi nhận:</Text>
          <br/>
          <Text strong style={{ fontSize: '24px', color: '#1890ff' }}>{systemMoney.toLocaleString('vi-VN')} đ</Text>
        </div>
        
        <Form form={formClockOut} layout="vertical">
          <Form.Item 
            name="tienThucTe" 
            label="Tiền mặt thực tế đếm được" 
            rules={[{ required: true, message: 'Vui lòng nhập số tiền thực tế đếm được!' }]}
          >
            <InputNumber 
              style={{ width: '100%' }} 
              size="large" 
              formatter={value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
              parser={value => value.replace(/\$\s?|(,*)/g, '')}
              min={0}
              placeholder="Nhập số tiền thực tế đếm được trong két"
            />
          </Form.Item>
          <Form.Item 
            name="ghiChu" 
            label="Ghi chú giải trình (Bắt buộc nếu có chênh lệch)"
          >
            <Input.TextArea rows={3} placeholder="Ghi chú nếu tiền thực tế bị lệch so với hệ thống..." />
          </Form.Item>
        </Form>
      </Modal>

    </div>
  );
};

export default ShiftManagement;
