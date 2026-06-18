import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import ProtectedRoute from './components/ProtectedRoute';
import AdminLayout from './layouts/AdminLayout';

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
            {/* Thêm các Route quản lý khác vào đây sau này (VD: /admin/tables) */}
          </Route>
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
