import os

path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/POSManagement.jsx'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

form_item_old = """          <Form.Item name="soLuongNguoi" label="Số lượng người" rules={[{ required: true, message: 'Vui lòng nhập số lượng người!' }]}>
            <InputNumber min={1} style={{ width: '100%' }} placeholder="Nhập số lượng người" />
          </Form.Item>"""
          
form_item_new = """          <Form.Item name="soLuongNguoi" label="Số lượng người" rules={[{ required: true, message: 'Vui lòng nhập số lượng người!' }]}>
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
          </Form.Item>"""

c = c.replace(form_item_old, form_item_new)

handle_old = """  const handleReservation = () => {
    formReservation.validateFields().then(values => {
      message.success(`Đã nhận đặt bàn thành công cho khách ${values.hoTen}!`);
      setIsReservationModalVisible(false);
      formReservation.resetFields();
      
      // Đổi trạng thái table thành DAT_TRUOC (Mock UI)
      setTables(prevTables => prevTables.map(t => {
        if (t.id === selectedTable.id) {
          const updatedTable = { ...t, trangThai: 'DAT_TRUOC' };
          setSelectedTable(updatedTable);
          return updatedTable;
        }
        return t;
      }));
    });
  };"""

handle_new = """  const handleReservation = () => {
    formReservation.validateFields().then(async values => {
      try {
        const payload = {
          hoTen: values.hoTen,
          sdt: values.sdt,
          thoiGianDen: values.thoiGianDen.format('YYYY-MM-DD') + 'T' + values.thoiGianDen.format('HH:mm:ss'),
          soLuongNguoi: values.soLuongNguoi,
          ghiChu: values.ghiChu || '',
          tienDatCoc: values.tienDatCoc || 0,
          danhSachBanId: [selectedTable.id]
        };
        
        await apiClient.post('/api/web/booking', payload);
        message.success(`Đã tạo phiếu đặt bàn thành công cho ${values.hoTen}! (Trạng thái: Chờ xác nhận)`);
        setIsReservationModalVisible(false);
        formReservation.resetFields();
        fetchTables(); // Reload tables
      } catch (error) {
        message.error('Lỗi khi đặt bàn: ' + (error.response?.data?.message || error.message));
      }
    });
  };"""

c = c.replace(handle_old, handle_new)

with open(path, 'w', encoding='utf-8') as f:
    f.write(c)
print('Patched POSManagement.jsx')
