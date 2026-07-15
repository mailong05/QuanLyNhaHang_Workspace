import React, { useState, useMemo } from 'react';
import { Layout, Typography, Row, Col, Card, Tabs, Select, Button, Menu as AntMenu } from 'antd';
import { FireOutlined, CalendarOutlined, HomeOutlined, CoffeeOutlined } from '@ant-design/icons';
import { useNavigate, useLocation } from 'react-router-dom';

const { Header, Content, Footer } = Layout;
const { Title, Text } = Typography;
const { Option } = Select;

const mockMenu = [
  { id: 1, name: 'Bò Bít Tết Sốt Tiêu Xanh', price: 250000, img: 'https://images.unsplash.com/photo-1600891964092-4316c288032e?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60', category: 'MON_CHINH', soldCount: 150 },
  { id: 2, name: 'Cá Hồi Áp Chảo Măng Tây', price: 320000, img: 'https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60', category: 'MON_CHINH', soldCount: 200 },
  { id: 3, name: 'Salad Hoàng Gia', price: 120000, img: 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60', category: 'KHAI_VI', soldCount: 300 },
  { id: 4, name: 'Súp Nấm Truffle', price: 180000, img: 'https://images.unsplash.com/photo-1547592180-85f173990554?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60', category: 'KHAI_VI', soldCount: 120 },
  { id: 5, name: 'Gà Quay Mật Ong', price: 210000, img: 'https://images.unsplash.com/photo-1598514982205-f36b96d1e8dd?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60', category: 'MON_CHINH', soldCount: 80 },
  { id: 6, name: 'Panna Cotta Dâu Rừng', price: 85000, img: 'https://images.unsplash.com/photo-1488477181946-6428a0291777?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60', category: 'TRANG_MIENG', soldCount: 450 },
  { id: 7, name: 'Mojito Chanh Bạc Hà', price: 65000, img: 'https://images.unsplash.com/photo-1551024709-8f23befc6f87?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60', category: 'DO_UONG', soldCount: 500 },
];

const Menu = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [activeTab, setActiveTab] = useState('ALL');
  const [sortOrder, setSortOrder] = useState('BEST_SELLER');

  const displayedMenu = useMemo(() => {
    let filtered = [...mockMenu];
    
    if (activeTab !== 'ALL') {
      filtered = filtered.filter(item => item.category === activeTab);
    }

    if (sortOrder === 'PRICE_ASC') {
      filtered.sort((a, b) => a.price - b.price);
    } else if (sortOrder === 'PRICE_DESC') {
      filtered.sort((a, b) => b.price - a.price);
    } else if (sortOrder === 'BEST_SELLER') {
      filtered.sort((a, b) => b.soldCount - a.soldCount);
    }

    return filtered;
  }, [activeTab, sortOrder]);

  const menuTabs = [
    { key: 'ALL', label: 'Tất cả' },
    { key: 'KHAI_VI', label: 'Món Khai Vị' },
    { key: 'MON_CHINH', label: 'Món Chính' },
    { key: 'TRANG_MIENG', label: 'Tráng Miệng' },
    { key: 'DO_UONG', label: 'Đồ Uống' },
  ];

  const headerMenuItems = [
    { key: '/', icon: <HomeOutlined />, label: 'Trang Chủ' },
    { key: '/menu', icon: <CoffeeOutlined />, label: 'Thực Đơn' },
  ];

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Header style={{ background: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '0 50px', boxShadow: '0 2px 8px rgba(0,0,0,0.1)', zIndex: 1 }}>
        <div style={{ display: 'flex', alignItems: 'center', cursor: 'pointer' }} onClick={() => navigate('/')}>
          <FireOutlined style={{ fontSize: '28px', color: '#fa8c16', marginRight: '10px' }} />
          <Title level={3} style={{ margin: 0, color: '#fa8c16' }}>Grand Restaurant</Title>
        </div>
        
        <AntMenu 
          mode="horizontal" 
          selectedKeys={[location.pathname]} 
          items={headerMenuItems}
          onClick={({ key }) => navigate(key)}
          style={{ borderBottom: 'none', flex: 1, justifyContent: 'center', fontSize: '16px' }}
        />

        <div>
          <Button type="primary" size="large" icon={<CalendarOutlined />} onClick={() => navigate('/#booking')}>Đặt Bàn Ngay</Button>
        </div>
      </Header>

      <Content style={{ padding: '40px 50px', background: '#f5f5f5' }}>
        <div style={{ textAlign: 'center', marginBottom: '24px' }}>
          <Title level={2}>Thực Đơn Của Chúng Tôi</Title>
          <Text type="secondary">Khám phá tinh hoa ẩm thực qua từng món ăn</Text>
        </div>

        <Row justify="space-between" align="middle" style={{ marginBottom: '24px', flexWrap: 'wrap', gap: '16px' }}>
          <Col>
            <Tabs 
              activeKey={activeTab} 
              onChange={setActiveTab} 
              items={menuTabs} 
              style={{ marginBottom: 0 }} 
            />
          </Col>
          <Col>
            <Select 
              value={sortOrder} 
              onChange={setSortOrder} 
              style={{ width: 200 }} 
              size="large"
            >
              <Option value="BEST_SELLER">Bán chạy nhất</Option>
              <Option value="PRICE_ASC">Giá từ thấp đến cao</Option>
              <Option value="PRICE_DESC">Giá từ cao xuống thấp</Option>
            </Select>
          </Col>
        </Row>

        <Row gutter={[24, 24]}>
          {displayedMenu.map(item => (
            <Col xs={24} sm={12} md={8} lg={6} key={item.id}>
              <Card
                hoverable
                cover={<img alt={item.name} src={item.img} style={{ height: '200px', objectFit: 'cover' }} />}
                style={{ borderRadius: '12px', overflow: 'hidden' }}
              >
                <Card.Meta 
                  title={<span style={{ fontSize: '16px', whiteSpace: 'normal' }}>{item.name}</span>} 
                  description={
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '8px' }}>
                      <Text type="danger" strong style={{ fontSize: '16px' }}>{item.price.toLocaleString('vi-VN')} đ</Text>
                      <Text type="secondary" style={{ fontSize: '12px' }}>Đã bán {item.soldCount}</Text>
                    </div>
                  } 
                />
              </Card>
            </Col>
          ))}
        </Row>
      </Content>
      <Footer style={{ textAlign: 'center', background: '#001529', color: 'white', padding: '24px 50px' }}>
        Grand Restaurant ©{new Date().getFullYear()} Created by Tech Lead
      </Footer>
    </Layout>
  );
};

export default Menu;
