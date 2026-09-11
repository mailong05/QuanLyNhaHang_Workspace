import React, { useState, useEffect } from 'react';
import { Table, Button, Input, Space, message, Tag } from 'antd';
import { SearchOutlined } from '@ant-design/icons';
import apiClient from '../../services/apiClient';
import dayjs from 'dayjs';

const CustomerManagement = () => {
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searchText, setSearchText] = useState('');
  const [pagination, setPagination] = useState({ current: 1, pageSize: 10, total: 0 });

  const fetchCustomers = async (page = 1, size = 10, search = '') => {
    setLoading(true);
    try {
      const data = await apiClient.get('/api/v1/khach-hang', {
        params: { page: page - 1, size, search }
      });
      setCustomers(data.content);
      setPagination({ ...pagination, current: page, total: data.totalElements });
    } catch (error) {
      message.error('Lỗi khi tải danh sách khách hàng');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCustomers(pagination.current, pagination.pageSize, searchText);
  }, []);

  const handleTableChange = (newPagination) => {
    fetchCustomers(newPagination.current, newPagination.pageSize, searchText);
  };

  const handleSearch = (value) => {
    setSearchText(value);
    fetchCustomers(1, pagination.pageSize, value);
  };

  const columns = [
    { title: 'Mã KH', dataIndex: 'maKH', key: 'maKH' },
    { title: 'Họ tên', dataIndex: 'hoTen', key: 'hoTen' },
    { title: 'Số điện thoại', dataIndex: 'sdt', key: 'sdt' },
    { title: 'Email', dataIndex: 'email', key: 'email' },
    { title: 'Điểm tích lũy', dataIndex: 'diemTichLuy', key: 'diemTichLuy', render: (val) => <Tag color="gold">{val} điểm</Tag> },
    { title: 'Hạng', dataIndex: 'hangKhachHang', key: 'hangKhachHang', render: (val) => <Tag color="blue">{val}</Tag> }
  ];

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 16 }}>
        <h2>Quản lý Khách Hàng</h2>
        <Space>
          <Input.Search
            placeholder="Tìm kiếm theo Tên, SĐT..."
            allowClear
            onSearch={handleSearch}
            style={{ width: 300 }}
          />
        </Space>
      </div>
      <Table
        columns={columns}
        dataSource={customers}
        rowKey="id"
        pagination={{ ...pagination, showSizeChanger: false }}
        loading={loading}
        onChange={handleTableChange}
      />
    </div>
  );
};

export default CustomerManagement;
