import os

path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/BookingManagement.jsx'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

old = """      } else {
        // GỘP HÓA ĐƠN
        message.warning('Tính năng Gộp Hóa Đơn đang chờ Backend hoàn thiện API (/api/v1/hoa-don/merge)!');
        return;
      }"""

new = """      } else {
        // GỘP HÓA ĐƠN
        const payload = {
            maPhieuDatNguon: transferMergeBooking.maPhieuDat,
            maBanDich: selectedMapTable.maBan
        };
        await apiClient.post('/api/v1/pos/gop-ban', payload);
        message.success(`Gộp hóa đơn thành công vào Bàn ${selectedMapTable.maBan}!`);
      }"""

c = c.replace(old, new)
with open(path, 'w', encoding='utf-8') as f: f.write(c)
