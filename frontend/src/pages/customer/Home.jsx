import axios from 'axios';
import React, { useState } from 'react';
import { Layout, Typography, Row, Col, Card, Form, Input, DatePicker, TimePicker, InputNumber, Button, message, Modal, Image, Menu as AntMenu } from 'antd';
import { CalendarOutlined, CheckCircleOutlined, FireOutlined, HomeOutlined, CoffeeOutlined } from '@ant-design/icons';
import { useNavigate, useLocation } from 'react-router-dom';

const { Title, Paragraph, Text } = Typography;

const mockMenu = [
  { id: 1, name: 'Bò Bít Tết Sốt Tiêu Xanh', price: 250000, img: 'https://images.unsplash.com/photo-1600891964092-4316c288032e?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60' },
  { id: 2, name: 'Cá Hồi Áp Chảo Măng Tây', price: 320000, img: 'https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60' },
  { id: 3, name: 'Salad Hoàng Gia', price: 120000, img: 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60' },
  { id: 4, name: 'Súp Nấm Truffle', price: 180000, img: 'https://images.unsplash.com/photo-1547592180-85f173990554?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60' },
  { id: 5, name: 'Gà Quay Mật Ong', price: 210000, img: 'https://images.unsplash.com/photo-1598514982205-f36b96d1e8dd?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60' },
  { id: 6, name: 'Panna Cotta Dâu Rừng', price: 85000, img: 'https://images.unsplash.com/photo-1488477181946-6428a0291777?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60' },
];

const Home = () => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();


  // State cho thanh toán QR
  const [isQrModalVisible, setIsQrModalVisible] = useState(false);
  const [currentDepositAmount, setCurrentDepositAmount] = useState(0);

  // State cho chọn bàn
  const [isTableMapVisible, setIsTableMapVisible] = useState(false);
  const [availableTables, setAvailableTables] = useState([]);
  const [loadingTables, setLoadingTables] = useState(false);
  const [selectedTables, setSelectedTables] = useState([]);

  const handleOpenTableMap = async () => {
    try {
      const values = await form.validateFields(['ngayDen', 'gioDen', 'soLuongNguoi']);
      if (!values.ngayDen || !values.gioDen || !values.soLuongNguoi) return;

      const thoiGianDen = values.ngayDen.format('YYYY-MM-DD') + 'T' + values.gioDen.format('HH:mm:ss');
      const soNguoi = values.soLuongNguoi;
      
      setLoadingTables(true);
      setIsTableMapVisible(true);
      
      const res = await axios.get('http://localhost:8080/api/web/booking/available-tables', {
        params: { thoiGianDen }
      });
      // Lọc các bàn có số ghế >= số lượng người khách nhập
      const filteredTables = res.data.data.filter(t => t.soGhe >= soNguoi);
      setAvailableTables(filteredTables);
    } catch (error) {
      if (error.name === 'ValidationError' || error.errorFields) {
        message.warning('Vui lòng chọn Ngày đến, Giờ đến và Số người trước khi chọn bàn!');
      } else {
        message.error('Lỗi khi tải danh sách bàn!');
      }
    } finally {
      setLoadingTables(false);
    }
  };

  const handleToggleTable = (table) => {
    if (!table.isAvailable) return;
    
    if (selectedTables.includes(table.id)) {
      setSelectedTables(selectedTables.filter(id => id !== table.id));
    } else {
      setSelectedTables([...selectedTables, table.id]);
    }
  };


  const handleBooking = async (values, tienDatCoc) => {
    setLoading(true);
    try {
      const payload = {
        hoTen: values.hoTen,
        sdt: values.sdt,
        email: values.email || '',
        thoiGianDen: values.ngayDen.format('YYYY-MM-DD') + 'T' + values.gioDen.format('HH:mm:ss'),
        soLuongNguoi: values.soLuongNguoi,
        ghiChu: values.ghiChu || '',
        tienDatCoc: tienDatCoc,
        danhSachBanId: selectedTables
      };
      await axios.post('http://localhost:8080/api/web/booking', payload);
      message.success(tienDatCoc > 0 ? 'Đặt bàn và ghi nhận cọc thành công!' : 'Đặt bàn thành công! Hệ thống đang xử lý và chờ xác nhận.');
      form.resetFields();
      setIsQrModalVisible(false);
      setCurrentDepositAmount(0);
      setSelectedTables([]); // Reset danh sách bàn đã chọn
    } catch (error) {
      message.error(error.response?.data?.message || 'Lỗi khi đặt bàn, vui lòng thử lại!');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const onFinish = (values) => {
    let tienDatCoc = values.tienDatCoc || 0;
    // Tự động tính cọc nếu trên 5 người (VD: 500k)
    if (values.soLuongNguoi >= 5 && tienDatCoc === 0) {
      tienDatCoc = 500000; 
      message.info('Bàn từ 5 người trở lên yêu cầu đặt cọc 500.000đ.');
    }
    
    if (tienDatCoc > 0) {
      setCurrentDepositAmount(tienDatCoc);
      setIsQrModalVisible(true);
      // Giữ thông tin form để submit sau khi cọc
      form.setFieldsValue({ tienDatCoc: tienDatCoc });
    } else {
      handleBooking(values, 0);
    }
  };

  const handleQrPaymentSuccess = () => {
    handleBooking(form.getFieldsValue(), currentDepositAmount);
  };


  const headerMenuItems = [
    { key: '/', icon: <HomeOutlined />, label: 'Trang Chủ' },
    { key: '/menu', icon: <CoffeeOutlined />, label: 'Thực Đơn' },
  ];

  return (
    <div>
      {/* Hero Section */}
        <div style={{ 
          background: 'linear-gradient(rgba(0,0,0,0.6), rgba(0,0,0,0.6)), url(https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?ixlib=rb-1.2.1&auto=format&fit=crop&w=1920&q=80) center/cover', 
          height: '600px', 
          display: 'flex', 
          flexDirection: 'column', 
          justifyContent: 'center', 
          alignItems: 'center',
          color: 'white',
          textAlign: 'center',
          padding: '0 20px'
        }}>
          <Title style={{ color: 'white', fontSize: '56px', marginBottom: '16px' }}>Hương Vị Thăng Hoa</Title>
          <Paragraph style={{ color: '#e6f7ff', fontSize: '20px', maxWidth: '600px' }}>
            Trải nghiệm tinh hoa ẩm thực trong không gian sang trọng và đẳng cấp nhất thành phố.
          </Paragraph>
          <Button type="primary" size="large" style={{ height: '50px', padding: '0 40px', fontSize: '18px', marginTop: '24px' }} href="#booking">
            Khám phá ngay
          </Button>
        </div>

        {/* Featured Menu */}
        <div style={{ padding: '60px 50px', background: '#f5f5f5' }}>
          <div style={{ textAlign: 'center', marginBottom: '40px' }}>
            <Title level={2}>Thực Đơn Nổi Bật</Title>
            <Text type="secondary">Những món ăn làm nên tên tuổi của Grand Restaurant</Text>
          </div>
          <Row gutter={[24, 24]}>
            {mockMenu.map(item => (
              <Col xs={24} sm={12} md={8} key={item.id}>
                <Card
                  hoverable
                  cover={<img alt={item.name} src={item.img} style={{ height: '250px', objectFit: 'cover' }} />}
                  style={{ borderRadius: '12px', overflow: 'hidden' }}
                >
                  <Card.Meta 
                    title={<span style={{ fontSize: '18px' }}>{item.name}</span>} 
                    description={<Text type="danger" strong style={{ fontSize: '16px' }}>{item.price.toLocaleString('vi-VN')} đ</Text>} 
                  />
                </Card>
              </Col>
            ))}
          </Row>
          <div style={{ textAlign: 'center', marginTop: '32px' }}>
            <Button size="large" onClick={() => navigate('/menu')}>Xem toàn bộ Thực Đơn</Button>
          </div>
        </div>

        {/* Booking Form Section */}
        <div id="booking" style={{ padding: '60px 50px', background: '#fff' }}>
          <Row justify="center">
            <Col xs={24} md={18} lg={14} xl={12}>
              <Card style={{ borderRadius: '16px', boxShadow: '0 10px 30px rgba(0,0,0,0.05)' }}>
                <div style={{ textAlign: 'center', marginBottom: '32px' }}>
                  <Title level={2}>Đặt Bàn Trực Tuyến</Title>
                  <Text type="secondary">Vui lòng điền thông tin bên dưới để giữ chỗ</Text>
                </div>
                <Form
                  form={form}
                  layout="vertical"
                  onFinish={onFinish}
                  size="large"
                >
                  <Row gutter={16}>
                    <Col xs={24} sm={12}>
                      <Form.Item name="hoTen" label="Họ và Tên" rules={[{ required: true, message: 'Vui lòng nhập họ tên!' }]}>
                        <Input placeholder="Nguyễn Văn A" />
                      </Form.Item>
                    </Col>
                    <Col xs={24} sm={12}>
                      <Form.Item name="sdt" label="Số điện thoại" rules={[{ required: true, message: 'Vui lòng nhập số điện thoại!' }]}>
                        <Input placeholder="0901234567" />
                      </Form.Item>
                    </Col>
                  </Row>
                  
                  <Row gutter={16}>
                    <Col xs={24} sm={8}>
                      <Form.Item name="ngayDen" label="Ngày đến" rules={[{ required: true, message: 'Chọn ngày!' }]}>
                        <DatePicker style={{ width: '100%' }} format="DD/MM/YYYY" />
                      </Form.Item>
                    </Col>
                    <Col xs={24} sm={8}>
                      <Form.Item name="gioDen" label="Giờ đến" rules={[{ required: true, message: 'Chọn giờ!' }]}>
                        <TimePicker style={{ width: '100%' }} format="HH:mm" minuteStep={15} />
                      </Form.Item>
                    </Col>
                    <Col xs={24} sm={8}>
                      <Form.Item name="soLuongNguoi" label="Số người" rules={[{ required: true, message: 'Nhập số người!' }]}>
                        <InputNumber min={1} max={50} style={{ width: '100%' }} />
                      </Form.Item>
                    </Col>
                  </Row>

                  <Form.Item name="tienDatCoc" label="Tiền cọc (Tùy chọn - Giữ chỗ chắc chắn hơn)">
                    <InputNumber 
                      min={0} 
                      style={{ width: '100%' }} 
                      placeholder="Nhập số tiền muốn cọc (VNĐ)" 
                      formatter={value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
                      parser={value => value.replace(/\$\s?|(,*)/g, '')}
                    />
                  </Form.Item>

                              <Form.Item label="Chọn bàn (Tùy chọn)">
              <Button type="default" block onClick={handleOpenTableMap}>
                Mở sơ đồ chọn bàn
              </Button>
              {selectedTables.length > 0 && (
                <div style={{ marginTop: '8px', color: '#1890ff', fontWeight: 'bold' }}>
                  Đã chọn {selectedTables.length} bàn
                </div>
              )}
            </Form.Item>

            <Form.Item name="ghiChu" label="Ghi chú thêm (Không bắt buộc)">
                    <Input.TextArea rows={4} placeholder="Ví dụ: Ghế trẻ em, ăn chay, kỷ niệm ngày cưới..." />
                  </Form.Item>

                  <Form.Item style={{ marginBottom: 0 }}>
                    <Button type="primary" htmlType="submit" block loading={loading} style={{ height: '50px', fontSize: '18px' }}>
                      Xác Nhận Đặt Bàn
                    </Button>
                  </Form.Item>
                </Form>
              </Card>
            </Col>
          </Row>
      </div>

      {/* QR Code Payment Modal */}
      <Modal
        title="Thanh Toán Tiền Cọc"
        open={isQrModalVisible}
        onCancel={() => setIsQrModalVisible(false)}
        footer={null}
        destroyOnClose
        centered
        width={400}
      >
        <div style={{ textAlign: 'center', padding: '20px 0' }}>
          <Text style={{ fontSize: '16px' }}>Số tiền cần chuyển:</Text>
          <div style={{ margin: '10px 0 20px 0' }}>
            <Text strong type="danger" style={{ fontSize: '32px' }}>
              {currentDepositAmount.toLocaleString('vi-VN')} VNĐ
            </Text>
          </div>
          
          <div style={{ border: '2px dashed #d9d9d9', padding: '16px', borderRadius: '12px', display: 'inline-block', marginBottom: '24px' }}>
            <Image
              preview={false}
              width={250}
              src={`https://img.vietqr.io/image/MB-0932223012-compact2.png?amount=${currentDepositAmount}&addInfo=DATBAN`}
              alt="QR Code Thanh Toán"
            />
          </div>
          
          <Button 
            type="primary" 
            size="large" 
            block 
            icon={<CheckCircleOutlined />}
            onClick={handleQrPaymentSuccess}
            style={{ height: '50px', fontSize: '16px', backgroundColor: '#52c41a', borderColor: '#52c41a' }}
          >
            Tôi đã chuyển khoản
          </Button>
        </div>
      </Modal>
      {/* Modal Chọn Bàn */}
      <Modal
        title="Sơ đồ chọn bàn"
        open={isTableMapVisible}
        onCancel={() => setIsTableMapVisible(false)}
        footer={[
          <Button key="ok" type="primary" onClick={() => setIsTableMapVisible(false)}>
            Xác nhận ({selectedTables.length} bàn)
          </Button>
        ]}
        width={800}
        centered
      >
        <div style={{ textAlign: 'center', marginBottom: 20 }}>
          <div style={{ display: 'inline-block', width: 20, height: 20, background: '#f5f5f5', border: '1px solid #d9d9d9', marginRight: 8, verticalAlign: 'middle' }}></div> <Text>Trống</Text>
          <div style={{ display: 'inline-block', width: 20, height: 20, background: '#1890ff', marginLeft: 16, marginRight: 8, verticalAlign: 'middle' }}></div> <Text>Đang chọn</Text>
          <div style={{ display: 'inline-block', width: 20, height: 20, background: '#ff4d4f', marginLeft: 16, marginRight: 8, verticalAlign: 'middle' }}></div> <Text>Đã đặt</Text>
        </div>

        {loadingTables ? (
          <div style={{ textAlign: 'center', padding: '40px 0' }}>Đang tải danh sách bàn...</div>
        ) : (
          <div style={{ maxHeight: '400px', overflowY: 'auto', padding: '10px' }}>
            {Array.from(new Set(availableTables.map(t => t.khuVuc))).map(kv => (
              <div key={kv} style={{ marginBottom: 24 }}>
                <Title level={5} style={{ borderBottom: '1px solid #f0f0f0', paddingBottom: 8 }}>{kv || 'Khu vực chung'}</Title>
                <Row gutter={[16, 16]}>
                  {availableTables.filter(t => t.khuVuc === kv).map(table => {
                    const isSelected = selectedTables.includes(table.id);
                    let bgColor = '#f5f5f5'; // Trống
                    let color = 'rgba(0,0,0,0.85)';
                    if (!table.isAvailable) {
                      bgColor = '#ff4d4f'; // Đã đặt
                      color = 'white';
                    } else if (isSelected) {
                      bgColor = '#1890ff'; // Đang chọn
                      color = 'white';
                    }

                    return (
                      <Col xs={12} sm={8} md={6} key={table.id}>
                        <div 
                          onClick={() => handleToggleTable(table)}
                          style={{
                            padding: '16px 8px',
                            background: bgColor,
                            color: color,
                            textAlign: 'center',
                            borderRadius: '8px',
                            cursor: table.isAvailable ? 'pointer' : 'not-allowed',
                            border: isSelected ? '2px solid #0050b3' : '1px solid #d9d9d9',
                            transition: 'all 0.3s',
                            opacity: table.isAvailable ? 1 : 0.6
                          }}
                        >
                          <div style={{ fontWeight: 'bold', fontSize: '16px' }}>{table.maBan}</div>
                          <div style={{ fontSize: '12px' }}>{table.soGhe} ghế</div>
                        </div>
                      </Col>
                    );
                  })}
                </Row>
              </div>
            ))}
            {availableTables.length === 0 && <div style={{ textAlign: 'center' }}>Không tìm thấy bàn trống nào.</div>}
          </div>
        )}
      </Modal>
    </div>
  );
};


export default Home;
