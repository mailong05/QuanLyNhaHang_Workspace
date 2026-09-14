import os

path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/BookingManagement.jsx'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

# 1. Update submitTransferMerge
old_submit = """  const submitTransferMerge = async () => {
    if (!selectedMapTable) {
      message.error('Vui lòng chọn một bàn từ sơ đồ!');
      return;
    }

    const { soLuongNguoi } = transferMergeBooking;
    if (selectedMapTable.soGhe < soLuongNguoi) {
      message.warning('Lưu ý: Bàn được chọn có sức chứa nhỏ hơn số lượng khách!');
    }

    try {
      if (transferMergeTab === 'CHANGE') {
        // Gọi API Đổi Bàn giả định
        message.success(`Đổi bàn thành công sang Bàn ${selectedMapTable.maBan}!`);
      } else {
        // Gộp Bàn logic
        // 1. Chạy thuật toán Deep Merge (Mock)
        const mockOrderA = [{ maMon: 'M01', soLuong: 2, donGia: 50000, thanhTien: 100000 }];
        const mockOrderC = [{ maMon: 'M01', soLuong: 1, donGia: 50000, thanhTien: 50000 }, { maMon: 'M02', soLuong: 1, donGia: 30000, thanhTien: 30000 }];
        const mergedResult = mockDeepMergeOrders(mockOrderA, mockOrderC);
        console.log('Thuật toán Deep Merge Result:', mergedResult);
        
        // 2. Cập nhật trạng thái phiếu nguồn thành DA_GOP_BAN
        const payload = { ...transferMergeBooking, trangThai: 'DA_GOP_BAN' };
        await apiClient.put(`/api/v1/phieu-dat-ban/${transferMergeBooking.maPhieuDat}`, payload);
        
        message.success(`Gộp bàn thành công vào Bàn ${selectedMapTable.maBan}!`);
      }
      setIsTransferMergeModalVisible(false);
      fetchBookings();
    } catch (error) {
      message.error('Có lỗi xảy ra khi xử lý!');
    }
  };"""

new_submit = """  const submitTransferMerge = async () => {
    if (!selectedMapTable) {
      message.error('Vui lòng chọn một bàn từ sơ đồ!');
      return;
    }

    const { soLuongNguoi } = transferMergeBooking;
    if (transferMergeTab !== 'ADD' && selectedMapTable.soGhe < soLuongNguoi) {
      message.warning('Lưu ý: Bàn được chọn có sức chứa nhỏ hơn số lượng khách!');
    }

    try {
      if (transferMergeTab === 'CHANGE') {
        // ĐỔI BÀN SANG BÀN TRỐNG
        const payload = {
          ...transferMergeBooking,
          chiTiets: [{ maBan: selectedMapTable.maBan, maPhieuDat: transferMergeBooking.maPhieuDat }]
        };
        await apiClient.put(`/api/v1/phieu-dat-ban/${transferMergeBooking.maPhieuDat}`, payload);
        message.success(`Đổi bàn thành công sang Bàn ${selectedMapTable.maBan}!`);
      } else if (transferMergeTab === 'ADD') {
        // GHÉP THÊM BÀN TRỐNG
        const oldTables = transferMergeBooking.chiTiets ? transferMergeBooking.chiTiets.map(ct => ({ maBan: ct.maBan, maPhieuDat: transferMergeBooking.maPhieuDat })) : [];
        oldTables.push({ maBan: selectedMapTable.maBan, maPhieuDat: transferMergeBooking.maPhieuDat });
        
        const payload = {
          ...transferMergeBooking,
          chiTiets: oldTables
        };
        await apiClient.put(`/api/v1/phieu-dat-ban/${transferMergeBooking.maPhieuDat}`, payload);
        message.success(`Ghép bàn thành công! Đã thêm Bàn ${selectedMapTable.maBan} vào phiếu.`);
      } else {
        // GỘP HÓA ĐƠN
        message.warning('Tính năng Gộp Hóa Đơn đang chờ Backend hoàn thiện API (/api/v1/hoa-don/merge)!');
        return;
      }
      setIsTransferMergeModalVisible(false);
      fetchBookings();
    } catch (error) {
      message.error('Có lỗi xảy ra khi xử lý!');
    }
  };"""

c = c.replace(old_submit, new_submit)

# 2. Update Radio.Group
old_radio = """        <Radio.Group value={transferMergeTab} onChange={e => {
            setTransferMergeTab(e.target.value);
            setSelectedMapTable(null);
          }} style={{ marginBottom: 16 }}>
          <Radio.Button value="CHANGE"><RetweetOutlined /> Đổi sang Bàn trống</Radio.Button>
          <Radio.Button value="MERGE"><MergeCellsOutlined /> Gộp vào Bàn đang phục vụ</Radio.Button>
        </Radio.Group>
        <Card size="small" title="Chọn bàn mục tiêu từ Sơ đồ">
          {transferMergeTab === 'CHANGE' 
            ? renderMiniTableMap(['TRONG'], transferMergeBooking?.chiTiets?.[0]?.maBan) 
            : renderMiniTableMap(['DANG_SUDUNG', 'DA_DAT'], transferMergeBooking?.chiTiets?.[0]?.maBan)}
        </Card>"""

new_radio = """        <Radio.Group value={transferMergeTab} onChange={e => {
            setTransferMergeTab(e.target.value);
            setSelectedMapTable(null);
          }} style={{ marginBottom: 16 }}>
          <Radio.Button value="CHANGE"><RetweetOutlined /> Đổi Bàn</Radio.Button>
          <Radio.Button value="ADD"><PlusOutlined /> Ghép Thêm Bàn</Radio.Button>
          <Radio.Button value="MERGE"><MergeCellsOutlined /> Gộp Hóa Đơn</Radio.Button>
        </Radio.Group>
        <Card size="small" title="Chọn bàn mục tiêu từ Sơ đồ">
          {transferMergeTab === 'CHANGE' || transferMergeTab === 'ADD'
            ? renderMiniTableMap(['TRONG'], transferMergeBooking?.chiTiets?.[0]?.maBan) 
            : renderMiniTableMap(['DANG_SUDUNG', 'DA_DAT'], transferMergeBooking?.chiTiets?.[0]?.maBan)}
        </Card>"""

c = c.replace(old_radio, new_radio)

# 3. Add PlusOutlined to imports
if "PlusOutlined" not in c.split('\n')[2]:
    c = c.replace("EditOutlined, RetweetOutlined, MergeCellsOutlined", "EditOutlined, RetweetOutlined, MergeCellsOutlined, PlusOutlined")

with open(path, 'w', encoding='utf-8') as f: f.write(c)
print('Patched BookingManagement.jsx')
