import os

path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/POSManagement.jsx'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

old_submit = """  const submitTransferMerge = () => {
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
  };"""

new_submit = """  const submitTransferMerge = async () => {
    if (!selectedMapTable) {
      message.error('Vui lòng chọn một bàn từ sơ đồ!');
      return;
    }

    if (!currentOrder || !currentOrder.maPhieuDat) {
      message.error('Không tìm thấy thông tin Phiếu đặt bàn của Hóa đơn này!');
      return;
    }

    try {
      // 1. Fetch the full booking info first
      const phieuRes = await apiClient.get(`/api/v1/phieu-dat-ban/${currentOrder.maPhieuDat}`);
      // Interceptor returns response.data.data directly, so phieuRes is the DTO
      const booking = phieuRes;

      if (transferMergeTab === 'CHANGE') {
        const payload = {
          ...booking,
          chiTiets: [{ maBan: selectedMapTable.maBan, maPhieuDat: booking.maPhieuDat }]
        };
        await apiClient.put(`/api/v1/phieu-dat-ban/${booking.maPhieuDat}`, payload);
        message.success(`Đổi bàn thành công sang Bàn ${selectedMapTable.maBan}!`);
      } else if (transferMergeTab === 'ADD') {
        const oldTables = booking.chiTiets ? booking.chiTiets.map(ct => ({ maBan: ct.maBan, maPhieuDat: booking.maPhieuDat })) : [];
        oldTables.push({ maBan: selectedMapTable.maBan, maPhieuDat: booking.maPhieuDat });
        
        const payload = {
          ...booking,
          chiTiets: oldTables
        };
        await apiClient.put(`/api/v1/phieu-dat-ban/${booking.maPhieuDat}`, payload);
        message.success(`Ghép bàn thành công! Đã thêm Bàn ${selectedMapTable.maBan} vào phiếu.`);
      } else {
        message.warning('Tính năng Gộp Hóa Đơn đang chờ Backend hoàn thiện API (/api/v1/hoa-don/merge)!');
        return;
      }
      setIsTransferMergeModalVisible(false);
      fetchTables();
      setSelectedTable(null);
      setCurrentOrder(null);
      setOrderItems([]);
    } catch (error) {
      message.error('Có lỗi xảy ra khi dời bàn!');
    }
  };"""

c = c.replace(old_submit, new_submit)

old_radio = """        <Radio.Group value={transferMergeTab} onChange={e => {
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
            ? renderMiniTableMap(['TRONG']) 
            : renderMiniTableMap(['DANG_SUDUNG', 'DA_DAT'])}
        </Card>"""

c = c.replace(old_radio, new_radio)

if "PlusOutlined" not in c.split('\n')[2]:
    c = c.replace("PrinterOutlined", "PrinterOutlined, PlusOutlined")

with open(path, 'w', encoding='utf-8') as f: f.write(c)
print('Patched POSManagement.jsx')
