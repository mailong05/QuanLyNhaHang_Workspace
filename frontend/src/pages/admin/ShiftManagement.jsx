import React, { useState, useEffect } from 'react';
import { Typography, Row, Col, Card, Button, Table, Modal, Form, InputNumber, Input, Tag, message } from 'antd';
import { LoginOutlined, LogoutOutlined } from '@ant-design/icons';
import axios from 'axios';

const { Title, Text } = Typography;

const historyColumns = [
  { title: 'Nhân viên', dataIndex: 'hoTenNV', key: 'hoTenNV' },
  { title: 'Ca', dataIndex: 'tenCa', key: 'tenCa' },
  { title: 'Giờ vào', dataIndex: 'thoiGianVaoCa', key: 'thoiGianVaoCa', render: val => new Date(val).toLocaleString('vi-VN') },
  { title: 'Giờ ra', dataIndex: 'thoiGianKetCa', key: 'thoiGianKetCa', render: val => val ? new Date(val).toLocaleString('vi-VN') : '-' },
  { title: 'Tiền đầu ca', dataIndex: 'tienBanDau', key: 'tienBanDau', render: val => `${val?.toLocaleString('vi-VN')} đ` },
  { title: 'Tiền kết ca', dataIndex: 'tienKetCa', key: 'tienKetCa', render: val => val ? `${val.toLocaleString('vi-VN')} đ` : '-' },
  { title: 'Trạng thái', dataIndex: 'trangThai', key: 'trangThai', render: val => (
    <Tag color={val === 'DANG_LAM_VIEC' ? 'processing' : 'default'}>{val === 'DANG_LAM_VIEC' ? 'Đang làm việc' : 'Đã kết ca'}</Tag>
  )},
];

const DENOMINATIONS = [500000, 200000, 100000, 50000, 20000, 10000, 5000, 2000, 1000];

