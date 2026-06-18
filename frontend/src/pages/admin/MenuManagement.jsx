import React, { useState, useEffect } from 'react';
import { Table, Tag, Button, Space, Card, message, Modal, Form, Input, InputNumber, Select, Popconfirm, Image } from 'antd';
import { EditOutlined, DeleteOutlined, PlusOutlined } from '@ant-design/icons';
import apiClient from '../../services/apiClient';

const { Option } = Select;

const MenuManagement = () => {
  const [menus, setMenus] = useState([]);
  const [loading, setLoading] = useState(false);
  
  // State quản lý Modal
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [editingId, setEditingId] = useState(null); // Lưu maMon đang sửa
  const [form] = Form.useForm();

  const fetchMenus = async () => {
    setLoading(true);
    try {
      const data = await apiClient.get('/api/v1/mon-an');
      const menuList = data.content ? data.content : data;
      setMenus(menuList);
    } catch (error) {
      message.error(error.message || 'Lỗi khi tải danh sách món ăn');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMenus();
  }, []);

  const getStatusTag = (status) => {
    switch (status) {
      case 'CON_HANG':
        return <Tag color="success">Còn hàng</Tag>;
      case 'HET_HANG':
        return <Tag color="error">Hết hàng</Tag>;
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
    form.setFieldsValue(record);
    setEditingId(record.maMon);
    setIsModalVisible(true);
  };

  // Xử lý Lưu form
  const handleFinish = async (values) => {
    try {
      if (editingId) {
        // Cập nhật
        await apiClient.put(`/api/v1/mon-an/${editingId}`, values);
        message.success('Cập nhật món ăn thành công!');
      } else {
        // Thêm mới
        await apiClient.post('/api/v1/mon-an', values);
        message.success('Thêm món mới thành công!');
      }
      setIsModalVisible(false);
      fetchMenus(); // Load lại dữ liệu
    } catch (error) {
      message.error(error.message || 'Có lỗi xảy ra khi lưu món ăn!');
    }
  };

  // Xử lý Xóa món ăn
  const handleDelete = async (maMon) => {
    try {
      await apiClient.delete(`/api/v1/mon-an/${maMon}`);
      message.success('Xóa món ăn thành công!');
      fetchMenus();
    } catch (error) {
      message.error(error.message || 'Không thể xóa món ăn lúc này!');
    }
  };

  const columns = [
    {
      title: 'Hình ảnh',
      dataIndex: 'urlHinhAnh',
      key: 'urlHinhAnh',
      render: (url) => url ? <Image width={50} height={50} src={url} style={{ objectFit: 'cover', borderRadius: '4px' }} /> : 'Không có'
    },
    {
      title: 'Mã Món',
      dataIndex: 'maMon',
      key: 'maMon',
    },
    {
      title: 'Tên Món',
      dataIndex: 'tenMon',
      key: 'tenMon',
    },
    {
      title: 'Tên Loại',
      dataIndex: 'tenLoai',
      key: 'tenLoai',
    },
    {
      title: 'Đơn Giá',
      dataIndex: 'donGia',
      key: 'donGia',
      render: (price) => `${price?.toLocaleString('vi-VN')} đ`
    },
    {
      title: 'Đơn Vị',
      dataIndex: 'donViTinh',
      key: 'donViTinh',
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
            title="Xóa Món Ăn"
            description="Bạn có chắc chắn muốn xóa món này không?"
            onConfirm={() => handleDelete(record.maMon)}
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
        title="Quản lý Món ăn" 
        extra={<Button type="primary" icon={<PlusOutlined />} onClick={showAddModal}>Thêm Món Mới</Button>}
      >
        <Table 
          columns={columns} 
          dataSource={menus} 
          rowKey="id" 
          loading={loading}
          pagination={{ pageSize: 10 }}
        />
      </Card>

      <Modal
        title={editingId ? 'Cập nhật Món Ăn' : 'Thêm Món Mới'}
        open={isModalVisible}
        onCancel={() => setIsModalVisible(false)}
        onOk={() => form.submit()}
        okText="Lưu"
        cancelText="Hủy"
        destroyOnClose
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={handleFinish}
          initialValues={{ trangThai: 'CON_HANG' }}
        >
          <Form.Item
            name="tenMon"
            label="Tên Món Ăn"
            rules={[{ required: true, message: 'Vui lòng nhập tên món!' }]}
          >
            <Input placeholder="VD: Gà rán" />
          </Form.Item>

          <Form.Item
            name="donGia"
            label="Đơn giá"
            rules={[{ required: true, message: 'Vui lòng nhập đơn giá!' }]}
          >
            <InputNumber min={0} style={{ width: '100%' }} placeholder="VD: 50000" />
          </Form.Item>

          <Form.Item
            name="donViTinh"
            label="Đơn vị tính"
          >
            <Input placeholder="VD: Đĩa, Phần, Ly" />
          </Form.Item>

          <Form.Item
            name="tenLoai"
            label="Tên Loại Món"
            rules={[{ required: true, message: 'Vui lòng nhập phân loại!' }]}
          >
            <Input placeholder="VD: Món khai vị, Đồ uống" />
          </Form.Item>

          <Form.Item
            name="urlHinhAnh"
            label="Đường dẫn Hình ảnh (URL)"
          >
            <Input placeholder="VD: https://example.com/image.jpg" />
          </Form.Item>

          <Form.Item
            name="trangThai"
            label="Trạng thái"
            rules={[{ required: true, message: 'Vui lòng chọn trạng thái!' }]}
          >
            <Select>
              <Option value="CON_HANG">Còn hàng</Option>
              <Option value="HET_HANG">Hết hàng</Option>
            </Select>
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};

export default MenuManagement;
