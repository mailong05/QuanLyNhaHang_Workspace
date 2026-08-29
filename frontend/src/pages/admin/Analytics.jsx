import React, { useState, useEffect } from 'react';
import { Typography, Row, Col, Card, DatePicker, Table, Space, Button } from 'antd';
import { FilterOutlined, DownloadOutlined } from '@ant-design/icons';
import axios from 'axios';

const { Title, Text } = Typography;
const { RangePicker } = DatePicker;

const topItemColumns = [
  { title: 'Top', key: 'rank', width: 60, align: 'center', render: (text, record, index) => <Text strong style={{ color: index < 3 ? '#cf1322' : 'inherit' }}>#{index + 1}</Text> },
  { title: 'Tên Món Ăn', dataIndex: 'tenMon', key: 'tenMon' },
  { title: 'Đã Bán', dataIndex: 'soLuong', key: 'soLuong', align: 'center' },
  { title: 'Doanh Thu', dataIndex: 'doanhThu', key: 'doanhThu', render: val => `${val.toLocaleString('vi-VN')} đ`, align: 'right' },
];

const Analytics = () => {
  const [topItems, setTopItems] = useState([]);
  const [chartData, setChartData] = useState([]);

  useEffect(() => {
    const fetchAnalytics = async () => {
      try {
        const token = localStorage.getItem('accessToken');
        const headers = token ? { Authorization: `Bearer ${token}` } : {};

        const [itemsRes, chartRes] = await Promise.all([
          axios.get('http://localhost:8080/api/v1/reports/top-items', { headers }),
          axios.get('http://localhost:8080/api/v1/reports/revenue-chart', { headers })
        ]);

        setTopItems(itemsRes.data.data);
        setChartData(chartRes.data.data);
      } catch (error) {
        console.error('Lỗi khi tải dữ liệu analytics', error);
      }
    };
    fetchAnalytics();
  }, []);

  return (
    <div style={{ padding: '24px', background: '#f0f2f5', minHeight: '100vh' }}>
      <Row justify="space-between" align="middle" style={{ marginBottom: '24px' }}>
        <Col>
          <Title level={2} style={{ margin: 0 }}>Thống Kê Doanh Thu</Title>
        </Col>
        <Col>
          <Space>
            <RangePicker style={{ width: '300px' }} />
            <Button type="primary" icon={<FilterOutlined />}>Lọc Dữ Liệu</Button>
            <Button icon={<DownloadOutlined />}>Xuất Báo Cáo</Button>
          </Space>
        </Col>
      </Row>

      <Row gutter={[24, 24]}>
        <Col xs={24} lg={14}>
          <Card title="Biểu Đồ Doanh Thu 7 Ngày Qua" bordered={false} style={{ borderRadius: '12px', height: '100%' }}>
            {/* Giả lập biểu đồ - trong thực tế sẽ dùng Recharts hoặc Chart.js */}
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
  );
};

export default Analytics;
