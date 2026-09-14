import os

path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/BookingManagement.jsx'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

old_code = """  // Render Sơ đồ Bàn Thu Nhỏ
  const renderMiniTableMap = (allowedStatuses, currentTable) => {
    const isGhopBan = transferMergeTab !== 'CHANGE';
    // Tìm khu vực của currentTable
    const currentTableArea = allTables.find(t => t.maBan === currentTable)?.khuVuc;
    const targetArea = isGhopBan ? (currentTableArea || mapFilterArea) : mapFilterArea;

    const filteredTables = allTables.filter(t => 
      t.khuVuc === targetArea && allowedStatuses.includes(t.trangThai) && 
      (currentTable !== t.maBan) // Không hiện bàn hiện tại của khách
    );"""

new_code = """  // Render Sơ đồ Bàn Thu Nhỏ
  const renderMiniTableMap = (allowedStatuses, currentTable) => {
    const isGhepThemBan = transferMergeTab === 'ADD';
    // Tìm khu vực của currentTable
    const currentTableArea = allTables.find(t => t.maBan === currentTable)?.khuVuc;
    const targetArea = isGhepThemBan ? (currentTableArea || mapFilterArea) : mapFilterArea;

    const filteredTables = allTables.filter(t => 
      t.khuVuc === targetArea && allowedStatuses.includes(t.trangThai) && 
      (currentTable !== t.maBan) // Không hiện bàn hiện tại của khách
    );"""

old_tabs = """    return (
      <div style={{ marginTop: 16 }}>
        {!isGhopBan ? (
          <Tabs activeKey={mapFilterArea} onChange={setMapFilterArea} items={[
            { key: 'TANG_TRET', label: 'Tầng trệt' },
            { key: 'LAU_1', label: 'Lầu 1' },
            { key: 'PHONG_VIP', label: 'Phòng VIP' }
          ]} />
        ) : (
          <div style={{ marginBottom: 16, padding: 8, backgroundColor: '#e6f7ff', borderRadius: 4, border: '1px solid #91d5ff' }}>
            <Typography.Text type="secondary">Đang lọc bàn cùng khu vực ({currentTableArea}) để ghép/gộp.</Typography.Text>
          </div>
        )}"""

new_tabs = """    return (
      <div style={{ marginTop: 16 }}>
        {!isGhepThemBan ? (
          <Tabs activeKey={mapFilterArea} onChange={setMapFilterArea} items={[
            { key: 'TANG_TRET', label: 'Tầng trệt' },
            { key: 'LAU_1', label: 'Lầu 1' },
            { key: 'PHONG_VIP', label: 'Phòng VIP' }
          ]} />
        ) : (
          <div style={{ marginBottom: 16, padding: 8, backgroundColor: '#e6f7ff', borderRadius: 4, border: '1px solid #91d5ff' }}>
            <Typography.Text type="secondary">Đang lọc bàn cùng khu vực ({currentTableArea}) để ghép bàn.</Typography.Text>
          </div>
        )}"""

if old_code in c and old_tabs in c:
    c = c.replace(old_code, new_code)
    c = c.replace(old_tabs, new_tabs)
    with open(path, 'w', encoding='utf-8') as f: f.write(c)
    print('Patched BookingManagement MAP')
else:
    print('Not found')