const ShiftManagement = () => {
  const [isClockInVisible, setIsClockInVisible] = useState(false);
  const [isClockOutVisible, setIsClockOutVisible] = useState(false);
  const [formClockIn] = Form.useForm();
  const [formClockOut] = Form.useForm();
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(false);
  const [systemMoney, setSystemMoney] = useState(0);

  // States to keep track of denomination quantities
  const [inDenoms, setInDenoms] = useState({});
  const [outDenoms, setOutDenoms] = useState({});

  const calculateTotal = (denoms) => {
    return DENOMINATIONS.reduce((sum, den) => sum + (denoms[den] || 0) * den, 0);
  };

  const totalIn = calculateTotal(inDenoms);
  const totalOut = calculateTotal(outDenoms);

  const getHeaders = () => {
    const token = localStorage.getItem('accessToken');
    return token ? { Authorization: `Bearer ${token}` } : {};
  };

  const fetchHistory = async () => {
    setLoading(true);
    try {
      const res = await axios.get('http://localhost:8080/api/v1/giao-ca', { headers: getHeaders() });
      setHistory(res.data.data.content);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const fetchCurrentShift = async () => {
    try {
      const res = await axios.get('http://localhost:8080/api/v1/giao-ca/hien-tai', { headers: getHeaders() });
      if (res.data && res.data.data) {
        setSystemMoney(res.data.data.tienHeThong);
      }
    } catch (e) {
        setSystemMoney(0);
    }
  };

  useEffect(() => {
    fetchHistory();
    fetchCurrentShift();
  }, []);

  const handleClockIn = () => {
    if (totalIn === 0) {
       message.error('Vui lòng nhập số tờ tiền để tính tiền đầu ca!');
       return;
    }
    axios.post('http://localhost:8080/api/v1/giao-ca/vao-ca', { tienBanDau: totalIn }, { headers: getHeaders() })
      .then(() => {
        message.success(`Đã vào ca thành công với số tiền đầu ca: ${totalIn.toLocaleString('vi-VN')} đ`);
        setIsClockInVisible(false);
        setInDenoms({});
        formClockIn.resetFields();
        fetchHistory();
        fetchCurrentShift();
      }).catch(e => {
        message.error(e.response?.data?.message || 'Lỗi khi vào ca');
      });
  };

  const handleClockOut = () => {
    formClockOut.validateFields().then(async values => {
      if (totalOut === 0) {
         message.error('Vui lòng nhập số tờ tiền để tính tiền thực tế!');
         return;
      }
      try {
        await axios.put('http://localhost:8080/api/v1/giao-ca/ket-ca', { tienThucTe: totalOut, ghiChu: values.ghiChu }, { headers: getHeaders() });
        message.success(`Đã kết ca thành công! Tiền thực tế: ${totalOut.toLocaleString('vi-VN')} đ`);
        setIsClockOutVisible(false);
        setOutDenoms({});
        formClockOut.resetFields();
        fetchHistory();
        fetchCurrentShift();
      } catch (e) {
        message.error(e.response?.data?.message || 'Lỗi khi kết ca');
      }
    });
  };

  const renderDenominationRows = (denoms, setDenoms) => {
    return (
      <div style={{ maxHeight: '350px', overflowY: 'auto', paddingRight: '10px' }}>
        <Row gutter={8} style={{ marginBottom: 8, fontWeight: 'bold' }}>
          <Col span={8}>Mệnh giá</Col>
          <Col span={8}>Số tờ</Col>
          <Col span={8} style={{ textAlign: 'right' }}>Thành tiền</Col>
        </Row>
        {DENOMINATIONS.map(den => (
          <Row gutter={8} key={den} align="middle" style={{ marginBottom: 12 }}>
            <Col span={8}>
              <Text>{den.toLocaleString('vi-VN')} đ</Text>
            </Col>
            <Col span={8}>
              <InputNumber
                min={0}
                style={{ width: '100%' }}
                placeholder="0"
                value={denoms[den]}
                onChange={val => setDenoms(prev => ({ ...prev, [den]: val || 0 }))}
              />
            </Col>
            <Col span={8} style={{ textAlign: 'right' }}>
              <Text strong>{((denoms[den] || 0) * den).toLocaleString('vi-VN')} đ</Text>
            </Col>
          </Row>
        ))}
      </div>
    );
  };

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
            onClick={() => {
                fetchCurrentShift();
                setIsClockOutVisible(true);
            }}
          >
            KẾT CA
          </Button>
        </Col>
      </Row>

      <Card title="Lịch sử Giao Ca" bordered={false} style={{ borderRadius: '12px', boxShadow: '0 4px 12px rgba(0,0,0,0.05)' }}>
        <Table columns={historyColumns} dataSource={history} pagination={{ pageSize: 5 }} loading={loading} rowKey="id" />
      </Card>

      <Modal title="Xác nhận Vào Ca" open={isClockInVisible} onOk={handleClockIn} onCancel={() => setIsClockInVisible(false)} okText="Vào ca" cancelText="Hủy" width={500}>
        <div style={{ marginBottom: '16px', padding: '12px', backgroundColor: '#e6f7ff', border: '1px solid #91d5ff', borderRadius: '8px', textAlign: 'center' }}>
          <Text style={{ fontSize: '16px' }}>Tổng tiền đầu ca: </Text>
          <Text strong style={{ fontSize: '20px', color: '#1890ff' }}>{totalIn.toLocaleString('vi-VN')} VNĐ</Text>
        </div>
        <Form form={formClockIn} layout="vertical">
          {renderDenominationRows(inDenoms, setInDenoms)}
        </Form>
      </Modal>

      <Modal title="Xác nhận Kết Ca" open={isClockOutVisible} onOk={handleClockOut} onCancel={() => setIsClockOutVisible(false)} okText="Kết ca" cancelText="Hủy" okButtonProps={{ danger: true }} width={500}>
        <div style={{ marginBottom: '16px', padding: '16px', backgroundColor: '#f6ffed', border: '1px solid #b7eb8f', borderRadius: '8px' }}>
          <Text strong>Tiền trên hệ thống (chỉ tính Tiền Mặt): </Text>
          <Text type="success" strong style={{ fontSize: '18px', display: 'block', marginTop: 4 }}>{systemMoney.toLocaleString('vi-VN')} VNĐ</Text>
        </div>
        
        <div style={{ marginBottom: '16px', padding: '12px', backgroundColor: '#fffbe6', border: '1px solid #ffe58f', borderRadius: '8px', textAlign: 'center' }}>
          <Text style={{ fontSize: '16px' }}>Tổng tiền kiểm đếm thực tế: </Text>
          <Text strong style={{ fontSize: '20px', color: '#faad14' }}>{totalOut.toLocaleString('vi-VN')} VNĐ</Text>
        </div>

        <Form form={formClockOut} layout="vertical">
          <Card size="small" title="Kiểm đếm theo mệnh giá" style={{ marginBottom: 16 }}>
            {renderDenominationRows(outDenoms, setOutDenoms)}
          </Card>
          
          <Form.Item name="ghiChu" label="Ghi chú (chênh lệch nếu có)">
            <Input.TextArea rows={3} placeholder="Nhập lý do chênh lệch tiền..." />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default ShiftManagement;
