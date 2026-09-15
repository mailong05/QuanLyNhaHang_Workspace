import os
import re

path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/BookingManagement.jsx'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

# Replace showAssignTableModal and handleApprove with handleApproveDirectly
new_approve = """
  const handleApproveDirectly = async (record) => {
    try {
      const res = await apiClient.get(`/api/v1/phieu-dat-ban/${record.maPhieuDat}`);
      const fullBooking = res.data ? res.data : res;
      
      const payload = {
        ...fullBooking,
        trangThai: 'DA_XAC_NHAN'
      };
      
      await apiClient.put(`/api/v1/phieu-dat-ban/${record.maPhieuDat}`, payload);
      message.success('Duyệt phiếu đặt bàn thành công!');
      fetchBookings();
    } catch (error) {
      message.error(error.message || 'Lỗi khi duyệt phiếu');
    }
  };
"""

# We need to find where to inject it. I'll replace the showAssignTableModal and handleApprove block.
c = re.sub(
    r'const showAssignTableModal = \(record\) => \{.*?fetchBookings\(\);\s*\} catch \(error\) \{\s*message\.error\(error\.message \|\| \'Lỗi khi duyệt phiếu\'\);\s*\}\s*\};',
    new_approve.strip(),
    c,
    flags=re.DOTALL
)
c = re.sub(
    r'const showAssignTableModal = \(record\) => \{.*?fetchBookings\(\);\s*\} catch \(error\) \{\s*message\.error\(error\.message \|\| \'L-i khi duyt phiu\'\);\s*\}\s*\};',
    new_approve.strip(),
    c,
    flags=re.DOTALL
)

# And change the onClick of the Duyệt button:
c = re.sub(
    r'onClick=\{\(\) => showAssignTableModal\(record\)\}',
    r'onClick={() => handleApproveDirectly(record)}',
    c
)

# Remove Modal Xếp bàn (Duyệt) block
c = re.sub(
    r'\{\/\* Modal Xếp bàn \(Duyệt\) \*\/\}.*?<\/Modal>',
    '',
    c,
    flags=re.DOTALL
)
c = re.sub(
    r'\{\/\* Modal Xp bAn \(Duyt\) \*\/\}.*?<\/Modal>',
    '',
    c,
    flags=re.DOTALL
)

with open(path, 'w', encoding='utf-8') as f: f.write(c)
