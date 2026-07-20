import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/auth/Register';
import ProtectedRoute from './components/ProtectedRoute';
import AdminLayout from './layouts/AdminLayout';
import CustomerLayout from './layouts/CustomerLayout';
import TableManagement from './pages/admin/TableManagement';
import MenuManagement from './pages/admin/MenuManagement';
import VoucherManagement from './pages/admin/VoucherManagement';
import BookingManagement from './pages/admin/BookingManagement';
import POSManagement from './pages/admin/POSManagement';
import Home from './pages/customer/Home';
import Profile from './pages/customer/Profile';
import MyBookings from './pages/customer/MyBookings';
import Menu from './pages/customer/Menu';
import Dashboard from './pages/admin/Dashboard';
import ShiftManagement from './pages/admin/ShiftManagement';
import Analytics from './pages/admin/Analytics';

function App() {
  return (
    <Router>
      <Routes>
        {/* Customer Routes */}
        <Route element={<CustomerLayout />}>
          <Route path="/" element={<Home />} />
          <Route path="/menu" element={<Menu />} />
        </Route>
        
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        
        {/* Protected Routes for Admin/Staff */}
        <Route path="/admin" element={<ProtectedRoute allowedRoles={['ROLE_ADMIN', 'ROLE_STAFF', 'ADMIN', 'STAFF', 'NHAN_VIEN', 'QUAN_LY']} />}>
          <Route element={<AdminLayout />}>
            <Route index element={<Dashboard />} />
            <Route path="pos" element={<POSManagement />} />
            <Route path="bookings" element={<BookingManagement />} />
            <Route path="tables" element={<TableManagement />} />
            <Route path="menu" element={<MenuManagement />} />
            <Route path="vouchers" element={<VoucherManagement />} />
            
            {/* Dành riêng cho ADMIN */}
            <Route element={<ProtectedRoute allowedRoles={['ROLE_ADMIN', 'ADMIN']} />}>
              <Route path="shifts" element={<ShiftManagement />} />
              <Route path="analytics" element={<Analytics />} />
            </Route>
          </Route>
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
