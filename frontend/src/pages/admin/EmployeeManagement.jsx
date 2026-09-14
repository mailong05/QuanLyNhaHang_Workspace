import React, { useState, useEffect } from 'react';
import { Table, Button, Input, Space, message, Modal, Form, Popconfirm, Tag, Select, DatePicker } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined, SearchOutlined } from '@ant-design/icons';
import apiClient from '../../services/apiClient';
import dayjs from 'dayjs';

const { Option } = Select;

const EmployeeManagement = () => {
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searchText, setSearchText] = useState('');
  
  // Modal state
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [editingEmployee, setEditingEmployee] = useState(null);
  const [form] = Form.useForm();

  const fetchEmployees = async () => {
    setLoading(true);
    try {
      const data = await apiClient.get('/api/v1/nhan-vien', { params: { size: 100 } });
      setEmployees(data.content);
    } catch (error) {
      console.error(error);
      message.error('Lỗi khi tải danh sách nhân viên');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchEmployees();
  }, []);

  const handleAddClick = () => {
    setEditingEmployee(null);
    form.resetFields();
    setIsModalVisible(true);
  };

  const handleEditClick = (record) => {
    setEditingEmployee(record);
    form.setFieldsValue({
      hoTen: record.hoTen,
      sdt: record.sdt,
      email: record.email,
      chucVu: record.chucVu,
      trangThai: record.trangThai,
      luongCoBan: record.luongCoBan,
      ngayVaoLam: record.ngayVaoLam ? dayjs(record.ngayVaoLam) : null
    });
    setIsModalVisible(true);
  };

  const handleDelete = async (maNV) => {
    try {
      await apiClient.delete(`/api/v1/nhan-vien/${maNV}`);
      message.success('Xóa nhân viên thành công');
      fetchEmployees();
    } catch (error) {
      console.error(error);
      message.error('Lỗi khi xóa nhân viên');
    }
  };

  const handleSave = async () => {
    try {
      const values = await form.validateFields();
      const payload = {
        ...values,
        ngayVaoLam: values.ngayVaoLam.format('YYYY-MM-DD')
      };

      if (editingEmployee) {
        // Update
        await apiClient.put(`/api/v1/nhan-vien/${editingEmployee.maNV}`, payload);
        message.success('Cập nhật thông tin thành công');
      } else {
        // Create
        await apiClient.post('/api/v1/nhan-vien', payload);
        message.success('Thêm nhân viên mới thành công');
      }
      setIsModalVisible(false);
      fetchEmployees();
    } catch (error) {
      console.error(error);
      message.error('Vui lòng kiểm tra lại thông tin');
    }
  };

  const getRoleTag = (role) => {
    switch (role) {
      case 'QUAN_LY': return <Tag color="red">Quản lý</Tag>;
      case 'THU_NGAN': return <Tag color="gold">Thu ngân</Tag>;
      case 'DAU_BEP': return <Tag color="orange">Đầu bếp</Tag>;
      case 'PHUC_VU': return <Tag color="green">Phục vụ</Tag>;
      default: return <Tag>{role}</Tag>;
    }
  };

  const columns = [
    { title: 'Mã NV', dataIndex: 'maNV', key: 'maNV' },
    { title: 'Họ tên', dataIndex: 'hoTen', key: 'hoTen' },
    { title: 'Chức vụ', dataIndex: 'chucVu', key: 'chucVu', render: (val) => getRoleTag(val) },
    { title: 'SĐT', dataIndex: 'sdt', key: 'sdt' },
    { 
      title: 'Lương Cơ Bản', 
      dataIndex: 'luongCoBan', 
      key: 'luongCoBan',
      render: (val) => val ? val.toLocaleString('vi-VN') + ' đ' : ''
    },
    { 
      title: 'Trạng thái', 
      dataIndex: 'trangThai', 
      key: 'trangThai',
      render: (val) => val === 'DANG_LAM_VIEC' ? <Tag color="blue">Đang làm</Tag> : <Tag color="default">Nghỉ việc</Tag>
    },
    {
      title: 'Hành động',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Button type="primary" icon={<EditOutlined />} onClick={() => handleEditClick(record)}>
            Sửa
          </Button>
          <Popconfirm
            title="Nghỉ việc / Xóa?"
            description="Bạn có chắc chắn muốn xóa nhân viên này?"
            onConfirm={() => handleDelete(record.maNV)}
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
        <h2>Quản lý Nhân Viên</h2>
        <Button type="primary" icon={<PlusOutlined />} onClick={handleAddClick}>
          Thêm Nhân Viên
        </Button>
      </div>

      <Table
        columns={columns}
        dataSource={employees}
        rowKey="id"
        loading={loading}
        pagination={{ pageSize: 10 }}
      />

      <Modal
        title={editingEmployee ? "Cập nhật thông tin Nhân Viên" : "Thêm Nhân Viên Mới"}
        open={isModalVisible}
        onOk={handleSave}
        onCancel={() => setIsModalVisible(false)}
        okText="Lưu"
        cancelText="Hủy"
        width={600}
      >
        <Form form={form} layout="vertical">
          <div style={{ display: 'flex', gap: '16px' }}>
            <Form.Item
              name="hoTen"
              label="Họ tên"
              rules={[{ required: true, message: 'Bắt buộc' }]}
              style={{ flex: 1 }}
            >
              <Input placeholder="Nguyễn Văn A" />
            </Form.Item>
            <Form.Item
              name="sdt"
              label="Số điện thoại"
              rules={[{ required: true, message: 'Bắt buộc' }]}
              style={{ flex: 1 }}
            >
              <Input placeholder="09xx..." />
            </Form.Item>
          </div>

          <div style={{ display: 'flex', gap: '16px' }}>
            <Form.Item
              name="chucVu"
              label="Chức vụ"
              rules={[{ required: true, message: 'Bắt buộc' }]}
              style={{ flex: 1 }}
            >
              <Select placeholder="Chọn chức vụ">
                <Option value="QUAN_LY">Quản lý</Option>
                <Option value="THU_NGAN">Thu ngân</Option>
                <Option value="DAU_BEP">Đầu bếp</Option>
                <Option value="PHUC_VU">Phục vụ</Option>
              </Select>
            </Form.Item>
            <Form.Item
              name="trangThai"
              label="Trạng thái"
              rules={[{ required: true, message: 'Bắt buộc' }]}
              style={{ flex: 1 }}
            >
              <Select placeholder="Chọn trạng thái">
                <Option value="DANG_LAM_VIEC">Đang làm việc</Option>
                <Option value="NGHI_VIEC">Đã nghỉ việc</Option>
              </Select>
            </Form.Item>
          </div>

          <div style={{ display: 'flex', gap: '16px' }}>
            <Form.Item
              name="ngayVaoLam"
              label="Ngày vào làm"
              rules={[{ required: true, message: 'Bắt buộc' }]}
              style={{ flex: 1 }}
            >
              <DatePicker format="DD/MM/YYYY" style={{ width: '100%' }} />
            </Form.Item>
            <Form.Item
              name="luongCoBan"
              label="Lương cơ bản (VND)"
              rules={[{ required: true, message: 'Bắt buộc' }]}
              style={{ flex: 1 }}
            >
              <Input type="number" placeholder="5000000" />
            </Form.Item>
          </div>

          <Form.Item name="email" label="Email (Không bắt buộc)">
            <Input type="email" placeholder="example@gmail.com" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default EmployeeManagement;
