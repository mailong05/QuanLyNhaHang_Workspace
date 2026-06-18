import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

const ProtectedRoute = () => {
  const token = localStorage.getItem('accessToken');

  // Nếu không có token, chuyển hướng về trang đăng nhập
  if (!token) {
    return <Navigate to="/login" replace />;
  }

  // Nếu có token, cho phép render các component con bên trong
  return <Outlet />;
};

export default ProtectedRoute;
