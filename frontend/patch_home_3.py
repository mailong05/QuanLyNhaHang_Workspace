import os
path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/customer/Home.jsx'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

# 1. Add setSelectedTables([]) to handleBooking
old_handle_booking = """      message.success(tienDatCoc > 0 ? 'Đặt bàn và ghi nhận cọc thành công!' : 'Đặt bàn thành công! Hệ thống đang xử lý và chờ xác nhận.');
      form.resetFields();
      setIsQrModalVisible(false);
      setCurrentDepositAmount(0);"""
new_handle_booking = """      message.success(tienDatCoc > 0 ? 'Đặt bàn và ghi nhận cọc thành công!' : 'Đặt bàn thành công! Hệ thống đang xử lý và chờ xác nhận.');
      form.resetFields();
      setIsQrModalVisible(false);
      setCurrentDepositAmount(0);
      setSelectedTables([]); // Reset danh sách bàn đã chọn"""
c = c.replace(old_handle_booking, new_handle_booking)

# 2. Add soLuongNguoi filtering to handleOpenTableMap
old_map = """      const values = await form.validateFields(['ngayDen', 'gioDen']);
      if (!values.ngayDen || !values.gioDen) return;

      const thoiGianDen = values.ngayDen.format('YYYY-MM-DD') + 'T' + values.gioDen.format('HH:mm:ss');
      
      setLoadingTables(true);
      setIsTableMapVisible(true);
      
      const res = await axios.get('http://localhost:8080/api/web/booking/available-tables', {
        params: { thoiGianDen }
      });
      setAvailableTables(res.data.data);"""

new_map = """      const values = await form.validateFields(['ngayDen', 'gioDen', 'soLuongNguoi']);
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
      setAvailableTables(filteredTables);"""
c = c.replace(old_map, new_map)

# 3. Update the error message to include soLuongNguoi
old_error = """message.warning('Vui lòng chọn Ngày đến và Giờ đến trước khi chọn bàn!');"""
new_error = """message.warning('Vui lòng chọn Ngày đến, Giờ đến và Số người trước khi chọn bàn!');"""
c = c.replace(old_error, new_error)

with open(path, 'w', encoding='utf-8') as f:
    f.write(c)
