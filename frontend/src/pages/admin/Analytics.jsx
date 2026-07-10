import React from 'react';
import { Typography, Row, Col, Card, DatePicker, Table, Space, Button } from 'antd';
import { FilterOutlined, DownloadOutlined } from '@ant-design/icons';

const { Title, Text } = Typography;
const { RangePicker } = DatePicker;

const mockTopItems = [
  { key: '1', rank: 1, tenMon: 'Bò Bít Tết Sốt Tiêu Xanh', soLuong: 125, doanhThu: 31250000 },
  { key: '2', rank: 2, tenMon: 'Cá Hồi Áp Chảo Măng Tây', soLuong: 98, doanhThu: 31360000 },
  { key: '3', rank: 3, tenMon: 'Súp Nấm Truffle', soLuong: 85, doanhThu: 15300000 },
  { key: '4', rank: 4, tenMon: 'Salad Hoàng Gia', soLuong: 70, doanhThu: 8400000 },
  { key: '5', rank: 5, tenMon: 'Gà Quay Mật Ong', soLuong: 65, doanhThu: 13650000 },
];

const topItemColumns = [
  { title: 'Top', dataIndex: 'rank', key: 'rank', width: 60, align: 'center', render: val => <Text strong style={{ color: val <= 3 ? '#cf1322' : 'inherit' }}>#{val}</Text> },
  { title: 'Tên Món Ăn', dataIndex: 'tenMon', key: 'tenMon' },
  { title: 'Đã Bán', dataIndex: 'soLuong', key: 'soLuong', align: 'center' },
  { title: 'Doanh Thu', dataIndex: 'doanhThu', key: 'doanhThu', render: val => `${val.toLocaleString('vi-VN')} đ`, align: 'right' },
];

const Analytics = () => {
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
          <Card 
            title="Biểu Đồ Doanh Thu" 
            bordered={false} 
            style={{ borderRadius: '12px', boxShadow: '0 2px 8px rgba(0,0,0,0.05)', height: '100%' }}
          >
            {/* Khung trống cho Biểu đồ Recharts sau này */}
            <div style={{ 
              height: '400px', 
              border: '2px dashed #d9d9d9', 
              borderRadius: '8px', 
              display: 'flex', 
              alignItems: 'center', 
              justifyContent: 'center',
              backgroundColor: '#fafafa'
            }}>
              <Text type="secondary" style={{ fontSize: '18px' }}>[ Khu vực Biểu đồ Doanh Thu - Dành cho Recharts ]</Text>
            </div>
          </Card>
        </Col>
        
        <Col xs={24} lg={10}>
          <Card 
            title="Top 5 Món Bán Chạy" 
            bordered={false} 
            style={{ borderRadius: '12px', boxShadow: '0 2px 8px rgba(0,0,0,0.05)', height: '100%' }}
          >
            <Table 
              columns={topItemColumns} 
              dataSource={mockTopItems} 
              pagination={false}
              size="middle"
            />
            
            <div style={{ marginTop: '24px', padding: '16px', background: '#e6f7ff', borderRadius: '8px' }}>
              <Title level={5} style={{ color: '#1890ff', margin: 0 }}>Tỷ lệ lấp đầy bàn trung bình</Title>
              <div style={{ display: 'flex', alignItems: 'baseline', marginTop: '8px' }}>
                <Text style={{ fontSize: '32px', fontWeight: 'bold', color: '#1890ff', marginRight: '8px' }}>76%</Text>
                <Text type="secondary">Trong khoảng thời gian đã chọn</Text>
              </div>
            </div>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default Analytics;
