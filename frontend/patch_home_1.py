import os

path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/customer/Home.jsx'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

state_injection = """
  // State cho thanh toán QR
  const [isQrModalVisible, setIsQrModalVisible] = useState(false);
  const [currentDepositAmount, setCurrentDepositAmount] = useState(0);

  // State cho chọn bàn
  const [isTableMapVisible, setIsTableMapVisible] = useState(false);
  const [availableTables, setAvailableTables] = useState([]);
  const [loadingTables, setLoadingTables] = useState(false);
  const [selectedTables, setSelectedTables] = useState([]);

  const handleOpenTableMap = async () => {
    try {
      const values = await form.validateFields(['ngayDen', 'gioDen']);
      if (!values.ngayDen || !values.gioDen) return;

      const thoiGianDen = values.ngayDen.format('YYYY-MM-DD') + 'T' + values.gioDen.format('HH:mm:ss');
      
      setLoadingTables(true);
      setIsTableMapVisible(true);
      
      const res = await axios.get('http://localhost:8080/api/web/booking/available-tables', {
        params: { thoiGianDen }
      });
      setAvailableTables(res.data.data);
    } catch (error) {
      if (error.name === 'ValidationError' || error.errorFields) {
        message.warning('Vui lòng chọn Ngày đến và Giờ đến trước khi chọn bàn!');
      } else {
        message.error('Lỗi khi tải danh sách bàn!');
      }
    } finally {
      setLoadingTables(false);
    }
  };

  const handleToggleTable = (table) => {
    if (!table.isAvailable) return;
    
    if (selectedTables.includes(table.id)) {
      setSelectedTables(selectedTables.filter(id => id !== table.id));
    } else {
      setSelectedTables([...selectedTables, table.id]);
    }
  };
"""

c = c.replace("""  // State cho thanh toAn QR
  const [isQrModalVisible, setIsQrModalVisible] = useState(false);
  const [currentDepositAmount, setCurrentDepositAmount] = useState(0);""", state_injection)
c = c.replace("""  // State cho thanh toán QR
  const [isQrModalVisible, setIsQrModalVisible] = useState(false);
  const [currentDepositAmount, setCurrentDepositAmount] = useState(0);""", state_injection)

# Add to payload
old_payload = """        soLuongNguoi: values.soLuongNguoi,
        ghiChu: values.ghiChu || '',
        tienDatCoc: tienDatCoc
      };"""
new_payload = """        soLuongNguoi: values.soLuongNguoi,
        ghiChu: values.ghiChu || '',
        tienDatCoc: tienDatCoc,
        danhSachBanId: selectedTables
      };"""
c = c.replace(old_payload, new_payload)

with open(path, 'w', encoding='utf-8') as f:
    f.write(c)
