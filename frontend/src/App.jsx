import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import ProtectedRoute from './components/ProtectedRoute';
import AdminLayout from './layouts/AdminLayout';
import TableManagement from './pages/admin/TableManagement';
import MenuManagement from './pages/admin/MenuManagement';
import VoucherManagement from './pages/admin/VoucherManagement';
import BookingManagement from './pages/admin/BookingManagement';
import POSManagement from './pages/admin/POSManagement';

function Home() {
  return (
    <div style={{ textAlign: 'center', marginTop: '50px' }}>
      <h1>Trang chủ - Quản lý Đặt bàn Nhà hàng</h1>
      <p>Đây là trang dành cho khách hàng đặt bàn trực tuyến.</p>
    </div>
  );
}

function AdminDashboard() {
  return (
    <div>
      <h2>Chào mừng đến với trang quản trị</h2>
      <p>Chọn chức năng từ menu bên trái để bắt đầu.</p>
    </div>
  );
}

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        
        {/* Protected Routes for Admin/Staff */}
        <Route path="/admin" element={<ProtectedRoute />}>
          <Route element={<AdminLayout />}>
            <Route index element={<AdminDashboard />} />
            <Route path="pos" element={<POSManagement />} />
            <Route path="tables" element={<TableManagement />} />
            <Route path="menu" element={<MenuManagement />} />
            <Route path="vouchers" element={<VoucherManagement />} />
            <Route path="bookings" element={<BookingManagement />} />
          </Route>
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
