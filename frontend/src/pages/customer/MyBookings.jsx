import React, { useState, useEffect } from 'react';
import { Typography, List, Card, Tag, Button, Space, message, Input, Spin, Empty, Modal } from 'antd';
import { CalendarOutlined, ClockCircleOutlined, TeamOutlined, SearchOutlined, ExclamationCircleOutlined } from '@ant-design/icons';
import dayjs from 'dayjs';
import apiClient from '../../services/apiClient';

const { Title, Text } = Typography;

const getStatusTag = (status) => {
  switch (status) {
    case 'CHO_XAC_NHAN': return <Tag color="warning">Chờ xác nhận</Tag>;
    case 'DA_XAC_NHAN': return <Tag color="processing">Đã xác nhận</Tag>;
    case 'DANG_PHUC_VU': return <Tag color="blue">Đang phục vụ</Tag>;
    case 'HOAN_TAT':
    case 'HOAN_THANH': return <Tag color="success">Hoàn tất</Tag>;
    case 'DA_HUY': return <Tag color="default">Đã hủy</Tag>;
    default: return <Tag>{status}</Tag>;
  }
};

const MyBookings = () => {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(false);
  const [identifier, setIdentifier] = useState(sessionStorage.getItem('username') || '');

  const fetchBookings = async (searchKey) => {
    const key = searchKey || identifier;
    if (!key) return;

    setLoading(true);
    try {
      const res = await apiClient.get(`/api/web/booking/my-bookings?identifier=${encodeURIComponent(key.trim())}`);
      setBookings(Array.isArray(res) ? res : (res?.data || []));
    } catch (error) {
      const msg = error.response?.data?.message || error.message || 'Lỗi khi tải lịch sử đặt bàn';
      message.error(msg);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (identifier) {
      fetchBookings(identifier);
    }
  }, []);

  const handleCancel = (maPhieuDat) => {
    Modal.confirm({
      title: 'Xác nhận hủy đặt bàn',
      icon: <ExclamationCircleOutlined />,
      content: `Bạn có chắc chắn muốn hủy phiếu đặt bàn ${maPhieuDat} không?`,
      okText: 'Hủy phiếu',
      okType: 'danger',
      cancelText: 'Quay lại',
      onOk: async () => {
        try {
          await apiClient.put(`/api/web/booking/${maPhieuDat}/cancel?identifier=${encodeURIComponent(identifier.trim())}`);
          message.success(`Đã hủy phiếu đặt bàn ${maPhieuDat} thành công.`);
          fetchBookings(identifier);
        } catch (error) {
          const msg = error.response?.data?.message || error.message || 'Không thể hủy phiếu đặt bàn';
          message.error(msg);
        }
      }
    });
  };

  return (
    <div style={{ maxWidth: '850px', margin: '0 auto', padding: '40px 24px' }}>
      <Title level={2} style={{ marginBottom: '8px' }}>Lịch sử Đặt Bàn của tôi</Title>
      <Text type="secondary" style={{ display: 'block', marginBottom: '24px' }}>
        Tra cứu thông tin và quản lý các lịch hẹn đặt bàn của bạn tại Ha Long Restaurant.
      </Text>

      <div style={{ display: 'flex', gap: '12px', marginBottom: '24px' }}>
        <Input
          size="large"
          placeholder="Nhập Số điện thoại hoặc Tên đăng nhập để tra cứu"
          value={identifier}
          onChange={(e) => setIdentifier(e.target.value)}
          onPressEnter={() => fetchBookings(identifier)}
          prefix={<SearchOutlined />}
          style={{ flex: 1 }}
        />
        <Button size="large" type="primary" onClick={() => fetchBookings(identifier)} loading={loading}>
          Tra cứu
        </Button>
      </div>

      {loading ? (
        <div style={{ textAlign: 'center', padding: '60px 0' }}>
          <Spin size="large" tip="Đang tải dữ liệu đặt bàn..." />
        </div>
      ) : bookings.length === 0 ? (
        <Empty description="Chưa có phiếu đặt bàn nào được tìm thấy" style={{ margin: '60px 0' }} />
      ) : (
        <List
          grid={{ gutter: 16, column: 1 }}
          dataSource={bookings}
          renderItem={item => {
            const timeObj = dayjs(item.thoiGianDen);
            const banNames = (item.chiTiets || []).map(c => c.maBan).filter(Boolean).join(', ');
            return (
              <List.Item>
                <Card 
                  style={{ borderRadius: '12px', boxShadow: '0 2px 10px rgba(0,0,0,0.06)' }}
                  bodyStyle={{ padding: '24px' }}
                >
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: '16px' }}>
                    <div>
                      <Space direction="vertical" size="small">
                        <Space align="center">
                          <Text strong style={{ fontSize: '18px', color: '#1890ff' }}>{item.maPhieuDat}</Text>
                          {getStatusTag(item.trangThai)}
                        </Space>
                        <Space size="middle" style={{ marginTop: '4px' }}>
                          <Space>
                            <CalendarOutlined style={{ color: '#52c41a' }} />
                            <Text>{timeObj.isValid() ? timeObj.format('DD/MM/YYYY') : item.thoiGianDen}</Text>
                          </Space>
                          <Space>
                            <ClockCircleOutlined style={{ color: '#fa8c16' }} />
                            <Text>{timeObj.isValid() ? timeObj.format('HH:mm') : ''}</Text>
                          </Space>
                          <Space>
                            <TeamOutlined style={{ color: '#1890ff' }} />
                            <Text>{item.soLuongNguoi} người</Text>
                          </Space>
                        </Space>
                        {banNames && (
                          <Text style={{ marginTop: '4px' }}>
                            <strong>Bàn đã chọn:</strong> <Tag color="blue">{banNames}</Tag>
                          </Text>
                        )}
                        {item.tienDatCoc > 0 && (
                          <Text type="success">
                            <strong>Tiền cọc:</strong> {Number(item.tienDatCoc).toLocaleString('vi-VN')} đ
                          </Text>
                        )}
                        {item.ghiChu && (
                          <Text type="secondary" style={{ fontStyle: 'italic' }}>
                            Ghi chú: {item.ghiChu}
                          </Text>
                        )}
                      </Space>
                    </div>
                    
                    <div>
                      {(item.trangThai === 'CHO_XAC_NHAN' || item.trangThai === 'DA_XAC_NHAN') && (
                        <Button danger onClick={() => handleCancel(item.maPhieuDat)}>
                          Hủy đặt bàn
                        </Button>
                      )}
                    </div>
                  </div>
                </Card>
              </List.Item>
            );
          }}
        />
      )}
    </div>
  );
};

export default MyBookings;
