import React from 'react';
import { Typography, List, Card, Tag, Button, Space, message } from 'antd';
import { CalendarOutlined, ClockCircleOutlined, TeamOutlined, EnvironmentOutlined } from '@ant-design/icons';

const { Title, Text } = Typography;

const mockBookings = [
  { id: 'BK001', ngayDen: '15/07/2026', gioDen: '19:00', soNguoi: 4, trangThai: 'CHO_XAC_NHAN', ghiChu: 'Ăn chay' },
  { id: 'BK002', ngayDen: '10/07/2026', gioDen: '18:30', soNguoi: 2, trangThai: 'DA_XAC_NHAN', ghiChu: 'Kỷ niệm ngày cưới' },
  { id: 'BK003', ngayDen: '05/07/2026', gioDen: '20:00', soNguoi: 6, trangThai: 'HOAN_TAT', ghiChu: '' },
  { id: 'BK004', ngayDen: '01/07/2026', gioDen: '12:00', soNguoi: 3, trangThai: 'DA_HUY', ghiChu: '' },
];

const getStatusTag = (status) => {
  switch (status) {
    case 'CHO_XAC_NHAN': return <Tag color="warning">Chờ xác nhận</Tag>;
    case 'DA_XAC_NHAN': return <Tag color="processing">Đã xác nhận (Đã nhận bàn)</Tag>;
    case 'HOAN_TAT': return <Tag color="success">Hoàn tất</Tag>;
    case 'DA_HUY': return <Tag color="default">Đã hủy</Tag>;
    default: return <Tag>{status}</Tag>;
  }
};

const MyBookings = () => {

  const handleCancel = (id) => {
    message.success(`Đã gửi yêu cầu hủy đặt bàn ${id}.`);
  };

  return (
    <div style={{ maxWidth: '800px', margin: '0 auto', padding: '40px 24px' }}>
      <Title level={2} style={{ marginBottom: '24px' }}>Lịch sử Đặt Bàn của tôi</Title>
      
      <List
        grid={{ gutter: 16, column: 1 }}
        dataSource={mockBookings}
        renderItem={item => (
          <List.Item>
            <Card 
              style={{ borderRadius: '12px', boxShadow: '0 2px 8px rgba(0,0,0,0.05)' }}
              bodyStyle={{ padding: '24px' }}
            >
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                <div>
                  <Space direction="vertical" size="small">
                    <Text strong style={{ fontSize: '16px' }}>Mã Đặt Bàn: {item.id}</Text>
                    <Space>
                      <CalendarOutlined style={{ color: '#1890ff' }} /> <Text>{item.ngayDen}</Text>
                      <span style={{ color: '#d9d9d9' }}>|</span>
                      <ClockCircleOutlined style={{ color: '#1890ff' }} /> <Text>{item.gioDen}</Text>
                    </Space>
                    <Space>
                      <TeamOutlined style={{ color: '#1890ff' }} /> <Text>{item.soNguoi} người</Text>
                      {item.ghiChu && (
                        <>
                          <span style={{ color: '#d9d9d9' }}>|</span>
                          <Text type="secondary">Ghi chú: {item.ghiChu}</Text>
                        </>
                      )}
                    </Space>
                  </Space>
                </div>
                
                <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: '12px' }}>
                  {getStatusTag(item.trangThai)}
                  {item.trangThai === 'CHO_XAC_NHAN' && (
                    <Button danger onClick={() => handleCancel(item.id)}>Hủy đặt bàn</Button>
                  )}
                </div>
              </div>
            </Card>
          </List.Item>
        )}
      />
    </div>
  );
};

export default MyBookings;
