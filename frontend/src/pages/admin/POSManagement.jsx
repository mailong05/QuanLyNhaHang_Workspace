import React, { useState, useEffect } from 'react';
import { Row, Col, Card, message, Typography, Badge } from 'antd';
import apiClient from '../../services/apiClient';
import './POSManagement.css'; // Optional: for extra hover effects if needed

const { Title, Text } = Typography;

const POSManagement = () => {
  const [tables, setTables] = useState([]);
  const [loading, setLoading] = useState(false);
  const [selectedTable, setSelectedTable] = useState(null);

  const fetchTables = async () => {
    setLoading(true);
    try {
      const data = await apiClient.get('/api/v1/ban-an');
      const tableList = data.content ? data.content : data;
      setTables(tableList);
    } catch (error) {
      message.error(error.message || 'Lỗi khi tải danh sách bàn');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTables();
  }, []);

  // Hàm xác định màu sắc dựa vào trạng thái
  const getTableStyle = (status) => {
    switch (status) {
      case 'TRONG':
        return { borderColor: '#52c41a', backgroundColor: '#f6ffed' }; // Xanh lá
      case 'DANG_SUDUNG':
        return { borderColor: '#ff4d4f', backgroundColor: '#fff1f0' }; // Đỏ
      case 'DA_DAT':
        return { borderColor: '#faad14', backgroundColor: '#fffbe6' }; // Vàng
      case 'DAT_TRUOC':
        return { borderColor: '#1890ff', backgroundColor: '#e6f7ff' }; // Xanh dương
      default:
        return { borderColor: '#d9d9d9', backgroundColor: '#ffffff' }; // Mặc định
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

  return (
    <div style={{ padding: '24px', minHeight: '100vh', background: '#f0f2f5' }}>
      <Row gutter={24}>
        {/* Cột Trái: Khu vực Bàn (60% ~ span 14) */}
        <Col span={14}>
          <Card title="Sơ đồ Bàn" loading={loading} style={{ minHeight: '80vh' }}>
            <Row gutter={[16, 16]} align="stretch">
              {tables.map(table => {
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
              })}
            </Row>
          </Card>
        </Col>

        {/* Cột Phải: Khu vực Order/Bill (40% ~ span 10) */}
        <Col span={10}>
          <Card 
            title="Chi tiết Order" 
            style={{ minHeight: '80vh' }}
          >
            {selectedTable ? (
              <div style={{ textAlign: 'center', marginTop: '20px' }}>
                <Title level={5} type="success">Đang thao tác tại: Bàn {selectedTable.maBan}</Title>
                <Text type="secondary">({selectedTable.viTri})</Text>
                {/* Khu vực chừa sẵn cho Component Order Detail */}
                <div style={{ marginTop: '40px', padding: '20px', border: '1px dashed #d9d9d9', borderRadius: '8px' }}>
                  <Text type="secondary">Khu vực hiển thị danh sách món ăn sẽ được phát triển ở Phase 6.2</Text>
                </div>
              </div>
            ) : (
              <div style={{ textAlign: 'center', marginTop: '50px' }}>
                <Text type="secondary" style={{ fontSize: '16px' }}>
                  Vui lòng chọn một bàn để thao tác
                </Text>
              </div>
            )}
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default POSManagement;
