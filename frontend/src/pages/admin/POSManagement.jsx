import React, { useState, useEffect } from 'react';
import { Row, Col, Card, message, Typography, Badge, Button, Table, Modal, Space, Image, InputNumber, Radio, Form, Input, DatePicker, Tabs } from 'antd';
import { PlusOutlined, DollarOutlined, CoffeeOutlined, CalendarOutlined, RetweetOutlined, MergeCellsOutlined } from '@ant-design/icons';
import apiClient from '../../services/apiClient';
import './POSManagement.css';

const { Title, Text } = Typography;

const POSManagement = () => {
  const [tables, setTables] = useState([]);
  const [loading, setLoading] = useState(false);
  const [selectedTable, setSelectedTable] = useState(null);

  // State cho Sơ đồ bàn
  const [filterStatus, setFilterStatus] = useState('ALL');
  const [filterArea, setFilterArea] = useState('TANG_TRET');

  // State cho Khu vực Order
  const [currentOrder, setCurrentOrder] = useState(null);
  const [orderItems, setOrderItems] = useState([]);
  const [loadingOrder, setLoadingOrder] = useState(false);

  // State cho Modal Menu
  const [isMenuModalVisible, setIsMenuModalVisible] = useState(false);
  const [menuList, setMenuList] = useState([]);
  const [loadingMenu, setLoadingMenu] = useState(false);

  // State cho Checkout Modal
  const [isCheckoutModalVisible, setIsCheckoutModalVisible] = useState(false);
  const [checkoutMethod, setCheckoutMethod] = useState('TIEN_MAT');
  const [amountGiven, setAmountGiven] = useState(0);
  const [loadingCheckout, setLoadingCheckout] = useState(false);

  // State cho Đặt Bàn Trước Modal
  const [isReservationModalVisible, setIsReservationModalVisible] = useState(false);
  const [formReservation] = Form.useForm();

  // State cho Doi/Gop Ban
  const [isTransferMergeModalVisible, setIsTransferMergeModalVisible] = useState(false);
  const [transferMergeTab, setTransferMergeTab] = useState('CHANGE');
  const [selectedMapTable, setSelectedMapTable] = useState(null);
  const [mapFilterArea, setMapFilterArea] = useState('TANG_TRET');


  const fetchTables = async () => {
    setLoading(true);
    try {
      const data = await apiClient.get('/api/v1/ban-an');
      const tableList = (data.content ? data.content : data).map((t, index) => ({
        ...t,
        khuVuc: t.khuVuc || (index % 3 === 0 ? 'PHONG_VIP' : index % 2 === 0 ? 'LAU_1' : 'TANG_TRET')
      }));
      setTables(tableList);
      
      // Update selectedTable reference to reflect new status if it was selected
      if (selectedTable) {
        const updatedTable = tableList.find(t => t.id === selectedTable.id);
        if (updatedTable) setSelectedTable(updatedTable);
      }
    } catch (error) {
      message.error(error.message || 'Lỗi khi tải danh sách bàn');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTables();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // Fetch Order details when a DANG_PHUC_VU table is selected
  useEffect(() => {
    if (selectedTable && selectedTable.trangThai === 'DANG_SUDUNG') {
      fetchOrderDetails(selectedTable.id);
    } else {
      setCurrentOrder(null);
      setOrderItems([]);
    }
  }, [selectedTable]);

  const fetchOrderDetails = async (banId) => {
    setLoadingOrder(true);
    try {
      // Gọi API giả định
      const res = await apiClient.get(`/api/v1/pos/ban/${banId}/hoa-don`);
      setCurrentOrder(res);
      setOrderItems(res.chiTiets || res.items || []);
    } catch (error) {
      // Silently handle or show error
      console.log('Chưa có hóa đơn cho bàn này hoặc API chưa sẵn sàng', error);
      setCurrentOrder(null);
      setOrderItems([]);
    } finally {
      setLoadingOrder(false);
    }
  };

  const getTableStyle = (status) => {
    switch (status) {
      case 'TRONG':
        return { borderColor: '#52c41a', backgroundColor: '#f6ffed' };
      case 'DANG_SUDUNG':
        return { borderColor: '#ff4d4f', backgroundColor: '#fff1f0' };
      case 'DA_DAT':
        return { borderColor: '#faad14', backgroundColor: '#fffbe6' };
      case 'DAT_TRUOC':
        return { borderColor: '#1890ff', backgroundColor: '#e6f7ff' };
      default:
        return { borderColor: '#d9d9d9', backgroundColor: '#ffffff' };
    }
  };

  const getStatusText = (status) => {
    switch (status) {
      case 'TRONG': return 'Trống';
      case 'DANG_SUDUNG': return 'Đang phục vụ';
      case 'DA_DAT': return 'Đã đặt';
      case 'DAT_TRUOC': return 'Đặt trước';
      default: return status;
    }
  };

  const handleTableClick = (table) => {
    setSelectedTable(table);
  };

  // --- ACTIONS CHO ORDER ---

  const handleOpenTable = async () => {
    if (!selectedTable) return;
    try {
      await apiClient.post('/api/v1/pos/mo-ban', { banId: selectedTable.id });
      message.success(`Đã mở bàn ${selectedTable.maBan}!`);
      fetchTables(); // Sẽ tự trigger lại effect update selectedTable và fetchOrder
    } catch (error) {
      message.error(error.message || 'Lỗi khi mở bàn');
    }
  };

  const showMenuModal = async () => {
    setIsMenuModalVisible(true);
    setLoadingMenu(true);
    try {
      const data = await apiClient.get('/api/v1/mon-an');
      const menus = data.content ? data.content : data;
      setMenuList(menus);
    } catch (error) {
      message.error('Lỗi khi tải Menu');
    } finally {
      setLoadingMenu(false);
    }
  };

  const handleAddItemToOrder = async (monAn) => {
    try {
      const payload = {
        banId: selectedTable.id,
        maMon: monAn.maMon,
        soLuong: 1
      };
      await apiClient.post('/api/v1/pos/them-mon', payload);
      message.success(`Đã thêm ${monAn.tenMon} vào order!`);
      // Reload order details
      fetchOrderDetails(selectedTable.id);
    } catch (error) {
      message.error(error.message || 'Lỗi khi thêm món');
    }
  };


  const handleOpenTransferMerge = () => {
    setSelectedMapTable(null);
    setTransferMergeTab('CHANGE');
    setIsTransferMergeModalVisible(true);
  };

  const submitTransferMerge = () => {
    if (!selectedMapTable) {
      message.error('Vui lòng chọn một bàn từ sơ đồ!');
      return;
    }
    if (transferMergeTab === 'CHANGE') {
      message.success(`Đổi bàn thành công sang Bàn ${selectedMapTable.maBan}!`);
    } else {
      message.success(`Gộp bàn thành công vào Bàn ${selectedMapTable.maBan}!`);
    }
    setIsTransferMergeModalVisible(false);
    fetchTables();
    setSelectedTable(null);
  };

  const renderMiniTableMap = (allowedStatuses) => {
    const filteredTables = tables.filter(t => 
      t.khuVuc === mapFilterArea && allowedStatuses.includes(t.trangThai) && 
      (selectedTable?.maBan !== t.maBan)
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
        default: return 'Không xác định';
      }
    };

    return (
      <>
        <Tabs activeKey={mapFilterArea} onChange={setMapFilterArea}>
          <Tabs.TabPane tab="Tầng trệt" key="TANG_TRET" />
          <Tabs.TabPane tab="Lầu 1" key="LAU_1" />
          <Tabs.TabPane tab="Phòng VIP" key="PHONG_VIP" />
        </Tabs>
        <div style={{ display: 'flex', gap: '16px', flexWrap: 'wrap', marginTop: 16 }}>
          {filteredTables.map(t => (
            <Card 
              key={t.id}
              hoverable
              onClick={() => setSelectedMapTable(t)}
              style={{ 
                width: 140, 
                textAlign: 'center',
                cursor: 'pointer',
                borderWidth: 2,
                borderStyle: 'solid',
                borderColor: selectedMapTable?.id === t.id ? '#1890ff' : getTableStyle(t.trangThai).borderColor,
                backgroundColor: selectedMapTable?.id === t.id ? '#e6f7ff' : getTableStyle(t.trangThai).backgroundColor
              }}
              bodyStyle={{ padding: '12px' }}
            >
              <Text strong>{t.maBan}</Text>
              <br/>
              <Text type="secondary" style={{ fontSize: '12px' }}>{getStatusText(t.trangThai)}</Text>
              <br/>
              <Text type="secondary" style={{ fontSize: '12px' }}>Sức chứa: {t.soGhe} người</Text>
            </Card>
          ))}
          {filteredTables.length === 0 && (
            <Text type="secondary" style={{ fontStyle: 'italic', padding: 16 }}>Không có bàn nào phù hợp ở khu vực này.</Text>
          )}
        </div>
      </>
    );
  };

  const showCheckoutModal = () => {
    setIsCheckoutModalVisible(true);
    setCheckoutMethod('TIEN_MAT');
    setAmountGiven(0);
  };

  const handleCheckout = async () => {
    setLoadingCheckout(true);
    try {
      await apiClient.post(`/api/v1/pos/thanh-toan/${currentOrder.id}`, { phuongThucTT: checkoutMethod });
      message.success('Thanh toán thành công!');
      setIsCheckoutModalVisible(false);
      setSelectedTable(null);
      setCurrentOrder(null);
      setOrderItems([]);
      fetchTables();
    } catch (error) {
      message.error(error.message || 'Lỗi khi thanh toán');
    } finally {
      setLoadingCheckout(false);
    }
  };

  // --- ACTIONS CHO ĐẶT BÀN (MOCK) ---
  const handleReservation = () => {
    formReservation.validateFields().then(async values => {
      try {
        const thoiGianDenStr = values.thoiGianDen.format('YYYY-MM-DDTHH:mm:ss');
        
        // KIỂM TRA BÀN TRỐNG TRƯỚC
        const checkRes = await apiClient.get(`/api/v1/phieu-dat-ban/check-availability?maBan=${selectedTable.maBan}&thoiGianDen=${thoiGianDenStr}`);
        const isAvailable = checkRes.data?.data;
        
        if (!isAvailable) {
            Modal.confirm({
                title: 'Bàn bận trong khoảng thời gian này!',
                content: `Hệ thống kiểm tra thấy Bàn ${selectedTable.maBan} đã có khách đặt trước đó (hệ thống tự động chặn các đơn đặt cách nhau dưới 2 tiếng). Bạn muốn làm gì tiếp theo?`,
                okText: 'Chọn ngày giờ khác',
                cancelText: 'Đổi bàn khác',
                onOk: () => {
                    // Do nothing, just close confirm modal and let user edit time on the same modal
                },
                onCancel: () => {
                    // Đóng modal Đặt bàn hiện tại để ra ngoài Sơ đồ chọn bàn khác
                    setIsReservationModalVisible(false);
                    // Có thể resetFields nếu muốn form trắng, hoặc giữ nguyên form để user click bàn khác và submit tiếp
                }
            });
            return; // Dừng luồng đặt bàn
        }

        const payload = {
          hoTen: values.hoTen,
          sdt: values.sdt,
          thoiGianDen: thoiGianDenStr,
          soLuongNguoi: values.soLuongNguoi,
          ghiChu: values.ghiChu || '',
          tienDatCoc: values.tienDatCoc || 0,
          danhSachBanId: [selectedTable.id]
        };
        
        await apiClient.post('/api/web/booking', payload);
        message.success(`Đã tạo phiếu đặt bàn thành công cho ${values.hoTen}!`);
        setIsReservationModalVisible(false);
        formReservation.resetFields();
        fetchTables(); // Reload tables
      } catch (error) {
        message.error('Lỗi khi đặt bàn: ' + (error.response?.data?.message || error.message));
      }
    });
  };

  // --- RENDER COLUMNS ---

  const orderColumns = [
    { title: 'Tên món', dataIndex: 'tenMon', key: 'tenMon' },
    { title: 'SL', dataIndex: 'soLuong', key: 'soLuong', width: 60, align: 'center' },
    { title: 'Đơn giá', dataIndex: 'donGia', key: 'donGia', render: (val) => (val || 0).toLocaleString('vi-VN') },
    { title: 'Thành tiền', key: 'thanhTien', render: (_, record) => (record.soLuong * (record.donGia || 0)).toLocaleString('vi-VN') },
  ];

  const menuColumns = [
    {
      title: 'Ảnh',
      dataIndex: 'urlHinhAnh',
      key: 'urlHinhAnh',
      render: (url) => url ? <Image width={40} height={40} src={url} style={{ objectFit: 'cover' }} /> : 'Không có'
    },
    { title: 'Tên món', dataIndex: 'tenMon', key: 'tenMon' },
    { title: 'Giá', dataIndex: 'donGia', key: 'donGia', render: (val) => `${val?.toLocaleString('vi-VN')} đ` },
    {
      title: 'Hành động',
      key: 'action',
      render: (_, record) => (
        <Button 
          type="primary" 
          size="small" 
          icon={<PlusOutlined />} 
          onClick={() => handleAddItemToOrder(record)}
          disabled={record.trangThai === 'HET_HANG'}
        >
          Thêm
        </Button>
      )
    }
  ];

  // Tính tổng tiền an toàn
  const calculateTotal = () => {
    return orderItems.reduce((acc, item) => acc + (item.soLuong * (item.donGia || 0)), 0);
  };

  const filteredTables = tables.filter(t => {
    const matchArea = t.khuVuc === filterArea;
    const matchStatus = filterStatus === 'ALL' || (filterStatus === 'DA_DAT' ? (t.trangThai === 'DA_DAT' || t.trangThai === 'DAT_TRUOC') : t.trangThai === filterStatus);
    return matchArea && matchStatus;
  });

  return (
    <div style={{ padding: '24px', minHeight: '100vh', background: '#f0f2f5' }}>
      <Row gutter={24}>
        {/* Cột Trái: Khu vực Bàn (60% ~ span 14) */}
        <Col span={14}>
          <Card title="Sơ đồ Bàn" loading={loading} style={{ minHeight: '80vh' }}>
            <div style={{ marginBottom: '16px' }}>
              <Radio.Group 
                optionType="button" 
                value={filterStatus} 
                onChange={e => setFilterStatus(e.target.value)}
              >
                <Radio value="ALL">Tất cả</Radio>
                <Radio value="TRONG">Trống</Radio>
                <Radio value="DANG_SUDUNG">Đang phục vụ</Radio>
                <Radio value="DA_DAT">Đã đặt</Radio>
              </Radio.Group>
            </div>
            <Tabs 
              activeKey={filterArea} 
              onChange={setFilterArea} 
              items={[
                { key: 'TANG_TRET', label: 'Tầng trệt' },
                { key: 'LAU_1', label: 'Lầu 1' },
                { key: 'PHONG_VIP', label: 'Phòng VIP' }
              ]} 
              style={{ marginBottom: '24px' }}
            />
            <Row gutter={[16, 16]} align="stretch">
              {filteredTables.length > 0 ? filteredTables.map(table => {
                const styleObj = getTableStyle(table.trangThai);
                const isSelected = selectedTable?.id === table.id;
                return (
                  <Col span={8} key={table.id} style={{ display: 'flex' }}>
                    <Card
                      hoverable
                      onClick={() => handleTableClick(table)}
                      style={{
                        ...styleObj,
                        borderWidth: isSelected ? '2px' : '1px',
                        borderStyle: 'solid',
                        boxShadow: isSelected ? '0 0 10px rgba(0,0,0,0.2)' : 'none',
                        cursor: 'pointer',
                        textAlign: 'center',
                        transition: 'all 0.3s',
                        width: '100%',
                        display: 'flex',
                        flexDirection: 'column',
                        justifyContent: 'center'
                      }}
                      bodyStyle={{ padding: '16px', flex: 1, display: 'flex', flexDirection: 'column', justifyContent: 'center' }}
                    >
                      <Title level={5} style={{ margin: 0, wordBreak: 'break-word' }}>Bàn {table.maBan}</Title>
                      <Text type="secondary">{table.viTri}</Text>
                      <div style={{ marginTop: '8px' }}>
                        <Badge 
                          color={styleObj.borderColor} 
                          text={<Text strong>{getStatusText(table.trangThai)}</Text>} 
                        />
                      </div>
                      <div style={{ marginTop: '4px' }}>
                        <Text type="secondary" style={{ fontSize: '12px' }}>Sức chứa: {table.soGhe} người</Text>
                      </div>
                    </Card>
                  </Col>
                );
              }) : (
                <Col span={24}>
                  <div style={{ textAlign: 'center', padding: '40px 0' }}>
                    <Text type="secondary">Không có bàn nào trong khu vực và trạng thái này.</Text>
                  </div>
                </Col>
              )}
            </Row>
          </Card>
        </Col>

        {/* Cột Phải: Khu vực Order/Bill (40% ~ span 10) */}
        <Col span={10}>
          <Card 
            title="Chi tiết Order" 
            style={{ minHeight: '80vh', display: 'flex', flexDirection: 'column' }}
            bodyStyle={{ flex: 1, display: 'flex', flexDirection: 'column' }}
            extra={
              selectedTable?.trangThai === 'DANG_SUDUNG' && (
                <Button type="primary" icon={<PlusOutlined />} onClick={showMenuModal}>
                  Thêm Món
                </Button>
              )
            }
          >
            {selectedTable ? (
              <div style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
                <div style={{ textAlign: 'center', marginBottom: '16px' }}>
                  <Title level={5} type="success" style={{ margin: 0 }}>Đang thao tác tại: Bàn {selectedTable.maBan}</Title>
                  <Text type="secondary">Sức chứa: {selectedTable.soGhe} người {selectedTable.viTri ? `(${selectedTable.viTri})` : ''}</Text>
                </div>

                {/* Xử lý render UI tùy theo trạng thái của bàn */}
                {selectedTable.trangThai === 'TRONG' ? (
                  <div style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', gap: '16px' }}>
                    <Button 
                      type="primary" 
                      size="large" 
                      icon={<CoffeeOutlined />} 
                      style={{ height: '60px', width: '250px', fontSize: '18px', borderRadius: '8px', backgroundColor: '#52c41a', borderColor: '#52c41a' }}
                      onClick={handleOpenTable}
                    >
                      Phục Vụ Ngay
                    </Button>
                    <Button 
                      type="primary" 
                      size="large" 
                      icon={<CalendarOutlined />} 
                      style={{ height: '60px', width: '250px', fontSize: '18px', borderRadius: '8px' }}
                      onClick={() => setIsReservationModalVisible(true)}
                    >
                      Nhận Đặt Bàn Trước
                    </Button>
                  </div>
                ) : (
                  <div style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
                    <Table 
                      columns={orderColumns} 
                      dataSource={orderItems} 
                      rowKey="id" 
                      size="small" 
                      pagination={false}
                      loading={loadingOrder}
                      scroll={{ y: 'calc(80vh - 250px)' }} 
                      locale={{ emptyText: 'Chưa có order nào' }}
                    />
                    
                    {/* Khu vực Thanh Toán bám đáy */}
                    <Card style={{ marginTop: 'auto', backgroundColor: '#fafafa', borderColor: '#d9d9d9' }} bodyStyle={{ padding: '16px' }}>
                      <Row justify="space-between" align="middle" style={{ marginBottom: '16px' }}>
                        <Text strong style={{ fontSize: '16px' }}>Tổng tiền:</Text>
                        <Text strong type="danger" style={{ fontSize: '20px' }}>
                          {calculateTotal().toLocaleString('vi-VN')} đ
                        </Text>
                      </Row>
                      <Space direction="vertical" style={{ width: '100%' }}>

                        {selectedTable?.trangThai === 'DANG_SUDUNG' && (
                          <Button 
                            type="dashed" 
                            icon={<RetweetOutlined />} 
                            size="large" 
                            block 
                            style={{ color: '#1890ff', borderColor: '#1890ff', marginBottom: '8px' }}
                            onClick={handleOpenTransferMerge}
                          >
                            Đổi / Gộp Bàn
                          </Button>
                        )}

                        <Button 
                          type="primary" 
                          icon={<DollarOutlined />} 
                          size="large" 
                          block 
                          style={{ backgroundColor: '#52c41a', borderColor: '#52c41a', height: '50px', fontSize: '16px' }}
                          disabled={orderItems.length === 0}
                          onClick={showCheckoutModal}
                        >
                          Thanh Toán Hóa Đơn
                        </Button>
                        <Button 
                          type="dashed" 
                          icon={<CalendarOutlined />} 
                          size="large" 
                          block 
                          onClick={() => setIsReservationModalVisible(true)}
                        >
                          Nhận Đặt Bàn Tương Lai
                        </Button>
                      </Space>
                    </Card>
                  </div>
                )}
              </div>
            ) : (
              <div style={{ flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                <Text type="secondary" style={{ fontSize: '16px' }}>
                  Vui lòng chọn một bàn để thao tác
                </Text>
              </div>
            )}
          </Card>
        </Col>
      </Row>

      {/* Modal Danh sách món ăn */}
      <Modal
        title="Thêm Món Ăn"
        open={isMenuModalVisible}
        onCancel={() => setIsMenuModalVisible(false)}
        footer={null}
        width={700}
        destroyOnClose
      >
        <Table 
          columns={menuColumns} 
          dataSource={menuList} 
          rowKey="maMon" 
          loading={loadingMenu}
          pagination={{ pageSize: 5 }}
          size="small"
        />
      </Modal>

      {/* Modal Thanh toán */}
      <Modal
        title="Thanh Toán Hóa Đơn"
        open={isCheckoutModalVisible}
        onCancel={() => setIsCheckoutModalVisible(false)}
        footer={[
          <Button key="back" onClick={() => setIsCheckoutModalVisible(false)}>
            Hủy
          </Button>,
          <Button 
            key="submit" 
            type="primary" 
            loading={loadingCheckout} 
            onClick={handleCheckout}
            disabled={checkoutMethod === 'TIEN_MAT' && amountGiven < calculateTotal()}
            style={{ backgroundColor: '#52c41a', borderColor: '#52c41a' }}
          >
            Xác nhận thanh toán
          </Button>,
        ]}
      >
        <div style={{ textAlign: 'center', marginBottom: '24px' }}>
          <Text style={{ fontSize: '18px' }}>Tổng tiền cần thanh toán</Text>
          <br/>
          <Text strong type="danger" style={{ fontSize: '32px' }}>
            {calculateTotal().toLocaleString('vi-VN')} đ
          </Text>
        </div>

        <div style={{ marginBottom: '16px' }}>
          <Text strong>Phương thức thanh toán:</Text>
          <br/>
          <Radio.Group 
            value={checkoutMethod} 
            onChange={(e) => setCheckoutMethod(e.target.value)} 
            style={{ marginTop: '8px' }}
          >
            <Radio.Button value="TIEN_MAT">Tiền mặt</Radio.Button>
            <Radio.Button value="CHUYEN_KHOAN">Chuyển khoản</Radio.Button>
          </Radio.Group>
        </div>

        {checkoutMethod === 'TIEN_MAT' && (
          <>
            <div style={{ marginBottom: '16px' }}>
              <Text strong>Tiền khách đưa:</Text>
              <br/>
              <InputNumber 
                style={{ width: '100%', marginTop: '8px', fontSize: '18px' }} 
                size="large"
                formatter={value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
                parser={value => value.replace(/\$\s?|(,*)/g, '')}
                value={amountGiven}
                onChange={setAmountGiven}
                min={0}
              />
            </div>
            
            <div style={{ padding: '16px', background: '#f5f5f5', borderRadius: '8px' }}>
              <Row justify="space-between" align="middle">
                <Text strong style={{ fontSize: '16px' }}>Tiền thừa trả khách:</Text>
                <Text strong type="danger" style={{ fontSize: '20px' }}>
                  {Math.max(0, amountGiven - calculateTotal()).toLocaleString('vi-VN')} đ
                </Text>
              </Row>
            </div>
          </>
        )}
      </Modal>

      {/* Modal Đặt Bàn Trước */}
      <Modal
        title={`Nhận Đặt Bàn Trước - Bàn ${selectedTable?.maBan || ''}`}
        open={isReservationModalVisible}
        onCancel={() => setIsReservationModalVisible(false)}
        onOk={handleReservation}
        okText="Xác nhận Đặt Bàn"
        cancelText="Hủy"
        destroyOnClose
      >
        <div style={{ textAlign: 'center', marginBottom: '16px' }}>
          <Text type="secondary" style={{ fontSize: '13px', fontStyle: 'italic' }}>(Lưu ý: Hệ thống sẽ tự động kiểm tra giờ trống của bàn trước khi xác nhận)</Text>
        </div>
        <Form form={formReservation} layout="vertical">
          <Form.Item name="hoTen" label="Tên khách hàng" rules={[{ required: true, message: 'Vui lòng nhập tên khách hàng!' }]}>
            <Input placeholder="Nhập tên khách hàng" />
          </Form.Item>
          <Form.Item name="sdt" label="Số điện thoại" rules={[{ required: true, message: 'Vui lòng nhập số điện thoại!' }]}>
            <Input placeholder="Nhập số điện thoại liên hệ" />
          </Form.Item>
          <Form.Item name="thoiGianDen" label="Thời gian đến" rules={[{ required: true, message: 'Vui lòng chọn thời gian đến!' }]}>
            <DatePicker showTime format="DD/MM/YYYY HH:mm" style={{ width: '100%' }} placeholder="Chọn ngày và giờ" />
          </Form.Item>
          <Form.Item name="soLuongNguoi" label="Số lượng người" rules={[{ required: true, message: 'Vui lòng nhập số lượng người!' }]}>
            <InputNumber min={1} style={{ width: '100%' }} placeholder="Nhập số lượng người" onChange={(val) => {
                const deposit = (val && val >= 5) ? 500000 : 0;
                formReservation.setFieldsValue({ tienDatCoc: deposit });
            }} />
          </Form.Item>
          <Form.Item name="tienDatCoc" label="Tiền đặt cọc (Tự động tính theo số người)">
            <InputNumber 
                style={{ width: '100%' }} 
                readOnly 
                formatter={value => value ? value.toLocaleString('vi-VN') + ' đ' : '0 đ'} 
            />
          </Form.Item>
          <Form.Item name="ghiChu" label="Ghi chú thêm">
            <Input.TextArea rows={3} placeholder="Ví dụ: Cần ghế trẻ em, dị ứng hải sản..." />
          </Form.Item>
        </Form>
      </Modal>

      {/* Modal Đổi / Gộp Bàn */}
      <Modal 
        title={`Chuyển / Gộp Bàn - Đang phục vụ tại ${selectedTable?.maBan || 'N/A'}`} 
        open={isTransferMergeModalVisible} 
        onCancel={() => setIsTransferMergeModalVisible(false)} 
        onOk={submitTransferMerge} 
        okText="Xác Nhận" 
        width={700} 
        destroyOnClose
      >
        <Radio.Group value={transferMergeTab} onChange={e => {
            setTransferMergeTab(e.target.value);
            setSelectedMapTable(null);
          }} style={{ marginBottom: 16 }}>
          <Radio.Button value="CHANGE"><RetweetOutlined /> Đổi sang Bàn trống</Radio.Button>
          <Radio.Button value="MERGE"><MergeCellsOutlined /> Gộp vào Bàn đang phục vụ</Radio.Button>
        </Radio.Group>
        <Card size="small" title="Chọn bàn mục tiêu từ Sơ đồ">
          {transferMergeTab === 'CHANGE' 
            ? renderMiniTableMap(['TRONG']) 
            : renderMiniTableMap(['DANG_SUDUNG', 'DA_DAT'])}
        </Card>
      </Modal>

    </div>
  );
};

export default POSManagement;
