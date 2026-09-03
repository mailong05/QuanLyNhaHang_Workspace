import axios from 'axios';
import { message } from 'antd';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000,
});

// Request Interceptor
apiClient.interceptors.request.use(
  (config) => {
    // Tự động lấy accessToken từ sessionStorage và đính kèm vào Header
    const token = sessionStorage.getItem('accessToken');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response Interceptor
apiClient.interceptors.response.use(
  (response) => {
    // Bóc tách vỏ bọc ApiResponse từ Backend (Spring Boot)
    // Nếu thành công, chỉ lấy response.data.data
    if (response.data && response.data.code === 200) {
      return response.data.data;
    }
    return response;
  },
  (error) => {
    // Xử lý các lỗi từ Backend
    const message = error.response?.data?.message || error.message || 'Đã xảy ra lỗi hệ thống!';
    const code = error.response?.status;

    if (code === 401) {
      // Chưa xác thực
      console.error('Lỗi 401: Chưa xác thực hoặc Token hết hạn!');
      sessionStorage.removeItem('accessToken');
      if (window.location.pathname !== '/login') {
        message.error("Phiên đăng nhập đã hết hạn, vui lòng đăng nhập lại!");
        window.location.href = '/login';
      }
    } else if (code === 403) {
      // Không có quyền
      console.error('Lỗi 403: Bạn không có quyền truy cập tài nguyên này!');
    }

    return Promise.reject({ code, message, error });
  }
);

export default apiClient;
