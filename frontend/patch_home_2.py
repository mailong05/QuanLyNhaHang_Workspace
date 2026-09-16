import os
path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/customer/Home.jsx'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

# 1. Add the table button
button_ui = """            <Form.Item label="Chọn bàn (Tùy chọn)">
              <Button type="default" block onClick={handleOpenTableMap}>
                Mở sơ đồ chọn bàn
              </Button>
              {selectedTables.length > 0 && (
                <div style={{ marginTop: '8px', color: '#1890ff', fontWeight: 'bold' }}>
                  Đã chọn {selectedTables.length} bàn
                </div>
              )}
            </Form.Item>

            <Form.Item name="ghiChu\""""
c = c.replace('<Form.Item name="ghiChu"', button_ui)

# 2. Add the Modal UI at the end
modal_ui = """      {/* Modal Chọn Bàn */}
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
"""
c = c.replace('    </div>\n  );\n};', modal_ui)

with open(path, 'w', encoding='utf-8') as f:
    f.write(c)
