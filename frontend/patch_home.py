import os

path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/customer/Home.jsx'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

import_axios = "import axios from 'axios';\n"
if 'axios' not in c:
    c = import_axios + c

func_new = """  const handleBooking = async (values, tienDatCoc) => {
    setLoading(true);
    try {
      const payload = {
        hoTen: values.hoTen,
        sdt: values.sdt,
        email: values.email || '',
        thoiGianDen: values.ngayDen.format('YYYY-MM-DD') + 'T' + values.gioDen.format('HH:mm:ss'),
        soLuongNguoi: values.soLuongNguoi,
        ghiChu: values.ghiChu || '',
        tienDatCoc: tienDatCoc
      };
      await axios.post('http://localhost:8080/api/web/booking', payload);
      message.success(tienDatCoc > 0 ? 'Đặt bàn và ghi nhận cọc thành công!' : 'Đặt bàn thành công! Hệ thống đang xử lý và chờ xác nhận.');
      form.resetFields();
      setIsQrModalVisible(false);
      setCurrentDepositAmount(0);
    } catch (error) {
      message.error(error.response?.data?.message || 'Lỗi khi đặt bàn, vui lòng thử lại!');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const onFinish = (values) => {
    let tienDatCoc = values.tienDatCoc || 0;
    // Tự động tính cọc nếu trên 5 người (VD: 500k)
    if (values.soLuongNguoi >= 5 && tienDatCoc === 0) {
      tienDatCoc = 500000; 
      message.info('Bàn từ 5 người trở lên yêu cầu đặt cọc 500.000đ.');
    }
    
    if (tienDatCoc > 0) {
      setCurrentDepositAmount(tienDatCoc);
      setIsQrModalVisible(true);
      // Giữ thông tin form để submit sau khi cọc
      form.setFieldsValue({ tienDatCoc: tienDatCoc });
    } else {
      handleBooking(values, 0);
    }
  };

  const handleQrPaymentSuccess = () => {
    handleBooking(form.getFieldsValue(), currentDepositAmount);
  };
"""

start_idx = c.find('  const onFinish = (values) => {')
end_idx = c.find('  };', c.find('  const handleQrPaymentSuccess = () => {')) + 4

if start_idx != -1 and end_idx != -1:
    c = c[:start_idx] + func_new + c[end_idx:]
    with open(path, 'w', encoding='utf-8') as f:
        f.write(c)
    print('Home.jsx patched!')
else:
    print('Could not find functions in Home.jsx')
