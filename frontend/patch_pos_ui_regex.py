import os
import re

path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/POSManagement.jsx'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

# Replace the first calculateTotal in the order summary section
c = re.sub(
    r'<Row justify="space-between" align="middle" style=\{\{ marginBottom: \'16px\' \}\}>\s*<Text strong style=\{\{ fontSize: \'16px\' \}\}>.*?</Text>\s*<Text strong type="danger" style=\{\{ fontSize: \'20px\' \}\}>\s*\{calculateTotal\(\)\.toLocaleString\(\'vi-VN\'\)\} [^\n]*\s*</Text>\s*</Row>',
    """<Row justify="space-between" align="middle">
                        <Text type="secondary">Tạm tính:</Text>
                        <Text strong>{currentOrder?.tongTienGoc ? currentOrder.tongTienGoc.toLocaleString('vi-VN') : 0} đ</Text>
                      </Row>
                      <Row justify="space-between" align="middle">
                        <Text type="secondary">Thuế (VAT):</Text>
                        <Text strong>{currentOrder?.tienThue ? currentOrder.tienThue.toLocaleString('vi-VN') : 0} đ</Text>
                      </Row>
                      <Row justify="space-between" align="middle" style={{ marginBottom: '8px' }}>
                        <Text type="secondary">Khuyến mãi:</Text>
                        <Text strong type="success">-{currentOrder?.tienGiamGia ? currentOrder.tienGiamGia.toLocaleString('vi-VN') : 0} đ</Text>
                      </Row>
                      <Row justify="space-between" align="middle" style={{ marginBottom: '16px', borderTop: '1px solid #d9d9d9', paddingTop: '8px' }}>
                        <Text strong style={{ fontSize: '16px' }}>Tổng thanh toán:</Text>
                        <Text strong type="danger" style={{ fontSize: '20px' }}>
                          {currentOrder?.tongThanhToan ? currentOrder.tongThanhToan.toLocaleString('vi-VN') : 0} đ
                        </Text>
                      </Row>""",
    c
)

# And in the Checkout Modal:
c = re.sub(
    r'<Text style=\{\{ fontSize: \'18px\' \}\}>.*?</Text>\s*<br/>\s*<Text strong type="danger" style=\{\{ fontSize: \'32px\' \}\}>\s*\{calculateTotal\(\)\.toLocaleString\(\'vi-VN\'\)\} [^\n]*\s*</Text>',
    """<Text style={{ fontSize: '18px' }}>Tổng tiền cần thanh toán</Text>
            <br/>
            <Text strong type="danger" style={{ fontSize: '32px' }}>
              {currentOrder?.tongThanhToan ? currentOrder.tongThanhToan.toLocaleString('vi-VN') : 0} đ
            </Text>""",
    c
)

with open(path, 'w', encoding='utf-8') as f: f.write(c)
