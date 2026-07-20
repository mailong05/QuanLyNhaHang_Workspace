import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import ProtectedRoute from './components/ProtectedRoute';
import AdminLayout from './layouts/AdminLayout';
import TableManagement from './pages/admin/TableManagement';
import MenuManagement from './pages/admin/MenuManagement';
import VoucherManagement from './pages/admin/VoucherManagement';
import BookingManagement from './pages/admin/BookingManagement';
import POSManagement from './pages/admin/POSManagement';
import Home from './pages/customer/Home';
import Dashboard from './pages/admin/Dashboard';
import ShiftManagement from './pages/admin/ShiftManagement';
import Analytics from './pages/admin/Analytics';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        
        {/* Protected Routes for Admin/Staff */}
        <Route path="/admin" element={<ProtectedRoute allowedRoles={['ROLE_ADMIN', 'ROLE_STAFF', 'ADMIN', 'STAFF']} />}>
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
