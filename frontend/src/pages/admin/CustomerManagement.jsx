import React, { useState, useEffect } from 'react';
import { Table, Button, Input, Space, message, Tag, Popconfirm, Modal, Form } from 'antd';
import { SearchOutlined, DeleteOutlined, EditOutlined } from '@ant-design/icons';
import apiClient from '../../services/apiClient';

const CustomerManagement = () => {
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searchText, setSearchText] = useState('');
  const [pagination, setPagination] = useState({ current: 1, pageSize: 10, total: 0 });

  // Modal State
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [editingCustomer, setEditingCustomer] = useState(null);
  const [form] = Form.useForm();

  const fetchCustomers = async (page = 1, size = 10, search = '') => {
    setLoading(true);
    try {
      const data = await apiClient.get('/api/v1/khach-hang', {
        params: { page: page - 1, size, search }
      });
      setCustomers(data.content);
      setPagination({ ...pagination, current: page, total: data.totalElements });
    } catch (error) {
      console.error(error);
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

  const handleDeleteCustomer = async (maKH) => {
    try {
      await apiClient.delete(`/api/v1/khach-hang/${maKH}`);
      message.success('Xóa khách hàng thành công');
      fetchCustomers(pagination.current, pagination.pageSize, searchText);
    } catch (error) {
      console.error(error);
      message.error('Có lỗi xảy ra khi xóa khách hàng');
    }
  };

  const handleEditClick = (record) => {
    setEditingCustomer(record);
    // Điền dữ liệu cũ vào form
    form.setFieldsValue({
      hoTen: record.hoTen,
      sdt: record.sdt,
      email: record.email,
      loaiThanhVien: record.hangKhachHang, // Giữ nguyên hạng cũ hoặc map cho đúng Enum của backend
      diemTichLuy: record.diemTichLuy
    });
    setIsModalVisible(true);
  };

  const handleUpdateCustomer = async () => {
    try {
      const values = await form.validateFields();
      
      // Dữ liệu update cần khớp với KhachHangUpdateRequestDTO
      const requestData = {
        hoTen: values.hoTen,
        sdt: values.sdt,
        email: values.email,
        // Fake enum if needed, or pass the existing one. Assumed backend handles it.
        loaiThanhVien: editingCustomer.loaiThanhVien || 'DONG', 
        diemTichLuy: editingCustomer.diemTichLuy
      };

      await apiClient.put(`/api/v1/khach-hang/${editingCustomer.maKH}`, requestData);
      message.success('Cập nhật khách hàng thành công');
      setIsModalVisible(false);
      fetchCustomers(pagination.current, pagination.pageSize, searchText);
    } catch (error) {
      console.error(error);
      message.error('Vui lòng kiểm tra lại thông tin nhập!');
    }
  };

  const columns = [
    { title: 'Mã KH', dataIndex: 'maKH', key: 'maKH' },
    { title: 'Họ tên', dataIndex: 'hoTen', key: 'hoTen' },
    { title: 'Số điện thoại', dataIndex: 'sdt', key: 'sdt' },
    { title: 'Email', dataIndex: 'email', key: 'email' },
    { title: 'Điểm tích lũy', dataIndex: 'diemTichLuy', key: 'diemTichLuy', render: (val) => <Tag color="gold">{val} điểm</Tag> },
    { title: 'Hạng', dataIndex: 'hangKhachHang', key: 'hangKhachHang', render: (val) => <Tag color="blue">{val}</Tag> },
    {
      title: 'Hành động',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Button 
            type="primary" 
            icon={<EditOutlined />} 
            onClick={() => handleEditClick(record)}
          >
            Sửa
          </Button>
          <Popconfirm
            title="Xóa khách hàng này?"
            description="Bạn có chắc chắn muốn xóa khách hàng này không?"
            onConfirm={() => handleDeleteCustomer(record.maKH)}
            okText="Có, Xóa"
            cancelText="Hủy"
            okButtonProps={{ danger: true }}
          >
            <Button type="primary" danger icon={<DeleteOutlined />}>
              Xóa
            </Button>
          </Popconfirm>
        </Space>
      )
    }
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

      <Modal
        title="Cập nhật thông tin Khách hàng"
        open={isModalVisible}
        onOk={handleUpdateCustomer}
        onCancel={() => setIsModalVisible(false)}
        okText="Lưu thay đổi"
        cancelText="Hủy"
      >
        <Form form={form} layout="vertical">
          <Form.Item
            name="hoTen"
            label="Họ tên"
            rules={[{ required: true, message: 'Vui lòng nhập họ tên!' }]}
          >
            <Input placeholder="Nhập họ tên khách hàng" />
          </Form.Item>
          <Form.Item
            name="sdt"
            label="Số điện thoại"
            rules={[{ required: true, message: 'Vui lòng nhập số điện thoại!' }]}
          >
            <Input placeholder="Nhập số điện thoại" />
          </Form.Item>
          <Form.Item
            name="email"
            label="Email"
          >
            <Input type="email" placeholder="Nhập email (Không bắt buộc)" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default CustomerManagement;
