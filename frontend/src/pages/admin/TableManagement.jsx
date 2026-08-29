import React, { useState, useEffect } from 'react';
import { Table, Tag, Button, Space, Card, message, Modal, Form, Input, InputNumber, Select, Popconfirm } from 'antd';
import { EditOutlined, DeleteOutlined, PlusOutlined } from '@ant-design/icons';
import apiClient from '../../services/apiClient';

const { Option } = Select;

const TableManagement = () => {
  const [tables, setTables] = useState([]);
  const [loading, setLoading] = useState(false);
  const [pagination, setPagination] = useState({ current: 1, pageSize: 10, total: 0 });
  
  // State quản lý Modal
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [editingId, setEditingId] = useState(null); // Lưu maBan đang sửa
  const [form] = Form.useForm();

  const fetchTables = async (page = 1, pageSize = 10) => {
    setLoading(true);
    try {
      const data = await apiClient.get(`/api/v1/ban-an?page=${page - 1}&size=${pageSize}`);
      const tableList = data.content ? data.content : data;
      setTables(tableList);
      if (data.totalElements !== undefined) {
        setPagination({
          current: page,
          pageSize: pageSize,
          total: data.totalElements
        });
      }
    } catch (error) {
      message.error(error.message || 'Lỗi khi tải danh sách bàn');
    } finally {
      setLoading(false);
    }
  };

  const handleTableChange = (newPagination) => {
    fetchTables(newPagination.current, newPagination.pageSize);
  };

  useEffect(() => {
    fetchTables();
  }, []);

  const getStatusTag = (status) => {
    switch (status) {
      case 'TRONG':
        return <Tag color="success">Trống</Tag>;
      case 'DANG_SUDUNG':
        return <Tag color="error">Đang phục vụ</Tag>;
      case 'DA_DAT':
        return <Tag color="warning">Đã đặt</Tag>;
      case 'DAT_TRUOC':
        return <Tag color="processing">Đặt trước</Tag>;
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
    setEditingId(record.maBan);
    setIsModalVisible(true);
  };

  // Xử lý Lưu form
  const handleFinish = async (values) => {
    try {
      if (editingId) {
        // Cập nhật
        await apiClient.put(`/api/v1/ban-an/${editingId}`, values);
        message.success('Cập nhật bàn thành công!');
      } else {
        // Thêm mới
        await apiClient.post('/api/v1/ban-an', values);
        message.success('Thêm bàn mới thành công!');
      }
      setIsModalVisible(false);
      fetchTables(); // Load lại dữ liệu
    } catch (error) {
      message.error(error.message || 'Có lỗi xảy ra khi lưu bàn!');
    }
  };

  // Xử lý Xóa bàn
  const handleDelete = async (maBan) => {
    try {
      await apiClient.delete(`/api/v1/ban-an/${maBan}`);
      message.success('Xóa bàn thành công!');
      fetchTables();
    } catch (error) {
      message.error(error.message || 'Không thể xóa bàn lúc này!');
    }
  };

  const columns = [
    {
      title: 'Mã Bàn',
      dataIndex: 'maBan',
      key: 'maBan',
    },
    {
      title: 'Tên Bàn',
      key: 'tenBan',
      render: (_, record) => `Bàn ${record.maBan}`
    },
    {
      title: 'Vị trí',
      dataIndex: 'viTri',
      key: 'viTri',
    },
    {
      title: 'Khu vực',
      dataIndex: 'tenKhuVuc',
      key: 'tenKhuVuc',
    },
    {
      title: 'Sức chứa',
      key: 'soGhe',
      dataIndex: 'soGhe',
      render: (soGhe) => `${soGhe} người`
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
            title="Xóa Bàn"
            description="Bạn có chắc chắn muốn xóa bàn này không?"
            onConfirm={() => handleDelete(record.maBan)}
            okText="Đồng ý"
            cancelText="Hủy"
            disabled={record.trangThai !== 'TRONG'}
          >
            <Button 
              type="primary" 
              danger 
              icon={<DeleteOutlined />} 
              size="small"
              disabled={record.trangThai !== 'TRONG'}
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
        title="Quản lý Bàn ăn" 
        extra={<Button type="primary" icon={<PlusOutlined />} onClick={showAddModal}>Thêm Bàn Mới</Button>}
      >
        <Table 
          columns={columns} 
          dataSource={tables} 
          rowKey="maBan"
          loading={loading}
          pagination={pagination}
          onChange={handleTableChange}
          scroll={{ x: 'max-content' }}
        />
      </Card>

      <Modal
        title={editingId ? 'Cập nhật Bàn' : 'Thêm Bàn Mới'}
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
          initialValues={{ trangThai: 'TRONG' }}
        >
          <Form.Item
            name="viTri"
            label="Vị trí (Tên mô tả)"
            rules={[{ required: true, message: 'Vui lòng nhập vị trí!' }]}
          >
            <Input placeholder="VD: Tầng 1 - Cửa sổ" />
          </Form.Item>

          <Form.Item
            name="soGhe"
            label="Sức chứa (Số ghế)"
            rules={[{ required: true, message: 'Vui lòng nhập số ghế!' }]}
          >
            <InputNumber min={1} max={50} style={{ width: '100%' }} placeholder="VD: 4" />
          </Form.Item>

          <Form.Item
            name="maKhuVuc"
            label="Mã Khu Vực"
            rules={[{ required: true, message: 'Vui lòng nhập mã khu vực!' }]}
          >
            <Input placeholder="VD: KV000001" />
          </Form.Item>

          <Form.Item
            name="trangThai"
            label="Trạng thái"
            rules={[{ required: true, message: 'Vui lòng chọn trạng thái!' }]}
          >
            <Select>
              <Option value="TRONG">Trống</Option>
              <Option value="DA_DAT">Đã đặt</Option>
              <Option value="DAT_TRUOC">Đặt trước</Option>
              <Option value="DANG_SUDUNG">Đang phục vụ</Option>
            </Select>
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};

export default TableManagement;
