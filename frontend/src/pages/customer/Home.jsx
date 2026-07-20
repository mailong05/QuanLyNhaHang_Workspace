import React, { useState } from 'react';
import { Typography, Row, Col, Card, Form, Input, DatePicker, TimePicker, InputNumber, Button, message } from 'antd';
import { CalendarOutlined } from '@ant-design/icons';
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

  const onFinish = (values) => {
    const tienDatCoc = values.tienDatCoc || 0;
    
    if (tienDatCoc > 0) {
      setCurrentDepositAmount(tienDatCoc);
      setIsQrModalVisible(true);
    } else {
      setLoading(true);
      setTimeout(() => {
        message.success('Đặt bàn thành công! Chúng tôi sẽ liên hệ lại với bạn sớm nhất.');
        form.resetFields();
        setLoading(false);
      }, 1500);
    }
  };

  const handleQrPaymentSuccess = () => {
    message.success('Đặt bàn và ghi nhận cọc thành công!');
    setIsQrModalVisible(false);
    form.resetFields();
    setCurrentDepositAmount(0);
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
    </div>
  );
};

export default Home;
