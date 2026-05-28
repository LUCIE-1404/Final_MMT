# Final_MMT
# Hệ thống Quản lý Nhân sự (HRM) - Client/Server Architecture

Đồ án Môn học: Mạng Máy Tính 
Khoa Khoa học Công nghệ tiên tiến (FAST) - Trường Đại học Bách khoa

## 1. Tổng quan dự án
Dự án xây dựng kiến trúc mạng Client-Server. 
- **Client:** Ứng dụng di động Android (Java) giao tiếp qua HTTP Protocol.
- **Server:** Node.js (Express) cung cấp RESTful API.
- **Xác thực:** Sử dụng Firebase Authentication để sinh và xác thực ID Token qua HTTP Header (Bearer Token).

## 2. Công nghệ sử dụng
* **Mạng & Giao thức:** HTTP/HTTPS, REST API, JSON.
* **Client:** Android Studio, Java, Retrofit2 (Xử lý HTTP Requests).
* **Backend:** Node.js, Express, Body-parser.

## 3. Hướng dẫn Triển khai (Deployment Guide)
### 3.1. Khởi chạy Backend Server (Node.js)
1. Mở terminal tại thư mục `hrm_backend_node`.
2. Cài đặt các gói phụ thuộc (Dependencies):
   npm install
3. Khởi chạy server tại cổng 3000:
   npm start
### 3.2. Cấu hình Client (Android)
1. Mở project Android bằng Android Studio.
2. Mở file RetrofitClient.java.
3. Build và Run ứng dụng trên thiết bị thật hoặc Emulator.
### 3.3. API Endpoints
  GET /api/v1/employees : Truy xuất toàn bộ danh sách nhân sự.
  POST /api/v1/employees: Khởi tạo luồng mạng thêm nhân sự mới.
  PUT /api/v1/employees/:id: Gửi gói tin cập nhật thông tin.
  DELETE /api/v1/employees/:id: Gửi tín hiệu xóa dữ liệu.
