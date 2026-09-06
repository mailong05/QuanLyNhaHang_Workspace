import os

path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/POSManagement.jsx'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

c = c.replace('DollarOutlined, CoffeeOutlined, CalendarOutlined }', 'DollarOutlined, CoffeeOutlined, CalendarOutlined, RetweetOutlined, MergeCellsOutlined }')

state_injection = """
  // State cho Doi/Gop Ban
  const [isTransferMergeModalVisible, setIsTransferMergeModalVisible] = useState(false);
  const [transferMergeTab, setTransferMergeTab] = useState('CHANGE');
  const [selectedMapTable, setSelectedMapTable] = useState(null);
  const [mapFilterArea, setMapFilterArea] = useState('TANG_TRET');
"""
c = c.replace('const [formReservation] = Form.useForm();', 'const [formReservation] = Form.useForm();\n' + state_injection)

logic_injection = """
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
"""
c = c.replace('  const showCheckoutModal = () => {', logic_injection + '\n  const showCheckoutModal = () => {')

button_injection = """
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
"""
c = c.replace('<Space direction="vertical" style={{ width: \'100%\' }}>', '<Space direction="vertical" style={{ width: \'100%\' }}>\n' + button_injection)

modal_injection = """
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
"""
c = c.replace('      </Modal>\n    </div>\n  );\n};', '      </Modal>\n' + modal_injection + '\n    </div>\n  );\n};')

with open(path, 'w', encoding='utf-8') as f:
    f.write(c)

print("Done")
