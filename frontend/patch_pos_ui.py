import os
import re

path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/POSManagement.jsx'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

# 1. Add DeleteOutlined
if "DeleteOutlined" not in c:
    c = c.replace("import { RetweetOutlined, DollarOutlined, CalendarOutlined, PlusOutlined, CoffeeOutlined } from '@ant-design/icons';", "import { RetweetOutlined, DollarOutlined, CalendarOutlined, PlusOutlined, CoffeeOutlined, DeleteOutlined } from '@ant-design/icons';")

# 2. handleRemoveItem function
handle_remove = """
  const handleRemoveItem = async (chiTietId) => {
    if (!currentOrder) return;
    setLoadingOrder(true);
    try {
      const res = await apiClient.delete(`/api/v1/pos/hoa-don/${currentOrder.id}/mon/${chiTietId}`);
      setCurrentOrder(res);
      setOrderItems(res.chiTiets || []);
      message.success('Đã bỏ món khỏi order!');
    } catch (error) {
      console.error(error);
      message.error('Lỗi khi bỏ món!');
    } finally {
      setLoadingOrder(false);
    }
  };
"""
if "const handleRemoveItem" not in c:
    c = c.replace("const handleAddItemToOrder", handle_remove + "\n  const handleAddItemToOrder")

# 3. Add column to orderColumns
old_columns = """  const orderColumns = [
    { title: 'Tên món', dataIndex: 'tenMon', key: 'tenMon' },
    { title: 'SL', dataIndex: 'soLuong', key: 'soLuong', width: 60, align: 'center' },
    { title: 'Đơn giá', dataIndex: 'donGia', key: 'donGia', render: (val) => (val || 0).toLocaleString('vi-VN') },
    { title: 'Thành tiền', key: 'thanhTien', render: (_, record) => (record.soLuong * (record.donGia || 0)).toLocaleString('vi-VN') },
  ];"""

new_columns = """  const orderColumns = [
    { title: 'Tên món', dataIndex: 'tenMon', key: 'tenMon' },
    { title: 'SL', dataIndex: 'soLuong', key: 'soLuong', width: 60, align: 'center' },
    { title: 'Đơn giá', dataIndex: 'donGia', key: 'donGia', render: (val) => (val || 0).toLocaleString('vi-VN') },
    { title: 'Thành tiền', key: 'thanhTien', render: (_, record) => (record.soLuong * (record.donGia || 0)).toLocaleString('vi-VN') },
    { 
      title: '', 
      key: 'action', 
      width: 40,
      render: (_, record) => (
        <Button type="text" danger icon={<DeleteOutlined />} onClick={() => handleRemoveItem(record.id)} />
      )
    }
  ];"""

if old_columns in c:
    c = c.replace(old_columns, new_columns)

# 4. Replace calculateTotal calls and update UI layout
old_totals_ui = """                    <Card style={{ marginTop: 'auto', backgroundColor: '#fafafa', borderColor: '#d9d9d9' }} bodyStyle={{ padding: '16px' }}>
                      <Row justify="space-between" align="middle" style={{ marginBottom: '16px' }}>
                        <Text strong style={{ fontSize: '16px' }}>Tổng tiền:</Text>
                        <Text strong type="danger" style={{ fontSize: '20px' }}>
                          {calculateTotal().toLocaleString('vi-VN')} đ
                        </Text>
                      </Row>"""

new_totals_ui = """                    <Card style={{ marginTop: 'auto', backgroundColor: '#fafafa', borderColor: '#d9d9d9' }} bodyStyle={{ padding: '16px' }}>
                      <Row justify="space-between" align="middle">
                        <Text type="secondary">Tạm tính:</Text>
                        <Text strong>{currentOrder?.tongTienGoc ? currentOrder.tongTienGoc.toLocaleString('vi-VN') : 0} đ</Text>
                      </Row>
                      <Row justify="space-between" align="middle">
                        <Text type="secondary">Thuế & Phí:</Text>
                        <Text strong>{currentOrder?.tienThue ? currentOrder.tienThue.toLocaleString('vi-VN') : 0} đ</Text>
                      </Row>
                      <Row justify="space-between" align="middle" style={{ marginBottom: '8px' }}>
                        <Text type="secondary">Khuyến mãi tự động:</Text>
                        <Text strong type="success">-{currentOrder?.tienGiamGia ? currentOrder.tienGiamGia.toLocaleString('vi-VN') : 0} đ</Text>
                      </Row>
                      <Row justify="space-between" align="middle" style={{ marginBottom: '16px', borderTop: '1px solid #d9d9d9', paddingTop: '8px' }}>
                        <Text strong style={{ fontSize: '16px' }}>Tổng thanh toán:</Text>
                        <Text strong type="danger" style={{ fontSize: '20px' }}>
                          {currentOrder?.tongThanhToan ? currentOrder.tongThanhToan.toLocaleString('vi-VN') : 0} đ
                        </Text>
                      </Row>"""

if old_totals_ui in c:
    c = c.replace(old_totals_ui, new_totals_ui)

# 5. Fix disabled conditions
c = c.replace("amountGiven < calculateTotal()", "amountGiven < (currentOrder?.tongThanhToan || 0)")
c = c.replace("{calculateTotal().toLocaleString('vi-VN')} đ", "{currentOrder?.tongThanhToan ? currentOrder.tongThanhToan.toLocaleString('vi-VN') : 0} đ")
c = c.replace("{Math.max(0, amountGiven - calculateTotal()).toLocaleString('vi-VN')} đ", "{Math.max(0, amountGiven - (currentOrder?.tongThanhToan || 0)).toLocaleString('vi-VN')} đ")

with open(path, 'w', encoding='utf-8') as f: f.write(c)
