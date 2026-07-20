import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { Result, Button } from 'antd';

const ProtectedRoute = ({ allowedRoles }) => {
  const token = localStorage.getItem('accessToken');
  const userRole = localStorage.getItem('role');

  // Nếu không có token, chuyển hướng về trang đăng nhập
  if (!token) {
    return <Navigate to="/login" replace />;
  }

  // Nếu có truyền mảng allowedRoles, kiểm tra role
  if (allowedRoles && allowedRoles.length > 0) {
    if (!userRole || !allowedRoles.includes(userRole)) {
      // Return 403
      return (
        <Result
          status="403"
          title="403"
          subTitle="Xin lỗi, bạn không có quyền truy cập trang này."
          extra={<Button type="primary" onClick={() => window.location.href = '/'}>Về trang chủ</Button>}
        />
      );
    }
  }

  // Nếu có token và đúng quyền, cho phép render các component con
  return <Outlet />;
};

export default ProtectedRoute;
