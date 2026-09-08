import os

path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/POSManagement.jsx'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

handle_old = """  const handleReservation = () => {
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

handle_new = """  const handleReservation = () => {
    formReservation.validateFields().then(async values => {
      try {
        const thoiGianDenStr = values.thoiGianDen.format('YYYY-MM-DDTHH:mm:ss');
        
        // KIỂM TRA BÀN TRỐNG TRƯỚC
        const checkRes = await apiClient.get(`/api/v1/phieu-dat-ban/check-availability?maBan=${selectedTable.maBan}&thoiGianDen=${thoiGianDenStr}`);
        const isAvailable = checkRes.data?.data;
        
        if (!isAvailable) {
            Modal.confirm({
                title: 'Bàn bận trong khoảng thời gian này!',
                content: `Hệ thống kiểm tra thấy Bàn ${selectedTable.maBan} đã có khách đặt trước đó (hệ thống tự động chặn các đơn đặt cách nhau dưới 2 tiếng). Bạn muốn làm gì tiếp theo?`,
                okText: 'Chọn ngày giờ khác',
                cancelText: 'Đổi bàn khác',
                onOk: () => {
                    // Do nothing, just close confirm modal and let user edit time on the same modal
                },
                onCancel: () => {
                    // Đóng modal Đặt bàn hiện tại để ra ngoài Sơ đồ chọn bàn khác
                    setIsReservationModalVisible(false);
                    // Có thể resetFields nếu muốn form trắng, hoặc giữ nguyên form để user click bàn khác và submit tiếp
                }
            });
            return; // Dừng luồng đặt bàn
        }

        const payload = {
          hoTen: values.hoTen,
          sdt: values.sdt,
          thoiGianDen: thoiGianDenStr,
          soLuongNguoi: values.soLuongNguoi,
          ghiChu: values.ghiChu || '',
          tienDatCoc: values.tienDatCoc || 0,
          danhSachBanId: [selectedTable.id]
        };
        
        await apiClient.post('/api/web/booking', payload);
        message.success(`Đã tạo phiếu đặt bàn thành công cho ${values.hoTen}!`);
        setIsReservationModalVisible(false);
        formReservation.resetFields();
        fetchTables(); // Reload tables
      } catch (error) {
        message.error('Lỗi khi đặt bàn: ' + (error.response?.data?.message || error.message));
      }
    });
  };"""

if handle_old in c:
    c = c.replace(handle_old, handle_new)
    with open(path, 'w', encoding='utf-8') as f:
        f.write(c)
    print('Patched POSManagement.jsx with availability check')
else:
    print('Target not found in POSManagement.jsx')
