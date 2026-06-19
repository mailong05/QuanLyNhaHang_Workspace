import React, { useState, useEffect } from 'react';
import { Table, Tag, Button, Space, Card, message, Tabs, Modal, Form, Select, Popconfirm } from 'antd';
import { CheckCircleOutlined, CloseCircleOutlined, RightCircleOutlined } from '@ant-design/icons';
import dayjs from 'dayjs';
import apiClient from '../../services/apiClient';

const { TabPane } = Tabs;
const { Option } = Select;

const BookingManagement = () => {
  const [bookings, setBookings] = useState([]);
  const [availableTables, setAvailableTables] = useState([]);
  const [loading, setLoading] = useState(false);
  const [activeTab, setActiveTab] = useState('CHO_XAC_NHAN');

  // Modal State cho việc Xếp Bàn (Duyệt phiếu)
  const [isAssignModalVisible, setIsAssignModalVisible] = useState(false);
  const [assigningBooking, setAssigningBooking] = useState(null);
  const [form] = Form.useForm();

  const fetchBookings = async () => {
    setLoading(true);
    try {
      const data = await apiClient.get('/api/v1/phieu-dat-ban');
      const bookingList = data.content ? data.content : data;
      setBookings(bookingList);
    } catch (error) {
      message.error(error.message || 'Lỗi khi tải danh sách phiếu đặt bàn');
    } finally {
      setLoading(false);
    }
  };

  const fetchAvailableTables = async () => {
    try {
      const data = await apiClient.get('/api/v1/ban-an');
      const tableList = data.content ? data.content : data;
      // Lọc các bàn có trạng thái TRONG
      const emptyTables = tableList.filter(t => t.trangThai === 'TRONG');
      setAvailableTables(emptyTables);
    } catch (error) {
      message.error('Lỗi khi tải danh sách bàn trống');
    }
  };

  useEffect(() => {
    fetchBookings();
  }, []);

  // Filter dữ liệu theo Tab hiện tại
  const filteredBookings = bookings.filter(b => b.trangThai === activeTab);

  const getStatusTag = (status) => {
    switch (status) {
      case 'CHO_XAC_NHAN':
        return <Tag color="processing">Chờ xác nhận</Tag>;
      case 'DA_XAC_NHAN':
        return <Tag color="success">Đã xác nhận</Tag>;
      case 'DA_HUY':
        return <Tag color="error">Đã hủy</Tag>;
      case 'DANG_PHUC_VU':
        return <Tag color="warning">Đang phục vụ</Tag>;
      case 'HOAN_THANH':
        return <Tag color="default">Hoàn thành</Tag>;
      default:
        return <Tag color="default">{status}</Tag>;
    }
  };

  // --- ACTIONS ---

  // 1. Duyệt Phiếu (Mở Modal chọn Bàn)
  const showAssignTableModal = (record) => {
    setAssigningBooking(record);
    form.resetFields();
    fetchAvailableTables(); // Lấy danh sách bàn trống mỗi khi mở Modal
    setIsAssignModalVisible(true);
  };

  const handleApprove = async (values) => {
    try {
      // payload cho API PUT /api/v1/phieu-dat-ban/{maPhieuDat}
      const payload = {
        thoiGianDen: assigningBooking.thoiGianDen,
        soLuongNguoi: assigningBooking.soLuongNguoi,
        ghiChu: assigningBooking.ghiChu,
        tienDatCoc: assigningBooking.tienDatCoc,
        trangThai: 'DA_XAC_NHAN',
        chiTiets: [
          {
            maBan: values.maBan,
            maPhieuDat: assigningBooking.maPhieuDat
          }
        ]
      };
      await apiClient.put(`/api/v1/phieu-dat-ban/${assigningBooking.maPhieuDat}`, payload);
      message.success('Duyệt phiếu và xếp bàn thành công!');
      setIsAssignModalVisible(false);
      fetchBookings();
    } catch (error) {
      message.error(error.message || 'Lỗi khi duyệt phiếu');
    }
  };

  // 2. Hủy Phiếu
  const handleCancelBooking = async (record) => {
    try {
      const payload = {
        thoiGianDen: record.thoiGianDen,
        soLuongNguoi: record.soLuongNguoi,
        ghiChu: record.ghiChu,
        tienDatCoc: record.tienDatCoc,
        trangThai: 'DA_HUY',
        chiTiets: record.chiTiets || []
      };
      await apiClient.put(`/api/v1/phieu-dat-ban/${record.maPhieuDat}`, payload);
      message.success('Đã hủy phiếu đặt bàn!');
      fetchBookings();
    } catch (error) {
      message.error(error.message || 'Lỗi khi hủy phiếu');
    }
  };

  // 3. Khách đã nhận bàn (Chuyển sang Đang phục vụ)
  const handleCheckIn = async (record) => {
    try {
      const payload = {
        thoiGianDen: record.thoiGianDen,
        soLuongNguoi: record.soLuongNguoi,
        ghiChu: record.ghiChu,
        tienDatCoc: record.tienDatCoc,
        trangThai: 'DANG_PHUC_VU',
        chiTiets: record.chiTiets || []
      };
      await apiClient.put(`/api/v1/phieu-dat-ban/${record.maPhieuDat}`, payload);
      message.success('Khách đã nhận bàn thành công!');
      fetchBookings();
    } catch (error) {
      message.error(error.message || 'Lỗi khi check-in nhận bàn');
    }
  };

  const columns = [
    {
      title: 'Mã Phiếu',
      dataIndex: 'maPhieuDat',
      key: 'maPhieuDat',
    },
    {
      title: 'Tên Khách',
      dataIndex: 'hoTenKH',
      key: 'hoTenKH',
    },
    {
      title: 'SĐT',
      key: 'sdt',
      render: (_, record) => record.sdtKH || record.sdt || 'N/A' // Fallback nếu backend ko trả về thẳng
    },
    {
      title: 'Thời gian đến',
      dataIndex: 'thoiGianDen',
      key: 'thoiGianDen',
      render: (time) => time ? dayjs(time).format('HH:mm DD/MM/YYYY') : ''
    },
    {
      title: 'Số người',
      dataIndex: 'soLuongNguoi',
      key: 'soLuongNguoi',
      render: (num) => `${num} người`
    },
    {
      title: 'Trạng thái',
      dataIndex: 'trangThai',
      key: 'trangThai',
      render: (trangThai) => getStatusTag(trangThai)
    },
    {
      title: 'Hành động',
      key: 'action',
      render: (_, record) => {
        if (record.trangThai === 'CHO_XAC_NHAN') {
          return (
            <Space size="middle">
              <Button 
                type="primary" 
                icon={<CheckCircleOutlined />} 
                size="small" 
                style={{ backgroundColor: '#52c41a', borderColor: '#52c41a' }}
                onClick={() => showAssignTableModal(record)}
              >
                Duyệt
              </Button>
              <Popconfirm
                title="Hủy Phiếu Đặt Bàn"
                description="Bạn có chắc chắn muốn hủy phiếu này không?"
                onConfirm={() => handleCancelBooking(record)}
                okText="Đồng ý"
                cancelText="Hủy"
              >
                <Button type="primary" danger icon={<CloseCircleOutlined />} size="small">
                  Hủy
                </Button>
              </Popconfirm>
            </Space>
          );
        }

        if (record.trangThai === 'DA_XAC_NHAN') {
          return (
            <Button 
              type="primary" 
              icon={<RightCircleOutlined />} 
              size="small"
              onClick={() => handleCheckIn(record)}
            >
              Đã nhận bàn
            </Button>
          );
        }

        return null;
      },
    },
  ];

  return (
    <>
      <Card title="Quản lý Phiếu Đặt Bàn">
        <Tabs activeKey={activeTab} onChange={(key) => setActiveTab(key)}>
          <TabPane tab="Chờ xác nhận" key="CHO_XAC_NHAN" />
          <TabPane tab="Đã xác nhận" key="DA_XAC_NHAN" />
          <TabPane tab="Đã hủy" key="DA_HUY" />
        </Tabs>
        <Table 
          columns={columns} 
          dataSource={filteredBookings} 
          rowKey="id" 
          loading={loading}
          pagination={{ pageSize: 10 }}
        />
      </Card>

      {/* Modal Xếp bàn khi Duyệt */}
      <Modal
        title={`Xếp bàn cho phiếu ${assigningBooking?.maPhieuDat || ''}`}
        open={isAssignModalVisible}
        onCancel={() => setIsAssignModalVisible(false)}
        onOk={() => form.submit()}
        okText="Xác nhận Duyệt"
        cancelText="Hủy"
        destroyOnClose
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={handleApprove}
        >
          <div style={{ marginBottom: '16px' }}>
            <p><strong>Khách hàng:</strong> {assigningBooking?.hoTenKH}</p>
            <p><strong>Số người:</strong> {assigningBooking?.soLuongNguoi}</p>
            <p><strong>Thời gian đến:</strong> {assigningBooking?.thoiGianDen ? dayjs(assigningBooking.thoiGianDen).format('HH:mm DD/MM/YYYY') : ''}</p>
          </div>

          <Form.Item
            name="maBan"
            label="Chọn bàn trống"
            rules={[{ required: true, message: 'Vui lòng chọn bàn để xếp cho khách!' }]}
          >
            <Select placeholder="-- Chọn Bàn --">
              {availableTables.map(table => (
                <Option key={table.id} value={table.maBan}>
                  Bàn {table.maBan} - {table.viTri} (Sức chứa: {table.soGhe})
                </Option>
              ))}
            </Select>
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};

export default BookingManagement;
