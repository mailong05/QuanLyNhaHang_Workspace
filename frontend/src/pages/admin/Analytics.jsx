import React, { useState, useEffect } from 'react';
import { Typography, Row, Col, Card, DatePicker, Table, Space, Button } from 'antd';
import { FilterOutlined, DownloadOutlined } from '@ant-design/icons';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
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
  const [dateRange, setDateRange] = useState(null);

  const fetchAnalytics = async (dates) => {
    try {
      const token = sessionStorage.getItem('accessToken');
      const headers = token ? { Authorization: `Bearer ${token}` } : {};
      
      let query = '';
      if (dates && dates.length === 2) {
        query = `?startDate=${dates[0].format('YYYY-MM-DD')}&endDate=${dates[1].format('YYYY-MM-DD')}`;
      }

      const [itemsRes, chartRes] = await Promise.all([
        axios.get(`http://localhost:8080/api/v1/reports/top-items${query}`, { headers }),
        axios.get(`http://localhost:8080/api/v1/reports/revenue-chart${query}`, { headers })
      ]);

      setTopItems(itemsRes.data.data);
      setChartData(chartRes.data.data);
    } catch (error) {
      console.error('Lỗi khi tải dữ liệu analytics', error);
    }
  };

  useEffect(() => {
    fetchAnalytics(dateRange);
  }, []);

  const handleFilter = () => {
    fetchAnalytics(dateRange);
  };

  const handleExport = () => {
    if (!chartData || chartData.length === 0) return;
    
    const csvRows = [];
    csvRows.push('Ngay,Doanh Thu');
    chartData.forEach(row => {
      csvRows.push(`${row.date},${row.doanhThu}`);
    });
    
    const csvString = csvRows.join('\n');
    const blob = new Blob([csvString], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.setAttribute('hidden', '');
    a.setAttribute('href', url);
    a.setAttribute('download', 'BaoCaoDoanhThu.csv');
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
  };

  return (
    <div style={{ padding: '24px', background: '#f0f2f5', minHeight: '100vh' }}>
      <Row justify="space-between" align="middle" style={{ marginBottom: '24px' }}>
        <Col>
          <Title level={2} style={{ margin: 0 }}>Thống Kê Doanh Thu</Title>
        </Col>
        <Col>
          <Space>
            <RangePicker style={{ width: '300px' }} value={dateRange} onChange={setDateRange} />
            <Button type="primary" icon={<FilterOutlined />} onClick={handleFilter}>Lọc Dữ Liệu</Button>
            <Button icon={<DownloadOutlined />} onClick={handleExport}>Xuất Báo Cáo CSV</Button>
          </Space>
        </Col>
      </Row>

      <Row gutter={[24, 24]}>
        <Col xs={24} lg={14}>
          <Card title="Biểu Đồ Doanh Thu 7 Ngày Qua" bordered={false} style={{ borderRadius: '12px', height: '100%' }}>
            <div style={{ height: '300px' }}>
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={chartData} margin={{ top: 20, right: 30, left: 20, bottom: 5 }}>
                  <CartesianGrid strokeDasharray="3 3" vertical={false} />
                  <XAxis dataKey="date" tickFormatter={(tick) => tick.split('-').slice(1).join('/')} />
                  <YAxis tickFormatter={(value) => `${(value / 1000).toLocaleString('vi-VN')}k`} />
                  <Tooltip formatter={(value) => `${value.toLocaleString('vi-VN')} đ`} labelFormatter={(label) => `Ngày: ${label}`} />
                  <Bar dataKey="doanhThu" fill="#1890ff" radius={[4, 4, 0, 0]} barSize={40} />
                </BarChart>
              </ResponsiveContainer>
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
