import React, { useState, useEffect } from 'react';
import { Typography, Row, Col, Card, Statistic, Table, Tag } from 'antd';
import { DollarOutlined, ShoppingCartOutlined, CoffeeOutlined } from '@ant-design/icons';
import axios from 'axios';

const { Title } = Typography;

const transactionColumns = [
  { title: 'Mã Hóa Đơn', dataIndex: 'maHD', key: 'maHD', render: text => <a>{text}</a> },
  { title: 'Giờ thanh toán', dataIndex: 'thoiGianThanhToan', key: 'thoiGianThanhToan', render: val => new Date(val).toLocaleTimeString('vi-VN') },
  { title: 'Tổng tiền', dataIndex: 'tongTien', key: 'tongTien', render: val => <span style={{ fontWeight: 'bold' }}>{val.toLocaleString('vi-VN')} đ</span> },
  { title: 'Phương thức', dataIndex: 'phuongThucThanhToan', key: 'phuongThucThanhToan', render: val => (
    <Tag color={val === 'TIEN_MAT' ? 'green' : 'blue'}>{val === 'TIEN_MAT' ? 'Tiền mặt' : 'Chuyển khoản'}</Tag>
  )},
];

const Dashboard = () => {
  const [overview, setOverview] = useState({ doanhThuHomNay: 0, soDonHoanTat: 0, banDangPhucVu: 0 });
  const [recentTransactions, setRecentTransactions] = useState([]);

  useEffect(() => {
    const fetchDashboard = async () => {
      try {
        const token = localStorage.getItem('accessToken');
        const headers = token ? { Authorization: `Bearer ${token}` } : {};
        
        const [overviewRes, transRes] = await Promise.all([
          axios.get('http://localhost:8080/api/v1/reports/dashboard-overview', { headers }),
          axios.get('http://localhost:8080/api/v1/reports/recent-transactions', { headers })
        ]);
        
        setOverview(overviewRes.data.data);
        setRecentTransactions(transRes.data.data);
      } catch (error) {
        console.error('Lỗi khi tải dữ liệu dashboard', error);
      }
    };
    fetchDashboard();
  }, []);

  return (
    <div style={{ padding: '24px', background: '#f0f2f5', minHeight: '100vh' }}>
      <Title level={2} style={{ marginBottom: '24px' }}>Tổng quan hệ thống</Title>
      
      <Row gutter={[24, 24]}>
        <Col xs={24} sm={8}>
          <Card bordered={false} style={{ borderRadius: '12px', boxShadow: '0 2px 8px rgba(0,0,0,0.05)' }}>
            <Statistic
              title="Doanh thu hôm nay"
              value={overview.doanhThuHomNay}
              precision={0}
              valueStyle={{ color: '#cf1322', fontWeight: 'bold' }}
              prefix={<DollarOutlined />}
              suffix="VNĐ"
            />
          </Card>
        </Col>
        
        <Col xs={24} sm={8}>
          <Card bordered={false} style={{ borderRadius: '12px', boxShadow: '0 2px 8px rgba(0,0,0,0.05)' }}>
            <Statistic
              title="Đơn hàng hoàn tất"
              value={overview.soDonHoanTat}
              valueStyle={{ color: '#3f8600', fontWeight: 'bold' }}
              prefix={<ShoppingCartOutlined />}
              suffix="Đơn"
            />
          </Card>
        </Col>

        <Col xs={24} sm={8}>
          <Card bordered={false} style={{ borderRadius: '12px', boxShadow: '0 2px 8px rgba(0,0,0,0.05)' }}>
            <Statistic
              title="Bàn đang phục vụ"
              value={overview.banDangPhucVu}
              valueStyle={{ color: '#faad14', fontWeight: 'bold' }}
              prefix={<CoffeeOutlined />}
              suffix="Bàn"
            />
          </Card>
        </Col>
      </Row>

      <Row gutter={[24, 24]} style={{ marginTop: '24px' }}>
        <Col span={24}>
          <Card title="Giao dịch gần đây nhất" bordered={false} style={{ borderRadius: '12px', boxShadow: '0 2px 8px rgba(0,0,0,0.05)' }}>
            <Table 
              columns={transactionColumns} 
              dataSource={recentTransactions} 
              pagination={false}
              rowKey="maHD"
            />
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default Dashboard;
