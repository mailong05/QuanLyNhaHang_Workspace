import os
path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/BookingManagement.jsx'
with open(path, 'r', encoding='utf-8') as f: c = f.read()

# Replace displayBookings logic
old_logic = """  const filteredBookings = bookings.filter(b => b.trangThai === activeTab);
  const completedAndMergedBookings = bookings.filter(b => (activeTab === 'HOAN_THANH' && (b.trangThai === 'HOAN_THANH' || b.trangThai === 'DA_GOP_BAN')));
  const displayBookings = activeTab === 'HOAN_THANH' ? completedAndMergedBookings : filteredBookings;"""

new_logic = """  const displayBookings = activeTab === 'HOAN_THANH' 
    ? bookings.filter(b => b.trangThai === 'HOAN_THANH' || b.trangThai === 'HOAN_TAT') 
    : bookings.filter(b => b.trangThai === activeTab);"""

c = c.replace(old_logic, new_logic)

# Replace Tabs
old_tabs = """        <Tabs activeKey={activeTab} onChange={(key) => setActiveTab(key)}>
          <TabPane tab="Chờ xác nhận" key="CHO_XAC_NHAN" />
          <TabPane tab="Đã xác nhận" key="DA_XAC_NHAN" />
          <TabPane tab="Đang phục vụ" key="DANG_PHUC_VU" />
          <TabPane tab="Hoàn thành & Gộp" key="HOAN_THANH" />
          <TabPane tab="Đã hủy" key="DA_HUY" />
        </Tabs>"""

new_tabs = """        <Tabs activeKey={activeTab} onChange={(key) => setActiveTab(key)}>
          <TabPane tab="Chờ xác nhận" key="CHO_XAC_NHAN" />
          <TabPane tab="Đã xác nhận" key="DA_XAC_NHAN" />
          <TabPane tab="Đang phục vụ" key="DANG_PHUC_VU" />
          <TabPane tab="Đã hoàn thành" key="HOAN_THANH" />
          <TabPane tab="Đã gộp bàn" key="DA_GOP_BAN" />
          <TabPane tab="Đã hủy" key="DA_HUY" />
        </Tabs>"""

c = c.replace(old_tabs, new_tabs)

with open(path, 'w', encoding='utf-8') as f: f.write(c)
print('Patched Booking Tabs')
