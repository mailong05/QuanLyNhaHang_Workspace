import os
path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/BookingManagement.jsx'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

# Add isMapModalVisible state
if "const [isMapModalVisible, setIsMapModalVisible] = useState(false);" not in c:
    c = c.replace(
        "const [isTransferMergeModalVisible, setIsTransferMergeModalVisible] = useState(false);",
        "const [isTransferMergeModalVisible, setIsTransferMergeModalVisible] = useState(false);\n  const [isMapModalVisible, setIsMapModalVisible] = useState(false);"
    )

# Modify handleSaveEditBooking
old_handle_save = """  const handleSaveEditBooking = async (values) => {
    try {
        const payload = {
          ...editingBooking,
          hoTenKH: values.hoTenKH,
          sdtKH: values.sdtKH,
          thoiGianDen: values.thoiGianDen.format('YYYY-MM-DDTHH:mm:ss'),
          soLuongNguoi: values.soLuongNguoi,
          ghiChu: values.ghiChu,
        chiTiets: values.maBan ? [{ maBan: values.maBan, maPhieuDat: editingBooking.maPhieuDat }] : []
      };
      await apiClient.put(`/api/v1/phieu-dat-ban/${editingBooking.maPhieuDat}`, payload);
      message.success('Cập nhật phiếu đặt bàn thành công!');
      setIsEditModalVisible(false);
      fetchBookings();
    } catch (error) {
      message.error('Lỗi cập nhật phiếu');
    }
  };"""

new_handle_save = """  const handleSaveEditBooking = async (values) => {
    try {
      const thoiGianDenStr = values.thoiGianDen.format('YYYY-MM-DDTHH:mm:ss');
      const targetTable = values.maBan || editingBooking?.chiTiets?.[0]?.maBan;

      // Nếu có bàn, phải check availability (loại trừ chính phiếu này)
      if (targetTable) {
        const checkRes = await apiClient.get(`/api/v1/phieu-dat-ban/check-availability?maBan=${targetTable}&thoiGianDen=${thoiGianDenStr}&excludePhieuId=${editingBooking.id}`);
        const isAvailable = checkRes;
        
        if (!isAvailable) {
            Modal.warning({
                title: 'Bàn đã bận!',
                content: `Bàn ${targetTable} đang có khách hoặc đã được đặt trước trong khoảng thời gian này. Vui lòng chọn bàn khác hoặc đổi giờ.`
            });
            return;
        }
      }

      const payload = {
        ...editingBooking,
        hoTenKH: values.hoTenKH,
        sdtKH: values.sdtKH,
        thoiGianDen: thoiGianDenStr,
        soLuongNguoi: values.soLuongNguoi,
        ghiChu: values.ghiChu,
        chiTiets: targetTable ? [{ maBan: targetTable, maPhieuDat: editingBooking.maPhieuDat }] : []
      };
      await apiClient.put(`/api/v1/phieu-dat-ban/${editingBooking.maPhieuDat}`, payload);
      message.success('Cập nhật phiếu đặt bàn thành công!');
      setIsEditModalVisible(false);
      fetchBookings();
    } catch (error) {
      message.error('Lỗi cập nhật phiếu');
    }
  };"""

c = c.replace(old_handle_save, new_handle_save)

# Modify renderMiniTableMap signature to take currentTable
c = c.replace(
    "const renderMiniTableMap = (allowedStatuses) => {",
    "const renderMiniTableMap = (allowedStatuses, currentTable) => {"
).replace(
    "(transferMergeBooking?.chiTiets?.[0]?.maBan !== t.maBan) // Không hiện bàn hiện tại của khách",
    "(currentTable !== t.maBan) // Không hiện bàn hiện tại của khách"
)

c = c.replace("renderMiniTableMap(['TRONG'])", "renderMiniTableMap(['TRONG'], transferMergeBooking?.chiTiets?.[0]?.maBan)")
c = c.replace("renderMiniTableMap(['DANG_SUDUNG', 'DA_DAT'])", "renderMiniTableMap(['DANG_SUDUNG', 'DA_DAT'], transferMergeBooking?.chiTiets?.[0]?.maBan)")

# Modify the Edit Modal form
old_form_maBan = """            {editingBooking?.trangThai === 'DA_XAC_NHAN' && (
              <Form.Item name="maBan" label="Bàn đã xếp (Có thể chọn lại bàn khác)">
                <Select placeholder="-- Chọn Bàn --">
                  {allTables.filter(t => t.trangThai === 'TRONG' || t.maBan === editingBooking?.chiTiets?.[0]?.maBan).map(table => (
                    <Option key={table.id} value={table.maBan}>
                      Bàn {table.maBan} - {table.viTri} (Sức chứa: {table.soGhe})
                    </Option>
                  ))}
                </Select>
              </Form.Item>
            )}"""

new_form_maBan = """            {editingBooking?.trangThai === 'DA_XAC_NHAN' && (
              <Form.Item name="maBan" label="Bàn đã xếp">
                <Space>
                  <Input readOnly value={formEdit.getFieldValue('maBan') || 'Chưa xếp'} style={{ width: 120 }} />
                  <Button type="primary" onClick={() => {
                     setMapFilterArea('Tầng 1');
                     setSelectedMapTable(null);
                     setIsMapModalVisible(true);
                  }}>Đổi Bàn</Button>
                </Space>
              </Form.Item>
            )}"""

c = c.replace(old_form_maBan, new_form_maBan)

# Add the new Map Modal at the end of the return statement
new_map_modal = """
      {/* Modal Chọn Bàn từ Sơ đồ */}
      <Modal title="Chọn Bàn Từ Sơ Đồ" open={isMapModalVisible} onCancel={() => setIsMapModalVisible(false)} onOk={() => {
        if (selectedMapTable) {
            formEdit.setFieldsValue({ maBan: selectedMapTable.maBan });
            setIsMapModalVisible(false);
        } else {
            message.warning('Vui lòng chọn 1 bàn');
        }
      }} okText="Xác nhận chọn bàn" width={700}>
         <Card size="small" title="Chọn bàn mục tiêu từ Sơ đồ">
          {renderMiniTableMap(['TRONG', 'DA_DAT', 'DANG_SUDUNG'], editingBooking?.chiTiets?.[0]?.maBan)}
         </Card>
      </Modal>
    </>
  );
};
"""

c = c.replace("    </>\n  );\n};\n", new_map_modal)

with open(path, 'w', encoding='utf-8') as f: f.write(c)
print('Patched BookingManagement.jsx')
