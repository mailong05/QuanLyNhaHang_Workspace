# 🍽️ Restaurant Management System (Hệ Thống Quản Lý Đặt Bàn & Nhà Hàng)

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-brightgreen.svg?logo=springboot)
![React](https://img.shields.io/badge/React-19.0-blue.svg?logo=react)
![Ant Design](https://img.shields.io/badge/Ant_Design-6.4-blue.svg?logo=antdesign)
![MSSQL](https://img.shields.io/badge/SQL_Server-Latest-red.svg?logo=microsoftsqlserver)

Một hệ thống phần mềm toàn diện dành cho doanh nghiệp F&B, cung cấp giải pháp chuyển đổi số từ khâu **đặt bàn trực tuyến của khách hàng** (B2C) đến khâu **vận hành, thu ngân POS và quản trị nhân sự** (B2B) của nhà hàng.

---

## 🌟 Tính Năng Nổi Bật (Key Features)

### 1. Phân Hệ Khách Hàng (Customer Portal)
* **Đặt bàn trực tuyến (Web Booking):** Giao diện trực quan cho phép khách hàng chọn ngày, giờ, số lượng người và **chọn bàn trực tiếp trên Sơ đồ nhà hàng (Cinema-style Table Map)**.
* **Tài khoản cá nhân:** Khách hàng có thể tự đăng ký hoặc được cấp tài khoản để theo dõi lịch sử đặt bàn và Điểm tích lũy.

### 2. Phân Hệ Vận Hành & Thu Ngân (POS & Staff)
* **Giao dịch POS:** Thêm món, gọi món, quản lý trạng thái bàn (Trống, Đang phục vụ, Đã đặt).
* **Nghiệp vụ linh hoạt:** Hỗ trợ **Gộp bàn (Merge Tables)**, đổi bàn, áp dụng Khuyến mãi (Voucher) và tính Thuế (VAT).
* **Quản lý Ca làm việc (Shift Management):** Mở ca, giao ca, kết ca và thống kê tiền mặt tại quầy chặt chẽ.

### 3. Phân Hệ Quản Trị Hệ Thống (Admin Dashboard)
* **Quản lý Nhân Sự:** Phân quyền hệ thống nghiêm ngặt dựa trên Role (ADMIN, NHAN_VIEN). Tự động tạo và quản lý tài khoản truy cập cho nhân viên.
* **Quản lý Khách Hàng:** Quản lý thông tin, phân hạng thành viên, tích điểm điểm thành viên.
* **Quản lý Thực Đơn & Voucher:** Cập nhật món ăn, khu vực (Tầng 1, Tầng 2, VIP), chương trình khuyến mãi.
* **Báo cáo & Phân tích (Analytics):** Dashboard thống kê doanh thu trực quan (Sử dụng biểu đồ Recharts).

---

## 🛠️ Công Nghệ Sử Dụng (Tech Stack)

### Backend (Lõi Hệ Thống)
Hệ thống API được xây dựng theo kiến trúc **RESTful**, đảm bảo hiệu suất cao và bảo mật nghiêm ngặt.
* **Framework:** Java Spring Boot 3 (Jakarta EE)
* **ORM & Database:** Spring Data JPA, Hibernate, **Microsoft SQL Server (MSSQL)**
* **Bảo mật (Security):** Spring Security 6, Stateless Authentication bằng **JWT (JSON Web Token)**
* **Tài liệu API:** Springdoc OpenAPI (Swagger UI)
* **Tiện ích:** Lombok, Maven

### Frontend (Giao diện Người Dùng)
Ứng dụng Web tĩnh, render nhanh, kiến trúc Component-based.
* **Core:** React 19 (React Hooks), Build tool Vite siêu tốc.
* **UI Framework:** Ant Design (antd) v6 - Chuẩn Enterprise UI.
* **Routing & State:** React Router DOM.
* **Xử lý dữ liệu:** Axios (HTTP Client), Day.js (Xử lý DateTime).
* **Data Visualization:** Recharts (Vẽ biểu đồ báo cáo).

---

## 🔒 Kiến Trúc Bảo Mật (Security Architecture)

Hệ thống áp dụng chuẩn bảo mật RBAC (Role-Based Access Control) thông qua JWT:
1. `Role.KHACH_HANG`: Chỉ được truy cập API public (menu, xem bàn trống) và API cá nhân (profile).
2. `Role.NHAN_VIEN`: Được truy cập module POS, Hóa đơn, Giao ca. (Bị chặn ở các module báo cáo doanh thu tổng).
3. `Role.ADMIN`: Toàn quyền truy cập quản trị nhân sự và cấu hình hệ thống.
* *Mật khẩu được băm (Hash) an toàn bằng PasswordEncoder trước khi lưu vào DB.*

---

## 🚀 Hướng Dẫn Cài Đặt (Installation)

### 1. Yêu cầu môi trường (Prerequisites)
* Java JDK 17+
* Node.js 18+ & npm
* Microsoft SQL Server
* Maven 3.8+

### 2. Cài đặt Backend
```bash
cd backend
# Cấu hình chuỗi kết nối Database tại: src/main/resources/application.properties
mvn clean install
mvn spring-boot:run
```
*Backend sẽ chạy tại: `http://localhost:8080`*
*Swagger UI (Tài liệu API): `http://localhost:8080/swagger-ui.html`*

### 3. Cài đặt Frontend
```bash
cd frontend
npm install
npm run dev
```
*Frontend sẽ chạy tại: `http://localhost:5173`*

---

## 👨‍💻 Tác giả (Author)
* **Dự án:** Quản Lý Đặt Bàn Nhà Hàng (Ver Web)
* Bản quyền thuộc về Đội ngũ phát triển.
