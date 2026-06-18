import React, { useState, useEffect } from 'react';
import { Table, Tag, Button, Space, Card, message, Modal, Form, Input, InputNumber, Select, Popconfirm, DatePicker } from 'antd';
import { EditOutlined, DeleteOutlined, PlusOutlined } from '@ant-design/icons';
import dayjs from 'dayjs';
import apiClient from '../../services/apiClient';

const { Option } = Select;

const VoucherManagement = () => {
  const [vouchers, setVouchers] = useState([]);
  const [loading, setLoading] = useState(false);
  
  // State quản lý Modal
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [editingId, setEditingId] = useState(null); // Lưu maKM đang sửa
  const [form] = Form.useForm();

  const fetchVouchers = async () => {
    setLoading(true);
    try {
      const data = await apiClient.get('/api/v1/khuyen-mai');
      const voucherList = data.content ? data.content : data;
      setVouchers(voucherList);
    } catch (error) {
      message.error(error.message || 'Lỗi khi tải danh sách khuyến mãi');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchVouchers();
  }, []);

  const getStatusTag = (status) => {
    switch (status) {
      case 'DANG_HOAT_DONG':
        return <Tag color="success">Đang hoạt động</Tag>;
      case 'DA_KET_THUC':
        return <Tag color="error">Đã kết thúc</Tag>;
      default:
        return <Tag color="default">{status}</Tag>;
    }
  };

  // Mở Modal Thêm mới
  const showAddModal = () => {
    form.resetFields();
    setEditingId(null);
    setIsModalVisible(true);
  };

  // Mở Modal Cập nhật
  const showEditModal = (record) => {
    // Chuyển đổi chuỗi ngày YYYY-MM-DD thành đối tượng dayjs trước khi nạp vào Form
    const formValues = {
      ...record,
      ngayBatDau: record.ngayBatDau ? dayjs(record.ngayBatDau) : null,
      ngayKetThuc: record.ngayKetThuc ? dayjs(record.ngayKetThuc) : null,
    };
    
    form.setFieldsValue(formValues);
    setEditingId(record.maKM); // Dựa vào maKM để cập nhật
    setIsModalVisible(true);
  };

  // Xử lý Lưu form
  const handleFinish = async (values) => {
    try {
      // Chuyển đổi lại đối tượng dayjs thành chuỗi YYYY-MM-DD
      const payload = {
        ...values,
        ngayBatDau: values.ngayBatDau ? values.ngayBatDau.format('YYYY-MM-DD') : null,
        ngayKetThuc: values.ngayKetThuc ? values.ngayKetThuc.format('YYYY-MM-DD') : null,
      };

      if (editingId) {
        // Cập nhật
        await apiClient.put(`/api/v1/khuyen-mai/${editingId}`, payload);
        message.success('Cập nhật khuyến mãi thành công!');
      } else {
        // Thêm mới
        await apiClient.post('/api/v1/khuyen-mai', payload);
        message.success('Thêm khuyến mãi mới thành công!');
      }
      setIsModalVisible(false);
      fetchVouchers(); // Load lại dữ liệu
    } catch (error) {
      message.error(error.message || 'Có lỗi xảy ra khi lưu khuyến mãi!');
    }
  };

  // Xử lý Xóa khuyến mãi
  const handleDelete = async (maKM) => {
    try {
      await apiClient.delete(`/api/v1/khuyen-mai/${maKM}`);
      message.success('Xóa khuyến mãi thành công!');
      fetchVouchers();
    } catch (error) {
      message.error(error.message || 'Không thể xóa khuyến mãi lúc này!');
    }
  };

  const columns = [
    {
      title: 'Mã KM',
      dataIndex: 'maKM',
      key: 'maKM',
    },
    {
      title: 'Tên Khuyến Mãi',
      dataIndex: 'tenKM',
      key: 'tenKM',
    },
    {
      title: 'Giá trị giảm',
      dataIndex: 'giaTriGiam',
      key: 'giaTriGiam',
      render: (value) => `${value?.toLocaleString('vi-VN')} đ/%`
    },
    {
      title: 'Điều kiện tối thiểu',
      dataIndex: 'dieuKienToiThieu',
      key: 'dieuKienToiThieu',
      render: (value) => value ? `${value.toLocaleString('vi-VN')} đ` : 'Không có'
    },
    {
      title: 'Từ ngày',
      dataIndex: 'ngayBatDau',
      key: 'ngayBatDau',
    },
    {
      title: 'Đến ngày',
      dataIndex: 'ngayKetThuc',
      key: 'ngayKetThuc',
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
      render: (_, record) => (
        <Space size="middle">
          <Button type="primary" icon={<EditOutlined />} size="small" onClick={() => showEditModal(record)}>
            Sửa
          </Button>
          <Popconfirm
            title="Xóa Khuyến Mãi"
            description="Bạn có chắc chắn muốn xóa khuyến mãi này không?"
            onConfirm={() => handleDelete(record.maKM)}
            okText="Đồng ý"
            cancelText="Hủy"
          >
            <Button 
              type="primary" 
              danger 
              icon={<DeleteOutlined />} 
              size="small"
            >
              Xóa
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <>
      <Card 
        title="Quản lý Khuyến Mãi" 
        extra={<Button type="primary" icon={<PlusOutlined />} onClick={showAddModal}>Thêm Khuyến Mãi Mới</Button>}
      >
        <Table 
          columns={columns} 
          dataSource={vouchers} 
          rowKey="id" 
          loading={loading}
          pagination={{ pageSize: 10 }}
        />
      </Card>

      <Modal
        title={editingId ? 'Cập nhật Khuyến Mãi' : 'Thêm Khuyến Mãi Mới'}
        open={isModalVisible}
        onCancel={() => setIsModalVisible(false)}
        onOk={() => form.submit()}
        okText="Lưu"
        cancelText="Hủy"
        destroyOnClose
        width={600}
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={handleFinish}
          initialValues={{ trangThai: 'DANG_HOAT_DONG' }}
        >
          <Form.Item
            name="tenKM"
            label="Tên Khuyến Mãi"
            rules={[{ required: true, message: 'Vui lòng nhập tên khuyến mãi!' }]}
          >
            <Input placeholder="VD: Giảm giá ngày lễ 2/9" />
          </Form.Item>

          <Space style={{ display: 'flex' }}>
            <Form.Item
              name="giaTriGiam"
              label="Giá trị giảm"
              rules={[{ required: true, message: 'Vui lòng nhập giá trị giảm!' }]}
            >
              <InputNumber min={0} style={{ width: '100%' }} placeholder="VD: 50000" />
            </Form.Item>

            <Form.Item
              name="dieuKienToiThieu"
              label="Đơn hàng tối thiểu"
            >
              <InputNumber min={0} style={{ width: '100%' }} placeholder="VD: 200000" />
            </Form.Item>
          </Space>

          <Space style={{ display: 'flex' }}>
            <Form.Item
              name="ngayBatDau"
              label="Ngày bắt đầu"
              rules={[{ required: true, message: 'Vui lòng chọn ngày bắt đầu!' }]}
            >
              <DatePicker style={{ width: '100%' }} format="YYYY-MM-DD" />
            </Form.Item>

            <Form.Item
              name="ngayKetThuc"
              label="Ngày kết thúc"
              dependencies={['ngayBatDau']}
              rules={[
                { required: true, message: 'Vui lòng chọn ngày kết thúc!' },
                ({ getFieldValue }) => ({
                  validator(_, value) {
                    const startDate = getFieldValue('ngayBatDau');
                    // Nếu chưa chọn ngày bắt đầu hoặc chưa chọn ngày kết thúc, bỏ qua validation này
                    if (!value || !startDate) {
                      return Promise.resolve();
                    }
                    if (value.isBefore(startDate, 'day')) {
                      return Promise.reject(new Error('Ngày kết thúc không được trước ngày bắt đầu!'));
                    }
                    return Promise.resolve();
                  },
                }),
              ]}
            >
              <DatePicker style={{ width: '100%' }} format="YYYY-MM-DD" />
            </Form.Item>
          </Space>

          <Form.Item
            name="trangThai"
            label="Trạng thái"
            rules={[{ required: true, message: 'Vui lòng chọn trạng thái!' }]}
          >
            <Select>
              <Option value="DANG_HOAT_DONG">Đang hoạt động</Option>
              <Option value="DA_KET_THUC">Đã kết thúc</Option>
            </Select>
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};

export default VoucherManagement;
