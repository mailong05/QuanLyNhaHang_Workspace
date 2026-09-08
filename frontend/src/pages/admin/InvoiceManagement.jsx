import React, { useState, useEffect } from 'react';
import { Table, Card, Tag, Button, Modal, Typography, Space, Input, Select } from 'antd';
import { EyeOutlined } from '@ant-design/icons';
import axios from 'axios';

const { Title, Text } = Typography;
const { Option } = Select;

const InvoiceManagement = () => {
  const [invoices, setInvoices] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searchText, setSearchText] = useState('');
  const [filterStatus, setFilterStatus] = useState('');

  const [pagination, setPagination] = useState({ current: 1, pageSize: 10, total: 0 });
  
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [selectedInvoice, setSelectedInvoice] = useState(null);

  const fetchInvoices = async (page = 1, size = 10, keyword = searchText, status = filterStatus) => {
    setLoading(true);
    try {
      const token = sessionStorage.getItem('accessToken');
      const headers = token ? { Authorization: `Bearer ${token}` } : {};
      
      const res = await axios.get(`http://localhost:8080/api/v1/hoa-don?page=${page - 1}&size=${size}&keyword=${keyword}&trangThai=${status}`, { headers });
      setInvoices(res.data.data.content);
      setPagination({
        current: page,
        pageSize: size,
        total: res.data.data.totalElements
      });
    } catch (error) {
      console.error('Lỗi tải danh sách hóa đơn:', error);
    }
    setLoading(false);
  };

  useEffect(() => {
    fetchInvoices();
  }, []);

  const handleTableChange = (pag) => {
    fetchInvoices(pag.current, pag.pageSize);
  };

  const showDetails = (invoice) => {
    setSelectedInvoice(invoice);
    setIsModalVisible(true);
  };

  const columns = [
    { title: 'Mã Hóa Đơn', dataIndex: 'maHD', key: 'maHD', render: text => <Text strong>{text}</Text> },
    { title: 'Ngày tạo', dataIndex: 'ngayTao', key: 'ngayTao', render: val => val ? new Date(val).toLocaleDateString('vi-VN') : '-' },
    { title: 'Giờ vào', dataIndex: 'gioVao', key: 'gioVao', render: val => val ? val : '-' },
    { title: 'Giờ ra', dataIndex: 'gioRa', key: 'gioRa', render: val => val ? val : '-' },
    { title: 'Tổng thanh toán', dataIndex: 'tongThanhToan', key: 'tongThanhToan', render: val => <Text strong style={{ color: '#cf1322' }}>{val?.toLocaleString('vi-VN')} đ</Text> },
    { title: 'Phương thức', dataIndex: 'phuongThucTT', key: 'phuongThucTT', render: val => {
        if (!val) return '-';
        return <Tag color={val === 'TIEN_MAT' ? 'green' : 'blue'}>{val === 'TIEN_MAT' ? 'Tiền mặt' : 'Chuyển khoản'}</Tag>;
    }},
    { title: 'Trạng thái', dataIndex: 'trangThaiThanhToan', key: 'trangThaiThanhToan', render: val => {
        return <Tag color={val === 'DA_THANH_TOAN' ? 'success' : 'warning'}>{val === 'DA_THANH_TOAN' ? 'Đã thanh toán' : 'Chưa thanh toán'}</Tag>;
    }},
    { title: 'Hành động', key: 'action', render: (_, record) => (
      <Button type="primary" icon={<EyeOutlined />} onClick={() => showDetails(record)} size="small">
        Chi tiết
      </Button>
    )}
  ];

  const detailColumns = [
    { title: 'Tên món', dataIndex: 'tenMon', key: 'tenMon' },
    { title: 'Đơn giá', dataIndex: 'donGiaLuuTru', key: 'donGiaLuuTru', render: val => `${val?.toLocaleString('vi-VN')} đ` },
    { title: 'Số lượng', dataIndex: 'soLuong', key: 'soLuong', align: 'center' },
    { title: 'Thành tiền', dataIndex: 'thanhTien', key: 'thanhTien', render: val => <Text strong style={{ color: '#cf1322' }}>{val?.toLocaleString('vi-VN')} đ</Text> },
    { title: 'Ghi chú', dataIndex: 'ghiChu', key: 'ghiChu' }
  ];

  return (
    <div style={{ padding: '24px', background: '#f0f2f5', minHeight: '100vh' }}>
      <Card title={<Title level={3}>Quản Lý Hóa Đơn</Title>} bordered={false} style={{ borderRadius: '8px' }}>
        
        <Space style={{ marginBottom: 16 }}>
          <Input.Search 
            placeholder="Tìm kiếm theo tên / mã..." 
            allowClear 
            onSearch={(val) => { setSearchText(val); fetchInvoices(1, pagination.pageSize, val, filterStatus); }} 
            style={{ width: 250 }} 
          />
          <Select 
            placeholder="Lọc theo trạng thái" 
            allowClear 
            style={{ width: 200 }} 
            onChange={(val) => { setFilterStatus(val || ''); fetchInvoices(1, pagination.pageSize, searchText, val || ''); }}
          >
            
            <Option value="CHUA_THANH_TOAN">Chưa thanh toán</Option>
            <Option value="DA_THANH_TOAN">Đã thanh toán</Option>
            <Option value="HUY">Hủy</Option>
    
          </Select>
        </Space>

        <Table 
          columns={columns} 
          dataSource={invoices} 
          rowKey="id" 
          pagination={pagination}
          loading={loading}
          onChange={handleTableChange}
          scroll={{ x: 1000 }}
        />
      </Card>

      <Modal
        title={`Chi tiết hóa đơn: ${selectedInvoice?.maHD}`}
        open={isModalVisible}
        onCancel={() => setIsModalVisible(false)}
        footer={[
          <Button key="close" onClick={() => setIsModalVisible(false)}>
            Đóng
          </Button>
        ]}
        width={800}
      >
        {selectedInvoice && (
          <Space direction="vertical" style={{ width: '100%' }} size="large">
            <div style={{ display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap' }}>
              <Text><strong>Thu ngân:</strong> {selectedInvoice.hoTenNV || '-'}</Text>
              <Text><strong>Mã đặt bàn:</strong> {selectedInvoice.maPhieuDat || 'Khách vãng lai'}</Text>
              <Text><strong>Tiền giảm giá:</strong> {selectedInvoice.tienGiamGia?.toLocaleString('vi-VN')} đ</Text>
              <Text><strong>Tiền thuế:</strong> {selectedInvoice.tienThue?.toLocaleString('vi-VN')} đ</Text>
            </div>
            
            <Table 
              columns={detailColumns}
              dataSource={selectedInvoice.chiTiets || []}
              rowKey="id"
              pagination={false}
              size="small"
              bordered
            />
            
            <div style={{ textAlign: 'right' }}>
              <Title level={4}>Tổng cộng: <span style={{ color: '#cf1322' }}>{selectedInvoice.tongThanhToan?.toLocaleString('vi-VN')} đ</span></Title>
            </div>
          </Space>
        )}
      </Modal>
    </div>
  );
};

export default InvoiceManagement;
