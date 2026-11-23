import React, { useState, useEffect, useMemo } from 'react';
import { Layout, Typography, Row, Col, Card, Tabs, Select, Button, Menu as AntMenu, Spin, Empty } from 'antd';
import { FireOutlined, CalendarOutlined, HomeOutlined, CoffeeOutlined } from '@ant-design/icons';
import { useNavigate, useLocation } from 'react-router-dom';
import apiClient from '../../services/apiClient';

const { Header, Content, Footer } = Layout;
const { Title, Text } = Typography;
const { Option } = Select;

const fallbackMenu = [
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
  const [menuList, setMenuList] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchMenu = async () => {
      setLoading(true);
      try {
        const res = await apiClient.get('/api/v1/mon-an?page=0&size=100');
        const content = res?.content || res?.data?.content || [];
        if (content.length > 0) {
          const mapped = content.map((item, index) => ({
            id: item.id || item.maMon,
            name: item.tenMon,
            price: Number(item.donGia) || 0,
            img: item.hinhAnh ? (item.hinhAnh.startsWith('http') ? item.hinhAnh : `http://localhost:8080${item.hinhAnh}`) : fallbackMenu[index % fallbackMenu.length].img,
            category: item.loaiMon || 'MON_CHINH',
            soldCount: 100 + index * 10
          }));
          setMenuList(mapped);
        } else {
          setMenuList(fallbackMenu);
        }
      } catch (err) {
        setMenuList(fallbackMenu);
      } finally {
        setLoading(false);
      }
    };
    fetchMenu();
  }, []);

  const displayedMenu = useMemo(() => {
    let filtered = [...menuList];
    
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
  }, [menuList, activeTab, sortOrder]);

  const menuTabs = [
    { key: 'ALL', label: 'Tất cả' },
    { key: 'KHAI_VI', label: 'Món Khai Vị' },
    { key: 'MON_CHINH', label: 'Món Chính' },
    { key: 'TRANG_MIENG', label: 'Tráng Miệng' },
    { key: 'DO_UONG', label: 'Đồ Uống' },
  ];

  return (
    <div>
      <div style={{ padding: '40px 50px', background: '#f5f5f5' }}>
        <div style={{ textAlign: 'center', marginBottom: '24px' }}>
          <Title level={2}>Thực Đơn Nhà Hàng</Title>
          <Text type="secondary">Thưởng thức tinh hoa ẩm thực được chế biến bởi các đầu bếp hàng đầu</Text>
        </div>

        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px', flexWrap: 'wrap', gap: '16px' }}>
          <Tabs
            activeKey={activeTab}
            onChange={setActiveTab}
            items={menuTabs}
            style={{ marginBottom: 0 }}
          />

          <Select
            value={sortOrder}
            onChange={setSortOrder}
            style={{ width: 180 }}
          >
            <Option value="BEST_SELLER">Bán chạy nhất</Option>
            <Option value="PRICE_ASC">Giá tăng dần</Option>
            <Option value="PRICE_DESC">Giá giảm dần</Option>
          </Select>
        </div>

        {loading ? (
          <div style={{ textAlign: 'center', padding: '60px 0' }}>
            <Spin size="large" tip="Đang nạp thực đơn..." />
          </div>
        ) : displayedMenu.length === 0 ? (
          <Empty description="Không tìm thấy món ăn nào trong danh mục này" style={{ margin: '60px 0' }} />
        ) : (
          <Row gutter={[24, 24]}>
            {displayedMenu.map((item) => (
              <Col xs={24} sm={12} md={8} lg={6} key={item.id}>
                <Card
                  hoverable
                  cover={
                    <div style={{ height: '200px', overflow: 'hidden' }}>
                      <img
                        alt={item.name}
                        src={item.img}
                        style={{ width: '100%', height: '100%', objectFit: 'cover', transition: 'transform 0.3s' }}
                      />
                    </div>
                  }
                  style={{ borderRadius: '12px', overflow: 'hidden', height: '100%', display: 'flex', flexDirection: 'column' }}
                  bodyStyle={{ display: 'flex', flexDirection: 'column', flex: 1, justifyContent: 'space-between' }}
                >
                  <div>
                    <Title level={4} style={{ marginBottom: '8px' }}>{item.name}</Title>
                    <Text strong style={{ color: '#ff4d4f', fontSize: '18px', display: 'block', marginBottom: '8px' }}>
                      {item.price.toLocaleString('vi-VN')} đ
                    </Text>
                  </div>
                  <Button 
                    type="primary" 
                    icon={<CalendarOutlined />} 
                    block 
                    style={{ marginTop: '16px' }}
                    onClick={() => navigate('/#booking')}
                  >
                    Đặt Bàn Thưởng Thức
                  </Button>
                </Card>
              </Col>
            ))}
          </Row>
        )}
      </div>
    </div>
  );
};

export default Menu;
