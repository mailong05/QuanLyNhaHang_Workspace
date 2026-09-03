import React, { useState, useEffect } from 'react';
import { Typography, Row, Col, Card, Statistic, Table, Tag, Tabs, Space, DatePicker, Button } from 'antd';
import { DollarOutlined, ShoppingCartOutlined, CoffeeOutlined, FilterOutlined, DownloadOutlined } from '@ant-design/icons';
import axios from 'axios';

const { Title, Text } = Typography;
const { RangePicker } = DatePicker;
const { TabPane } = Tabs;

const transactionColumns = [
  { title: 'Mã Hóa Đơn', dataIndex: 'maHD', key: 'maHD', render: text => <a>{text}</a> },
  { title: 'Giờ thanh toán', dataIndex: 'thoiGianThanhToan', key: 'thoiGianThanhToan', render: val => new Date(val).toLocaleTimeString('vi-VN') },
  { title: 'Tổng tiền', dataIndex: 'tongTien', key: 'tongTien', render: val => <span style={{ fontWeight: 'bold' }}>{val.toLocaleString('vi-VN')} đ</span> },
  { title: 'Phương thức', dataIndex: 'phuongThucThanhToan', key: 'phuongThucThanhToan', render: val => (
    <Tag color={val === 'TIEN_MAT' ? 'green' : 'blue'}>{val === 'TIEN_MAT' ? 'Tiền mặt' : 'Chuyển khoản'}</Tag>
  )},
];

const topItemColumns = [
  { title: 'Top', key: 'rank', width: 60, align: 'center', render: (text, record, index) => <Text strong style={{ color: index < 3 ? '#cf1322' : 'inherit' }}>#{index + 1}</Text> },
  { title: 'Tên Món Ăn', dataIndex: 'tenMon', key: 'tenMon' },
  { title: 'Đã Bán', dataIndex: 'soLuong', key: 'soLuong', align: 'center' },
  { title: 'Doanh Thu', dataIndex: 'doanhThu', key: 'doanhThu', render: val => `${val.toLocaleString('vi-VN')} đ`, align: 'right' },
];

const Dashboard = () => {
  const [overview, setOverview] = useState({ doanhThuHomNay: 0, soDonHoanTat: 0, banDangPhucVu: 0 });
  const [recentTransactions, setRecentTransactions] = useState([]);
  const [topItems, setTopItems] = useState([]);
  const [chartData, setChartData] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const token = sessionStorage.getItem('accessToken');
        const headers = token ? { Authorization: `Bearer ${token}` } : {};
        
        const [overviewRes, transRes, itemsRes, chartRes] = await Promise.all([
          axios.get('http://localhost:8080/api/v1/reports/dashboard-overview', { headers }),
          axios.get('http://localhost:8080/api/v1/reports/recent-transactions', { headers }),
          axios.get('http://localhost:8080/api/v1/reports/top-items', { headers }),
          axios.get('http://localhost:8080/api/v1/reports/revenue-chart', { headers })
        ]);
        
        setOverview(overviewRes.data.data);
        setRecentTransactions(transRes.data.data);
        setTopItems(itemsRes.data.data);
        setChartData(chartRes.data.data);
      } catch (error) {
        console.error('Lỗi khi tải dữ liệu dashboard', error);
      }
    };
    fetchData();
  }, []);

  return (
    <div style={{ padding: '24px', background: '#f0f2f5', minHeight: '100vh' }}>
      <Title level={2} style={{ marginBottom: '24px' }}>Bảng Điều Khiển</Title>
      
      <Tabs defaultActiveKey="1" type="card" size="large">
        <TabPane tab="Tổng quan hệ thống" key="1">
          <div style={{ paddingTop: '16px' }}>
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
        </TabPane>
        
        <TabPane tab="Thống kê chi tiết" key="2">
          <div style={{ paddingTop: '16px' }}>
            <Row justify="end" style={{ marginBottom: '16px' }}>
              <Space>
                <RangePicker style={{ width: '300px' }} />
                <Button type="primary" icon={<FilterOutlined />}>Lọc Dữ Liệu</Button>
                <Button icon={<DownloadOutlined />}>Xuất Báo Cáo</Button>
              </Space>
            </Row>

            <Row gutter={[24, 24]}>
              <Col xs={24} lg={14}>
                <Card title="Biểu Đồ Doanh Thu 7 Ngày Qua" bordered={false} style={{ borderRadius: '12px', height: '100%' }}>
                  <div style={{ height: '300px', display: 'flex', alignItems: 'flex-end', justifyContent: 'space-between', padding: '20px 0' }}>
                    {chartData.map((data, index) => {
                      const maxDoanhThu = Math.max(...chartData.map(d => d.doanhThu || 0));
                      const heightPercent = maxDoanhThu === 0 ? 0 : ((data.doanhThu || 0) / maxDoanhThu) * 100;
                      return (
                        <div key={index} style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', width: '10%' }}>
                          <div style={{ 
                            height: `${heightPercent}%`, 
                            minHeight: '4px',
                            width: '30px', 
                            backgroundColor: '#1890ff', 
                            borderRadius: '4px 4px 0 0',
                            transition: 'height 0.5s'
                          }}></div>
                          <Text style={{ fontSize: '12px', marginTop: '8px' }}>{data.date.split('-').slice(1).join('/')}</Text>
                        </div>
                      );
                    })}
                  </div>
                </Card>
              </Col>
              <Col xs={24} lg={10}>
                <Card title="Top 5 Món Ăn Bán Chạy" bordered={false} style={{ borderRadius: '12px', height: '100%' }}>
                  <Table 
                    columns={topItemColumns} 
                    dataSource={topItems} 
                    pagination={false}
                    rowKey="tenMon"
                    size="small"
                  />
                </Card>
              </Col>
            </Row>
          </div>
        </TabPane>
      </Tabs>
    </div>
  );
};

export default Dashboard;
