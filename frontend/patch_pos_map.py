import os

path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/POSManagement.jsx'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

old_code = """  const renderMiniTableMap = (allowedStatuses) => {
    const filteredTables = tables.filter(t => 
      t.khuVuc === mapFilterArea && allowedStatuses.includes(t.trangThai) && 
      (selectedTable?.maBan !== t.maBan)
    );"""

new_code = """  const renderMiniTableMap = (allowedStatuses) => {
    const isGhopBan = transferMergeTab !== 'CHANGE';
    const targetArea = isGhopBan ? (selectedTable?.khuVuc || mapFilterArea) : mapFilterArea;
    
    const filteredTables = tables.filter(t => 
      t.khuVuc === targetArea && allowedStatuses.includes(t.trangThai) && 
      (selectedTable?.maBan !== t.maBan)
    );"""

old_tabs = """    return (
      <div style={{ marginTop: 16 }}>
        <Tabs activeKey={mapFilterArea} onChange={setMapFilterArea} items={[
          { key: 'TANG_TRET', label: 'Tầng trệt' },
          { key: 'LAU_1', label: 'Lầu 1' },
          { key: 'PHONG_VIP', label: 'Phòng VIP' }
        ]} />"""

new_tabs = """    return (
      <div style={{ marginTop: 16 }}>
        {!isGhopBan ? (
          <Tabs activeKey={mapFilterArea} onChange={setMapFilterArea} items={[
            { key: 'TANG_TRET', label: 'Tầng trệt' },
            { key: 'LAU_1', label: 'Lầu 1' },
            { key: 'PHONG_VIP', label: 'Phòng VIP' }
          ]} />
        ) : (
          <div style={{ marginBottom: 16, padding: 8, backgroundColor: '#f0f2f5', borderRadius: 4 }}>
            <Text type="secondary">Chỉ hiển thị các bàn trong cùng khu vực để ghép/gộp.</Text>
          </div>
        )}"""

if old_code in c and old_tabs in c:
    c = c.replace(old_code, new_code)
    c = c.replace(old_tabs, new_tabs)
    with open(path, 'w', encoding='utf-8') as f: f.write(c)
    print('Patched POSManagement MAP')
else:
    print('Not found')
