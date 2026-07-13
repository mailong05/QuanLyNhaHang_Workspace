import React from 'react';
import { Typography, Row, Col, Card, Statistic, Table, Tag } from 'antd';
import { DollarOutlined, ShoppingCartOutlined, CoffeeOutlined, ArrowUpOutlined } from '@ant-design/icons';

const { Title } = Typography;

const mockTransactions = [
  { key: '1', maBill: 'HD_1784910001', gio: '14:20:05', tongTien: 1250000, phuongThuc: 'Chuyển khoản' },
  { key: '2', maBill: 'HD_1784910002', gio: '14:35:10', tongTien: 450000, phuongThuc: 'Tiền mặt' },
  { key: '3', maBill: 'HD_1784910003', gio: '15:10:45', tongTien: 890000, phuongThuc: 'Tiền mặt' },
  { key: '4', maBill: 'HD_1784910004', gio: '15:45:00', tongTien: 2100000, phuongThuc: 'Chuyển khoản' },
  { key: '5', maBill: 'HD_1784910005', gio: '16:05:30', tongTien: 340000, phuongThuc: 'Tiền mặt' },
];

const transactionColumns = [
  { title: 'Mã Hóa Đơn', dataIndex: 'maBill', key: 'maBill', render: text => <a>{text}</a> },
  { title: 'Giờ thanh toán', dataIndex: 'gio', key: 'gio' },
  { title: 'Tổng tiền', dataIndex: 'tongTien', key: 'tongTien', render: val => <span style={{ fontWeight: 'bold' }}>{val.toLocaleString('vi-VN')} đ</span> },
  { title: 'Phương thức', dataIndex: 'phuongThuc', key: 'phuongThuc', render: val => (
    <Tag color={val === 'Tiền mặt' ? 'green' : 'blue'}>{val}</Tag>
  )},
];

const Dashboard = () => {
  return (
    <div style={{ padding: '24px', background: '#f0f2f5', minHeight: '100vh' }}>
      <Title level={2} style={{ marginBottom: '24px' }}>Tổng quan hệ thống</Title>
      
      <Row gutter={[24, 24]}>
        <Col xs={24} sm={8}>
          <Card bordered={false} style={{ borderRadius: '12px', boxShadow: '0 2px 8px rgba(0,0,0,0.05)' }}>
            <Statistic
              title="Doanh thu hôm nay"
              value={15400000}
              precision={0}
              valueStyle={{ color: '#cf1322', fontWeight: 'bold' }}
              prefix={<DollarOutlined />}
              suffix="VNĐ"
            />
            <div style={{ marginTop: '16px' }}>
              <Tag color="success" icon={<ArrowUpOutlined />}>Tăng 12%</Tag> <span style={{ color: '#8c8c8c', fontSize: '12px' }}>so với hôm qua</span>
            </div>
          </Card>
        </Col>
        <Col xs={24} sm={8}>
          <Card bordered={false} style={{ borderRadius: '12px', boxShadow: '0 2px 8px rgba(0,0,0,0.05)' }}>
            <Statistic
              title="Số đơn hoàn tất"
              value={48}
              valueStyle={{ color: '#3f8600', fontWeight: 'bold' }}
              prefix={<ShoppingCartOutlined />}
              suffix="đơn"
            />
            <div style={{ marginTop: '16px' }}>
              <Tag color="success" icon={<ArrowUpOutlined />}>Tăng 5%</Tag> <span style={{ color: '#8c8c8c', fontSize: '12px' }}>so với hôm qua</span>
            </div>
          </Card>
        </Col>
        <Col xs={24} sm={8}>
          <Card bordered={false} style={{ borderRadius: '12px', boxShadow: '0 2px 8px rgba(0,0,0,0.05)' }}>
            <Statistic
              title="Bàn đang phục vụ"
              value={12}
              valueStyle={{ color: '#1890ff', fontWeight: 'bold' }}
              prefix={<CoffeeOutlined />}
              suffix="/ 25 bàn"
            />
            <div style={{ marginTop: '16px' }}>
              <Tag color="processing">Công suất 48%</Tag>
            </div>
          </Card>
        </Col>
      </Row>

      <Row gutter={[24, 24]} style={{ marginTop: '24px' }}>
        <Col span={24}>
          <Card title="Giao dịch mới nhất" bordered={false} style={{ borderRadius: '12px', boxShadow: '0 2px 8px rgba(0,0,0,0.05)' }}>
            <Table 
              columns={transactionColumns} 
              dataSource={mockTransactions} 
              pagination={false}
              size="middle"
            />
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default Dashboard;
