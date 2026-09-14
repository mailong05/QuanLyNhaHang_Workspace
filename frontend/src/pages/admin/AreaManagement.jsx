import React, { useState, useEffect } from 'react';
import { Table, Button, Input, Space, message, Modal, Form, Popconfirm } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import apiClient from '../../services/apiClient';

const AreaManagement = () => {
  const [areas, setAreas] = useState([]);
  const [loading, setLoading] = useState(false);
  
  // Modal state
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [editingArea, setEditingArea] = useState(null);
  const [form] = Form.useForm();

  const fetchAreas = async () => {
    setLoading(true);
    try {
      const data = await apiClient.get('/api/v1/khu-vuc', { params: { size: 100 } });
      setAreas(data.content);
    } catch (error) {
      console.error(error);
      message.error('Lỗi khi tải danh sách khu vực');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAreas();
  }, []);

  const handleAddClick = () => {
    setEditingArea(null);
    form.resetFields();
    setIsModalVisible(true);
  };

  const handleEditClick = (record) => {
    setEditingArea(record);
    form.setFieldsValue({
      tenKhuVuc: record.tenKhuVuc
    });
    setIsModalVisible(true);
  };

  const handleDelete = async (maKhuVuc) => {
    try {
      await apiClient.delete(`/api/v1/khu-vuc/${maKhuVuc}`);
      message.success('Xóa khu vực thành công');
      fetchAreas();
    } catch (error) {
      console.error(error);
      message.error('Lỗi khi xóa khu vực');
    }
  };

  const handleSave = async () => {
    try {
      const values = await form.validateFields();
      if (editingArea) {
        // Update
        await apiClient.put(`/api/v1/khu-vuc/${editingArea.maKhuVuc}`, values);
        message.success('Cập nhật khu vực thành công');
      } else {
        // Create
        await apiClient.post('/api/v1/khu-vuc', values);
        message.success('Thêm mới khu vực thành công');
      }
      setIsModalVisible(false);
      fetchAreas();
    } catch (error) {
      console.error(error);
      message.error('Vui lòng kiểm tra lại thông tin');
    }
  };

  const columns = [
    { title: 'Mã Khu Vực', dataIndex: 'maKhuVuc', key: 'maKhuVuc' },
    { title: 'Tên Khu Vực', dataIndex: 'tenKhuVuc', key: 'tenKhuVuc' },
    {
      title: 'Hành động',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Button type="primary" icon={<EditOutlined />} onClick={() => handleEditClick(record)}>
            Sửa
          </Button>
          <Popconfirm
            title="Xóa khu vực này?"
            description="Bạn có chắc chắn muốn xóa khu vực này không?"
            onConfirm={() => handleDelete(record.maKhuVuc)}
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
        <h2>Quản lý Khu Vực</h2>
        <Button type="primary" icon={<PlusOutlined />} onClick={handleAddClick}>
          Thêm Khu Vực
        </Button>
      </div>

      <Table
        columns={columns}
        dataSource={areas}
        rowKey="id"
        loading={loading}
        pagination={false}
      />

      <Modal
        title={editingArea ? "Cập nhật Khu Vực" : "Thêm mới Khu Vực"}
        open={isModalVisible}
        onOk={handleSave}
        onCancel={() => setIsModalVisible(false)}
        okText="Lưu"
        cancelText="Hủy"
      >
        <Form form={form} layout="vertical">
          <Form.Item
            name="tenKhuVuc"
            label="Tên Khu Vực"
            rules={[{ required: true, message: 'Vui lòng nhập tên khu vực!' }]}
          >
            <Input placeholder="Nhập tên khu vực (VD: Tầng 1, Sân Vườn)" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default AreaManagement;
