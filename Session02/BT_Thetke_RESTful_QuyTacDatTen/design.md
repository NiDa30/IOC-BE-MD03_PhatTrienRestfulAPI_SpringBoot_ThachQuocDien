# Thiết kế RESTful API - Quản lý Công việc (Tasks) và Người dùng (Users)

## 1. Thông tin chung
- **Base URL:** `/api/v1`
- **Định dạng dữ liệu (Content-Type):** `application/json`
- **Quy tắc thiết kế:** Sử dụng danh từ số nhiều cho các resource (`/users`, `/tasks`).

---

## 2. API Endpoints cho Người dùng (Users)

### 2.1. Lấy toàn bộ danh sách người dùng
- **Method:** `GET`
- **Endpoint:** `/users`
- **Mô tả:** Trả về danh sách tất cả người dùng trong hệ thống.
- **Response thành công (200 OK):**
  ```json
  [
    {
      "id": 1,
      "username": "nguyenvana",
      "role": "ADMIN"
    },
    ...
  ]
  ```

### 2.2. Tạo mới người dùng
- **Method:** `POST`
- **Endpoint:** `/users`
- **Mô tả:** Tạo mới một người dùng (Sử dụng `@PostMapping` và `@RequestBody` cùng `@Valid` để kiểm tra dữ liệu).
- **Request Body:**
  ```json
  {
    "username": "nguyenvana",
    "email": "vana@gmail.com",
    "password": "password123",
    "role": "USER"
  }
  ```
- **Response thành công (201 Created):** Trả về thông tin người dùng vừa được tạo.

### 2.3. Cập nhật vai trò của người dùng
- **Method:** `PATCH`
- **Endpoint:** `/users/{id}/role`
- **Mô tả:** Cập nhật quyền/vai trò của một người dùng cụ thể.
- **Request Body:**
  ```json
  {
    "role": "ADMIN"
  }
  ```
- **Response thành công (200 OK):** Trả về thông tin người dùng sau khi cập nhật.

### 2.4. Xóa một người dùng khỏi hệ thống
- **Method:** `DELETE`
- **Endpoint:** `/users/{id}`
- **Mô tả:** Xóa người dùng theo ID được truyền vào trên URL.
- **Response thành công (204 No Content).**

### 2.5. Liệt kê toàn bộ công việc của 1 người dùng
- **Method:** `GET`
- **Endpoint:** `/users/{id}/tasks`
- **Mô tả:** Lấy danh sách các công việc được giao cho một người dùng có ID cụ thể. Đây là cách thể hiện quan hệ một - nhiều trong RESTful.
- **Response thành công (200 OK):** Danh sách các Tasks.

---

## 3. API Endpoints cho Công việc (Tasks)

### 3.1. Lấy toàn bộ danh sách công việc
- **Method:** `GET`
- **Endpoint:** `/tasks`
- **Mô tả:** Trả về danh sách tất cả công việc.

### 3.2. Tạo mới công việc
- **Method:** `POST`
- **Endpoint:** `/tasks`
- **Mô tả:** Tạo mới một công việc và có thể gán luôn cho một người dùng thông qua `userId` (Foreign Key logic).
- **Request Body:**
  ```json
  {
    "title": "Hoàn thiện API backend",
    "description": "Viết RESTful API cho dự án",
    "priority": "high",
    "status": "TODO",
    "userId": 1 
  }
  ```
- **Response thành công (201 Created).**

### 3.3. Cập nhật trạng thái một công việc
- **Method:** `PATCH`
- **Endpoint:** `/tasks/{id}/status`
- **Mô tả:** Chỉ cập nhật trạng thái (`status`) của công việc.
- **Request Body:**
  ```json
  {
    "status": "IN_PROGRESS"
  }
  ```
- **Response thành công (200 OK).**

### 3.4. Xóa một công việc
- **Method:** `DELETE`
- **Endpoint:** `/tasks/{id}`
- **Mô tả:** Xóa công việc khỏi hệ thống.
- **Response thành công (204 No Content).**

### 3.5. Gắn công việc cho người dùng
- **Method:** `PATCH` (hoặc `POST`)
- **Endpoint:** `/tasks/{id}/assign`
- **Mô tả:** Gắn (hoặc thay đổi) người thực hiện cho một công việc.
- **Request Body:**
  ```json
  {
    "userId": 2
  }
  ```
- **Response thành công (200 OK).**

---

## 4. Các Endpoint Tìm Kiếm & Lọc (Filtering)

### 4.1. Tìm các công việc có mức độ ưu tiên là "high"
- **Method:** `GET`
- **Endpoint:** `/tasks?priority=high`
- **Mô tả:** Sử dụng Query Parameter để lọc danh sách các công việc theo mức độ ưu tiên.

### 4.2. Tìm các công việc có độ ưu tiên là "high" và được giao cho người dùng với id là 1
- **Method:** `GET`
- **Endpoint:** `/tasks?priority=high&userId=1`
- **Hoặc có thể dùng Endpoint của User:** `/users/1/tasks?priority=high`
- **Mô tả:** Kết hợp nhiều Query Parameters để lọc dữ liệu nâng cao.

---

## 5. Lưu ý khi triển khai bằng Spring Boot

1. **Nhận dữ liệu JSON:** Sử dụng annotation `@RestController` ở class và `@PostMapping`, `@PatchMapping`, ... ở method. Ở tham số method, dùng `@RequestBody` để Spring tự động parse JSON sang Object Java.
2. **Foreign Key (Khóa ngoại):** Khi tạo Task, Client truyền lên `userId`. Backend cần lấy `userId` này để tìm kiếm `User` entity trong DB, sau đó set `User` cho `Task` entity và lưu vào database.
3. **Validation:** 
   - Thêm dependency `spring-boot-starter-validation`.
   - Dùng các annotation như `@NotBlank`, `@NotNull`, `@Size` trong DTO của Request.
   - Thêm `@Valid` trước tham số có `@RequestBody` để Spring tự động kiểm tra dữ liệu (ví dụ: `public ResponseEntity<?> createTask(@Valid @RequestBody TaskDTO taskDTO)`).
