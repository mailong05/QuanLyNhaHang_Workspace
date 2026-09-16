import os
import re

path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/customer/Home.jsx'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

# 1. Add state for tables
state_patch = """  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [availableTables, setAvailableTables] = useState([]);
  const [selectedTables, setSelectedTables] = useState([]);
  const [isFetchingTables, setIsFetchingTables] = useState(false);"""
c = c.replace("  const [form] = Form.useForm();\n  const [loading, setLoading] = useState(false);", state_patch)

# 2. Add handleValuesChange
values_change = """
  const handleValuesChange = async (changedValues, allValues) => {
    if (allValues.ngayDen && allValues.gioDen) {
      const thoiGianDen = allValues.ngayDen.format('YYYY-MM-DD') + 'T' + allValues.gioDen.format('HH:mm:ss');
      setIsFetchingTables(true);
      try {
        const res = await axios.get(`http://localhost:8080/api/web/booking/available-tables?thoiGianDen=${thoiGianDen}`);
        setAvailableTables(res.data.data);
      } catch (e) {
        console.error(e);
      } finally {
        setIsFetchingTables(false);
      }
    }
  };

  const toggleTableSelection = (tableInfo) => {
    if (!tableInfo.isAvailable) return;
    if (selectedTables.includes(tableInfo.id)) {
      setSelectedTables(selectedTables.filter(id => id !== tableInfo.id));
    } else {
      setSelectedTables([...selectedTables, tableInfo.id]);
    }
  };
"""
c = c.replace("const handleBooking = async", values_change + "\n  const handleBooking = async")

# 3. Add to Form onValuesChange
c = c.replace("<Form form={form} layout=\"vertical\" onFinish={handleBookingForm}>", "<Form form={form} layout=\"vertical\" onFinish={handleBookingForm} onValuesChange={handleValuesChange}>")

# 4. Modify handleBooking payload to include danhSachBanId
c = c.replace("ghiChu: values.ghiChu || '',", "ghiChu: values.ghiChu || '',\n          danhSachBanId: selectedTables,")

# 5. Clear table selection on success
c = c.replace("form.resetFields();", "form.resetFields();\n        setSelectedTables([]);\n        setAvailableTables([]);")

# 6. Render table map
map_ui = """
                    <Col xs={24}>
                      <Form.Item label="Chọn Bàn (Không bắt buộc)">
                        {!form.getFieldValue('ngayDen') || !form.getFieldValue('gioDen') ? (
                          <div style={{ textAlign: 'center', padding: '20px', background: '#f5f5f5', borderRadius: '8px' }}>
                            <Typography.Text type="secondary">Vui lòng chọn Ngày và Giờ đến để xem sơ đồ bàn.</Typography.Text>
                          </div>
                        ) : isFetchingTables ? (
                          <div style={{ textAlign: 'center', padding: '20px' }}>Đang tải sơ đồ bàn...</div>
                        ) : (
                          <div style={{ padding: '16px', background: '#f5f5f5', borderRadius: '8px' }}>
                            <div style={{ display: 'flex', gap: '16px', marginBottom: '16px', justifyContent: 'center' }}>
                              <div><div style={{ display: 'inline-block', width: 16, height: 16, background: '#fff', border: '1px solid #d9d9d9', marginRight: 8, verticalAlign: 'middle' }}></div>Trống</div>
                              <div><div style={{ display: 'inline-block', width: 16, height: 16, background: '#1890ff', marginRight: 8, verticalAlign: 'middle' }}></div>Đang chọn</div>
                              <div><div style={{ display: 'inline-block', width: 16, height: 16, background: '#d9d9d9', marginRight: 8, verticalAlign: 'middle' }}></div>Đã đặt</div>
                            </div>
                            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '12px', justifyContent: 'center' }}>
                              {availableTables.map(table => (
                                <div 
                                  key={table.id}
                                  onClick={() => toggleTableSelection(table)}
                                  style={{
                                    width: '80px', height: '80px', 
                                    borderRadius: '8px',
                                    display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
                                    cursor: table.isAvailable ? 'pointer' : 'not-allowed',
                                    border: selectedTables.includes(table.id) ? '2px solid #096dd9' : '1px solid #d9d9d9',
                                    background: !table.isAvailable ? '#e8e8e8' : selectedTables.includes(table.id) ? '#e6f7ff' : '#fff',
                                    boxShadow: selectedTables.includes(table.id) ? '0 4px 12px rgba(24,144,255,0.2)' : 'none',
                                    transition: 'all 0.3s'
                                  }}
                                >
                                  <Typography.Text strong style={{ color: !table.isAvailable ? '#999' : selectedTables.includes(table.id) ? '#1890ff' : '#333' }}>
                                    {table.maBan}
                                  </Typography.Text>
                                  <Typography.Text style={{ fontSize: '12px', color: !table.isAvailable ? '#999' : '#666' }}>
                                    {table.soGhe} ghế
                                  </Typography.Text>
                                </div>
                              ))}
                            </div>
                          </div>
                        )}
                      </Form.Item>
                    </Col>
"""

c = c.replace("</Row>\n                  <Form.Item name=\"ghiChu\"", map_ui + "</Row>\n                  <Form.Item name=\"ghiChu\"")

with open(path, 'w', encoding='utf-8') as f: f.write(c)
