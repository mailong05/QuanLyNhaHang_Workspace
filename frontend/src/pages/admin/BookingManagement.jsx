import React, { useState, useEffect } from 'react';
import { Table, Tag, Button, Space, Card, message, Tabs, Modal, Form, Select, Popconfirm, Row, Col, Typography, InputNumber, DatePicker, Input, Radio } from 'antd';
import { CheckCircleOutlined, CloseCircleOutlined, RightCircleOutlined, EditOutlined, RetweetOutlined, MergeCellsOutlined, PlusOutlined } from '@ant-design/icons';
import dayjs from 'dayjs';
import apiClient from '../../services/apiClient';

const { TabPane } = Tabs;
const { Option } = Select;
const { Title, Text } = Typography;

const BookingManagement = () => {
  const [bookings, setBookings] = useState([]);
  const [allTables, setAllTables] = useState([]);
  const [loading, setLoading] = useState(false);
  const [activeTab, setActiveTab] = useState('CHO_XAC_NHAN');

  // Modal State cho việc Xếp Bàn (Duyệt phiếu)
  const [isAssignModalVisible, setIsAssignModalVisible] = useState(false);
  const [assigningBooking, setAssigningBooking] = useState(null);
  const [form] = Form.useForm();

  // Modal State cho việc Sửa phiếu (CHO_XAC_NHAN, DA_XAC_NHAN)
  const [isEditModalVisible, setIsEditModalVisible] = useState(false);
  const [editingBooking, setEditingBooking] = useState(null);
  const [formEdit] = Form.useForm();

  // Modal State cho việc Đổi/Gộp Bàn (DANG_PHUC_VU)
  const [isTransferMergeModalVisible, setIsTransferMergeModalVisible] = useState(false);
  const [isMapModalVisible, setIsMapModalVisible] = useState(false);
  const [transferMergeBooking, setTransferMergeBooking] = useState(null);
  const [transferMergeTab, setTransferMergeTab] = useState('CHANGE');
  const [selectedMapTable, setSelectedMapTable] = useState(null);
  const [mapFilterArea, setMapFilterArea] = useState('TANG_TRET');

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

  const fetchAllTables = async () => {
    try {
      const data = await apiClient.get('/api/v1/ban-an');
      const tableList = (data.content ? data.content : data).map((t, index) => ({
        ...t,
        khuVuc: t.khuVuc || (index % 3 === 0 ? 'PHONG_VIP' : index % 2 === 0 ? 'LAU_1' : 'TANG_TRET')
      }));
      setAllTables(tableList);
    } catch (error) {
      message.error('Lỗi khi tải danh sách bàn');
    }
  };

  useEffect(() => {
    fetchBookings();
    fetchAllTables();
  }, []);

  const displayBookings = activeTab === 'HOAN_THANH' 
    ? bookings.filter(b => b.trangThai === 'HOAN_THANH' || b.trangThai === 'HOAN_TAT') 
    : bookings.filter(b => b.trangThai === activeTab);

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
      case 'HOAN_TAT':
        return <Tag color="success">Hoàn thành</Tag>;
      case 'DA_GOP_BAN':
        return <Tag color="purple">Đã gộp bàn</Tag>;
      default:
        return <Tag color="default">{status}</Tag>;
    }
  };

  // --- ACTIONS ---

  const showAssignTableModal = (record) => {
    setAssigningBooking(record);
    form.resetFields();
    fetchAllTables();
    setIsAssignModalVisible(true);
  };

  const handleApprove = async (values) => {
    try {
      const payload = {
        thoiGianDen: assigningBooking.thoiGianDen,
        soLuongNguoi: assigningBooking.soLuongNguoi,
        ghiChu: assigningBooking.ghiChu,
        tienDatCoc: assigningBooking.tienDatCoc,
        trangThai: 'DA_XAC_NHAN',
        chiTiets: [{ maBan: values.maBan, maPhieuDat: assigningBooking.maPhieuDat }]
      };
      await apiClient.put(`/api/v1/phieu-dat-ban/${assigningBooking.maPhieuDat}`, payload);
      message.success('Duyệt phiếu và xếp bàn thành công!');
      setIsAssignModalVisible(false);
      fetchBookings();
    } catch (error) {
      message.error(error.message || 'Lỗi khi duyệt phiếu');
    }
  };

  const handleCancelBooking = async (record) => {
    try {
      const payload = { ...record, trangThai: 'DA_HUY' };
      await apiClient.put(`/api/v1/phieu-dat-ban/${record.maPhieuDat}`, payload);
      message.success('Đã hủy phiếu đặt bàn!');
      fetchBookings();
    } catch (error) {
      message.error(error.message || 'Lỗi khi hủy phiếu');
    }
  };

  const handleCheckIn = async (record) => {
    try {
      const payload = { ...record, trangThai: 'DANG_PHUC_VU' };
      await apiClient.put(`/api/v1/phieu-dat-ban/${record.maPhieuDat}`, payload);
      message.success('Khách đã nhận bàn thành công!');
      fetchBookings();
    } catch (error) {
      message.error(error.message || 'Lỗi khi check-in nhận bàn');
    }
  };

  // Sửa Phiếu logic
  const handleOpenEdit = (record) => {
    setEditingBooking(record);
    formEdit.setFieldsValue({
      hoTenKH: record.hoTenKH,
      sdtKH: record.sdtKH || record.sdt,
      thoiGianDen: dayjs(record.thoiGianDen),
      soLuongNguoi: record.soLuongNguoi,
      ghiChu: record.ghiChu,
      maBan: record.chiTiets?.[0]?.maBan
    });
    setIsEditModalVisible(true);
  };

  const handleSaveEditBooking = async (values) => {
    try {
      const thoiGianDenStr = values.thoiGianDen.format('YYYY-MM-DDTHH:mm:ss');
      const targetTable = values.maBan || editingBooking?.chiTiets?.[0]?.maBan;

      // Nếu có bàn, phải check availability (loại trừ chính phiếu này)
      if (targetTable) {
        const checkRes = await apiClient.get(`/api/v1/phieu-dat-ban/check-availability?maBan=${targetTable}&thoiGianDen=${thoiGianDenStr}&excludePhieuId=${editingBooking.id}`);
        const isAvailable = checkRes;
        
        if (!isAvailable) {
            Modal.warning({
                title: 'Bàn đã bận!',
                content: `Bàn ${targetTable} đang có khách hoặc đã được đặt trước trong khoảng thời gian này. Vui lòng chọn bàn khác hoặc đổi giờ.`
            });
            return;
        }
      }

      const payload = {
        ...editingBooking,
        hoTenKH: values.hoTenKH,
        sdtKH: values.sdtKH,
        thoiGianDen: thoiGianDenStr,
        soLuongNguoi: values.soLuongNguoi,
        ghiChu: values.ghiChu,
        chiTiets: targetTable ? [{ maBan: targetTable, maPhieuDat: editingBooking.maPhieuDat }] : []
      };
      await apiClient.put(`/api/v1/phieu-dat-ban/${editingBooking.maPhieuDat}`, payload);
      message.success('Cập nhật phiếu đặt bàn thành công!');
      setIsEditModalVisible(false);
      fetchBookings();
    } catch (error) {
      message.error('Lỗi cập nhật phiếu');
    }
  };

  // Đổi/Gộp Bàn logic
  const handleOpenTransferMerge = (record) => {
    setTransferMergeBooking(record);
    setSelectedMapTable(null);
    setTransferMergeTab('CHANGE');
    fetchAllTables();
    setIsTransferMergeModalVisible(true);
  };

  // Thuật toán gộp Order (Deep Merge)
  const mockDeepMergeOrders = (orderA, orderC) => {
    const mergedItems = [...orderC];
    orderA.forEach(itemA => {
      const existingItem = mergedItems.find(item => item.maMon === itemA.maMon);
      if (existingItem) {
        existingItem.soLuong += itemA.soLuong;
        existingItem.thanhTien = existingItem.soLuong * existingItem.donGia;
      } else {
        mergedItems.push({ ...itemA });
      }
    });
    return mergedItems;
  };

  const submitTransferMerge = async () => {
    if (!selectedMapTable) {
      message.error('Vui lòng chọn một bàn từ sơ đồ!');
      return;
    }

    const { soLuongNguoi } = transferMergeBooking;
    if (transferMergeTab !== 'ADD' && selectedMapTable.soGhe < soLuongNguoi) {
      message.warning('Lưu ý: Bàn được chọn có sức chứa nhỏ hơn số lượng khách!');
    }

    try {
      if (transferMergeTab === 'CHANGE') {
        // ĐỔI BÀN SANG BÀN TRỐNG
        const payload = {
          ...transferMergeBooking,
          chiTiets: [{ maBan: selectedMapTable.maBan, maPhieuDat: transferMergeBooking.maPhieuDat }]
        };
        await apiClient.put(`/api/v1/phieu-dat-ban/${transferMergeBooking.maPhieuDat}`, payload);
        message.success(`Đổi bàn thành công sang Bàn ${selectedMapTable.maBan}!`);
      } else if (transferMergeTab === 'ADD') {
        // GHÉP THÊM BÀN TRỐNG
        const oldTables = transferMergeBooking.chiTiets ? transferMergeBooking.chiTiets.map(ct => ({ maBan: ct.maBan, maPhieuDat: transferMergeBooking.maPhieuDat })) : [];
        oldTables.push({ maBan: selectedMapTable.maBan, maPhieuDat: transferMergeBooking.maPhieuDat });
        
        const payload = {
          ...transferMergeBooking,
          chiTiets: oldTables
        };
        await apiClient.put(`/api/v1/phieu-dat-ban/${transferMergeBooking.maPhieuDat}`, payload);
        message.success(`Ghép bàn thành công! Đã thêm Bàn ${selectedMapTable.maBan} vào phiếu.`);
      } else {
        // GỘP HÓA ĐƠN
        const payload = {
            maPhieuDatNguon: transferMergeBooking.maPhieuDat,
            maBanDich: selectedMapTable.maBan
        };
        await apiClient.post('/api/v1/pos/gop-ban', payload);
        message.success(`Gộp hóa đơn thành công vào Bàn ${selectedMapTable.maBan}!`);
      }
      setIsTransferMergeModalVisible(false);
      fetchBookings();
    } catch (error) {
      message.error('Có lỗi xảy ra khi xử lý!');
    }
  };

  // Render Sơ đồ Bàn Thu Nhỏ
  const renderMiniTableMap = (allowedStatuses, currentTable) => {
    const isGhepThemBan = transferMergeTab === 'ADD';
    // Tìm khu vực của currentTable
    const currentTableArea = allTables.find(t => t.maBan === currentTable)?.khuVuc;
    const targetArea = isGhepThemBan ? (currentTableArea || mapFilterArea) : mapFilterArea;

    const filteredTables = allTables.filter(t => 
      t.khuVuc === targetArea && allowedStatuses.includes(t.trangThai) && 
      (currentTable !== t.maBan) // Không hiện bàn hiện tại của khách
    );

    const getTableStyle = (status) => {
      switch (status) {
        case 'TRONG': return { borderColor: '#52c41a', backgroundColor: '#f6ffed' };
        case 'DANG_SUDUNG': return { borderColor: '#ff4d4f', backgroundColor: '#fff1f0' };
        case 'DA_DAT': return { borderColor: '#faad14', backgroundColor: '#fffbe6' };
        default: return { borderColor: '#d9d9d9', backgroundColor: '#ffffff' };
      }
    };

    const getStatusText = (status) => {
      switch (status) {
        case 'TRONG': return 'Trống';
        case 'DANG_SUDUNG': return 'Đang phục vụ';
        case 'DA_DAT': return 'Đã đặt';
        default: return status;
      }
    };

    return (
      <div style={{ marginTop: 16 }}>
        {!isGhepThemBan ? (
          <Tabs activeKey={mapFilterArea} onChange={setMapFilterArea} items={[
            { key: 'TANG_TRET', label: 'Tầng trệt' },
            { key: 'LAU_1', label: 'Lầu 1' },
            { key: 'PHONG_VIP', label: 'Phòng VIP' }
          ]} />
        ) : (
          <div style={{ marginBottom: 16, padding: 8, backgroundColor: '#e6f7ff', borderRadius: 4, border: '1px solid #91d5ff' }}>
            <Typography.Text type="secondary">Đang lọc bàn cùng khu vực ({currentTableArea}) để ghép bàn.</Typography.Text>
          </div>
        )}
        <Row gutter={[12, 12]} style={{ maxHeight: 300, overflowY: 'auto' }}>
          {filteredTables.length > 0 ? filteredTables.map(table => {
            const isSelected = selectedMapTable?.id === table.id;
            const styleObj = getTableStyle(table.trangThai);
            return (
              <Col span={8} key={table.id}>
                <Card
                  hoverable
                  onClick={() => setSelectedMapTable(table)}
                  style={{
                    ...styleObj,
                    borderWidth: isSelected ? '2px' : '1px',
                    borderStyle: 'solid',
                    borderColor: isSelected ? '#1890ff' : styleObj.borderColor,
                    boxShadow: isSelected ? '0 0 8px rgba(24,144,255,0.5)' : 'none',
                    textAlign: 'center',
                    padding: '8px'
                  }}
                  bodyStyle={{ padding: 0 }}
                >
                  <Title level={5} style={{ margin: 0, fontSize: '14px' }}>Bàn {table.maBan}</Title>
                  <Text style={{ fontSize: '12px' }}>{getStatusText(table.trangThai)}</Text>
                  <div style={{ marginTop: '4px' }}>
                    <Text type="secondary" style={{ fontSize: '11px' }}>Sức chứa: {table.soGhe} người</Text>
                  </div>
                </Card>
              </Col>
            );
          }) : (
            <div style={{ padding: '20px', width: '100%', textAlign: 'center' }}>
              <Text type="secondary">Không có bàn nào phù hợp trong khu vực này.</Text>
            </div>
          )}
        </Row>
      </div>
    );
  };

  const columns = [
    { title: 'Mã Phiếu', dataIndex: 'maPhieuDat', key: 'maPhieuDat' },
    { title: 'Tên Khách', dataIndex: 'hoTenKH', key: 'hoTenKH' },
    { title: 'SĐT', key: 'sdt', render: (_, record) => record.sdtKH || record.sdt || 'N/A' },
    { title: 'Thời gian đến', dataIndex: 'thoiGianDen', key: 'thoiGianDen', render: (time) => time ? dayjs(time).format('HH:mm DD/MM/YYYY') : '' },
    { title: 'Số người', dataIndex: 'soLuongNguoi', key: 'soLuongNguoi', render: (num) => `${num} người` },
      { title: 'Tiền cọc', dataIndex: 'tienDatCoc', key: 'tienDatCoc', render: (val) => val ? val.toLocaleString('vi-VN') + ' đ' : '0 đ' },
      { title: 'Mã bàn', key: 'maBan', render: (_, record) => record.chiTiets && record.chiTiets.length > 0 ? record.chiTiets.map(ct => ct.maBan).join(', ') : 'Chưa xếp' },
    { title: 'Trạng thái', dataIndex: 'trangThai', key: 'trangThai', render: (trangThai) => getStatusTag(trangThai) },
    {
      title: 'Hành động',
      key: 'action',
      render: (_, record) => {
        return (
          <Space size="middle">
            {record.trangThai === 'CHO_XAC_NHAN' && (
              <>
                <Button type="primary" icon={<CheckCircleOutlined />} size="small" style={{ backgroundColor: '#52c41a' }} onClick={() => showAssignTableModal(record)}>
                  Duyệt
                </Button>
                <Button icon={<EditOutlined />} size="small" onClick={() => handleOpenEdit(record)}>Sửa</Button>
                <Popconfirm title="Hủy Phiếu?" onConfirm={() => handleCancelBooking(record)}>
                  <Button type="primary" danger icon={<CloseCircleOutlined />} size="small">Hủy</Button>
                </Popconfirm>
              </>
            )}
            {record.trangThai === 'DA_XAC_NHAN' && (
              <>
                <Button type="primary" icon={<RightCircleOutlined />} size="small" onClick={() => handleCheckIn(record)}>
                  Đã nhận bàn
                </Button>
                <Button icon={<EditOutlined />} size="small" onClick={() => handleOpenEdit(record)}>Sửa</Button>
              </>
            )}
            {record.trangThai === 'DANG_PHUC_VU' && (
              <Button type="dashed" icon={<RetweetOutlined />} size="small" onClick={() => handleOpenTransferMerge(record)}>
                Đổi / Gộp Bàn
              </Button>
            )}
          </Space>
        );
      },
    },
  ];

  return (
    <>
      <Card title="Quản lý Phiếu Đặt Bàn">
        <Tabs activeKey={activeTab} onChange={(key) => setActiveTab(key)}>
          <TabPane tab="Chờ xác nhận" key="CHO_XAC_NHAN" />
          <TabPane tab="Đã xác nhận" key="DA_XAC_NHAN" />
          <TabPane tab="Đang phục vụ" key="DANG_PHUC_VU" />
          <TabPane tab="Đã hoàn thành" key="HOAN_THANH" />
          <TabPane tab="Đã gộp bàn" key="DA_GOP_BAN" />
          <TabPane tab="Đã hủy" key="DA_HUY" />
        </Tabs>
        <Table columns={columns} dataSource={displayBookings} rowKey="id" loading={loading} pagination={{ pageSize: 10 }} />
      </Card>

      {/* Modal Xếp bàn (Duyệt) */}
      <Modal title={`Xếp bàn cho phiếu ${assigningBooking?.maPhieuDat || ''}`} open={isAssignModalVisible} onCancel={() => setIsAssignModalVisible(false)} onOk={() => form.submit()} okText="Xác nhận Duyệt" destroyOnClose>
        <Form form={form} layout="vertical" onFinish={handleApprove}>
          <Form.Item name="maBan" label="Chọn bàn trống" rules={[{ required: true }]}>
            <Select placeholder="-- Chọn Bàn --">
              {allTables.filter(t => t.trangThai === 'TRONG').map(table => (
                <Option key={table.id} value={table.maBan}>
                  Bàn {table.maBan} - {table.viTri} (Sức chứa: {table.soGhe})
                </Option>
              ))}
            </Select>
          </Form.Item>
        </Form>
      </Modal>

      {/* Modal Sửa Phiếu */}
      <Modal title="Chỉnh sửa Phiếu Đặt Bàn" open={isEditModalVisible} onCancel={() => setIsEditModalVisible(false)} onOk={() => formEdit.submit()} okText="Lưu Thay Đổi" width={600} destroyOnClose>
        <Form form={formEdit} layout="vertical" onFinish={handleSaveEditBooking}>
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item name="hoTenKH" label="Tên khách hàng" rules={[{ required: true }]}><Input /></Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item name="sdtKH" label="Số điện thoại" rules={[{ required: true }]}><Input /></Form.Item>
            </Col>
          </Row>
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item name="thoiGianDen" label="Thời gian đến" rules={[{ required: true }]}><DatePicker showTime format="DD/MM/YYYY HH:mm" style={{ width: '100%' }} /></Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item name="soLuongNguoi" label="Số lượng người" rules={[{ required: true }]}><InputNumber min={1} style={{ width: '100%' }} /></Form.Item>
            </Col>
          </Row>
          <Form.Item name="ghiChu" label="Ghi chú thêm"><Input.TextArea rows={2} /></Form.Item>
          {(editingBooking?.trangThai === 'DA_XAC_NHAN' || editingBooking?.trangThai === 'CHO_XAC_NHAN') && (
            <Form.Item name="maBan" label="Bàn đã xếp (Có thể chọn lại bàn khác)">
              <Space.Compact style={{ width: '100%' }}>
                <Input readOnly value={formEdit.getFieldValue('maBan') || 'Chưa xếp'} style={{ width: '70%' }} />
                <Button type="primary" onClick={() => {
                   setMapFilterArea('TANG_TRET');
                   setSelectedMapTable(null);
                   setIsMapModalVisible(true);
                }} style={{ width: '30%' }}>Đổi Bàn</Button>
              </Space.Compact>
            </Form.Item>
          )}
        </Form>
      </Modal>

      {/* Modal Đổi / Gộp Bàn */}
      <Modal title={`Chuyển / Gộp Bàn - Đang phục vụ tại Bàn ${transferMergeBooking?.chiTiets?.[0]?.maBan || 'N/A'}`} open={isTransferMergeModalVisible} onCancel={() => setIsTransferMergeModalVisible(false)} onOk={submitTransferMerge} okText="Xác Nhận" width={700} destroyOnClose>
        <div style={{ marginBottom: 16 }}>
          <Text strong>Khách hàng: </Text><Text>{transferMergeBooking?.hoTenKH} ({transferMergeBooking?.soLuongNguoi} người)</Text>
        </div>
        <Radio.Group value={transferMergeTab} onChange={e => {
            setTransferMergeTab(e.target.value);
            setSelectedMapTable(null);
          }} style={{ marginBottom: 16 }}>
          <Radio.Button value="CHANGE"><RetweetOutlined /> Đổi Bàn</Radio.Button>
          <Radio.Button value="ADD"><PlusOutlined /> Ghép Thêm Bàn</Radio.Button>
          <Radio.Button value="MERGE"><MergeCellsOutlined /> Gộp Hóa Đơn</Radio.Button>
        </Radio.Group>
        <Card size="small" title="Chọn bàn mục tiêu từ Sơ đồ">
          {transferMergeTab === 'CHANGE' || transferMergeTab === 'ADD'
            ? renderMiniTableMap(['TRONG'], transferMergeBooking?.chiTiets?.[0]?.maBan) 
            : renderMiniTableMap(['DANG_SUDUNG', 'DA_DAT'], transferMergeBooking?.chiTiets?.[0]?.maBan)}
        </Card>
      </Modal>

      {/* Modal Chọn Bàn từ Sơ đồ */}
      <Modal zIndex={1050} title="Chọn Bàn Từ Sơ Đồ" open={isMapModalVisible} onCancel={() => setIsMapModalVisible(false)} onOk={() => {
        if (selectedMapTable) {
            formEdit.setFieldsValue({ maBan: selectedMapTable.maBan });
            setIsMapModalVisible(false);
        } else {
            message.warning('Vui lòng chọn 1 bàn');
        }
      }} okText="Xác nhận chọn bàn" width={700}>
         <Card size="small" title="Chọn bàn mục tiêu từ Sơ đồ">
          {renderMiniTableMap(['TRONG', 'DA_DAT', 'DANG_SUDUNG'], editingBooking?.chiTiets?.[0]?.maBan)}
         </Card>
      </Modal>
    </>
  );
};

export default BookingManagement;
