# 🛒 Shobee Phunschanh - Fullstack E-Commerce Project

---

## 📖 Giới thiệu chung
Dự án **Shobee Phunschanh** là một ứng dụng thương mại điện tử (E-Commerce) được xây dựng theo mô hình Client-Server hiện đại. Dự án tập trung vào việc quản lý sản phẩm, thông tin người dùng, cung cấp quá trình xác thực/phân quyền chặt chẽ với hệ thống RESTful API kết nối với giao diện React mượt mà và trực quan.

## 💡 Góc chia sẻ (Trải nghiệm kỹ thuật & Debug)

## ⌨️ Phím tắt IntelliJ IDEA

- `Ctrl + Shift + F` || `Shift + Shift`: Tìm kiếm toàn bộ project  
- `Ctrl + Shift + N`: Tìm nhanh file (vd: User.class)  
- `Alt + J`: Chọn nhiều từ giống nhau  
- `Ctrl + Alt + L`: Format code  
- `Ctrl + Alt + O`: Loại bỏ import/code không sử dụng  

---

## ⌨️ Phím tắt VS Code

- `Alt + Shift + F`: Format code  
- `Ctrl + D`: Chọn nhiều từ giống nhau  
- `Ctrl + R`: Mở project gần đây (project files)  
- `Ctrl + P`: Tìm nhanh file (vd: Page.tsx)  
- `Shift + Alt + O`: Loại bỏ import/code không sử dụng  
- `Alt + ←`: Quay lại vị trí trước (sau khi Ctrl + Click)  
- `Ctrl + Shift + I`: Mở chat phụ (tuỳ extension) hoặc Ctrl + P > Open Chat  
- `Ctrl + Shift + P`: Mở Command Palette  

### 📌 Một số lệnh trong Command Palette

- `Accounts: Sign Out`: Đăng xuất tài khoản  
- `Collapse Folders in Explorer`: Thu gọn toàn bộ thư mục, sử dụng khi Ctrl + P >

## 🚀 Công nghệ sử dụng (Tech Stack)

### Backend (Java Spring Boot)
- **Framework:** Spring Boot 4.0.1, Java 17
- **Database:** PostgreSQL, Spring Data JPA, Hibernate
- **Security:** Spring Security, JSON Web Token (JWT) cho xác thực không trạng thái (stateless authentication)
- **API Documentation:** Springdoc OpenAPI (Swagger UI) 2.8.8
- **Các thư viện khác:** Spring Kafka, Spring Mail, MapStruct, Lombok, P6Spy (SQL Logger), Thymeleaf

### Frontend (React & Vite)
- **Core:** React 19, TypeScript, Vite 7
- **Giao diện (Styling):** Tailwind CSS 3, Framer Motion (Animation), @floating-ui/react-dom-interactions
- **Routing:** React Router DOM 7
- **Data Fetching & State:** TanStack React Query 5, Axios
- **Form & Validation:** React Hook Form, Yup

---

## 📂 Cấu trúc thư mục (Directory Structure)

### 🖥️ Backend (Thư mục gốc)
Cấu trúc theo chuẩn N-Tier Architecture của Spring Boot:
```text
.
├── src/main/java/com/example/demo
│   ├── configuration/   # Cấu hình Spring, CORS, Swagger, Database Loader
│   ├── constant/        # Các hằng số (Role, Permission, API Path,...)
│   ├── controller/      # REST API Endpoints phục vụ Frontend
│   ├── dto/             # Data Transfer Objects cho Request/Response
│   ├── exception/       # Xử lý ngoại lệ tập trung (GlobalExceptionHandler)
│   ├── mapper/          # MapStruct interfaces chuyển đổi qua lại Entity <-> DTO
│   ├── model/           # JPA Entities (Định nghĩa bảng CSDL: User, Role, Address)
│   ├── repository/      # Spring Data JPA Repositories
│   ├── service/         # Business logic cốt lõi
│   └── util/            # Các hàm hỗ trợ, utils (Format, Validate,...)
├── src/main/resources/
│   ├── application.yml        # Cấu hình chung của ứng dụng
│   ├── application-dev.yml    # Cấu hình cho môi trường phát triển (Port 8080, DB config)
│   ├── messages_*.properties  # Các file properties hỗ trợ Đa ngôn ngữ (i18n)
│   └── templates/             # HTML Templates (để gửi email bằng Thymeleaf)
├── docker-compose.yml   # Cài đặt Docker cho PostgreSQL, Kafka, v.v.
├── pom.xml              # Đóng gói và quản lý dependencies của Maven
└── postgresql.sql       # Script khởi tạo cơ sở dữ liệu
```

### 📱 Frontend (Thư mục `/shop`)
```text
shop/
├── src/
│   ├── apis/            # Tổ chức các hàm gọi RESTful API bằng Axios
│   ├── assets/          # Static assets: Hình ảnh tĩnh, SVG, Font chữ
│   ├── components/      # UI components dùng chung (Button, Input, Pagination, Header, Footer)
│   ├── constants/       # Hằng số cấu hình, định tuyến đường dẫn (path)
│   ├── contexts/        # Auth Context quản lý trạng thái đăng nhập toàn cục
│   ├── hooks/           # Các Custom hook tái sử dụng logic (useQuery)
│   ├── layouts/         # Bố cục giao diện: MainLayout, RegisterLayout
│   ├── pages/           # Giao diện chính: Login, Register, Profile, ProductList, ProductDetail
│   ├── types/           # Type definitions chung của TypeScript
│   ├── utils/           # Helper functions (Validate form, format date/money, cấu hình file)
│   ├── App.tsx          # React Root Component
│   ├── main.tsx         # File khởi chạy chính của Vite + React
│   └── useRouteElements.tsx # Cấu hình React Router (Protected / Rejected routing)
├── index.html           # HTML gốc, entry point của app
├── package.json         # Danh sách NPM packages, scripts build
├── tailwind.config.cjs  # Cấu hình file utility class của Tailwind CSS
└── vite.config.ts       # Cấu hình trình đóng gói Vite
```

---

## ⚙️ Hướng dẫn cài đặt và chạy dự án (Installation & Run)

### 1. Khởi chạy Database bằng Docker
Yêu cầu: Có cài đặt Docker và Docker Compose.
```bash
# Khởi chạy các container nền (PostgreSQL, Kafka, v.v.)
docker compose up -d
```
*Lưu ý:* Cần phải tạo Database bằng cách sao chép nội dung file `postgresql.sql` và chạy vào hệ quản trị CSDL kết nối tới `localhost:5432` như đã dọn sẵn ở `docker-compose.yml`. (Tài khoản/Mật khẩu lưu trong file dev yml hoặc docker file).

\*  **Lưu ý cho lần đầu chạy Spring Boot (LazyInitializationException / Khởi tạo bảng):** 
1. Đảm bảo cấu hình `ddl-auto: update` để tự sinh các bảng không có trong script sql.
2. Sau khi sinh xong, set lại bằng `none` hoặc `validate` để tránh tạo lại làm mất cái enum, cái này mình chưa fix nữa.
Kể từ lúc cấu hình xong, Identity ID chuyển thành UUID do đó không cần import SQL file nữa.

### 2. Chạy Backend (Spring Boot)
Yêu cầu: JDK 17, Maven (`mvnw` đã tồn tại trong thư mục).
```bash
# Clean và đóng gói không test
./mvnw clean package -DskipTests

# Chạy Backend (chỉ định profile dev)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```
Backend sẽ khởi chạy tại: `http://localhost:8080/`.

### 3. Chạy Frontend (React Vite)
Yêu cầu: Node.js version mới nhất (hiện tại mình dùng v24.11.1), đã cài đặt `yarn`.
```bash
cd shop

# Cài đặt tất cả phụ thuộc (Dependencies)
yarn install

# Khởi chạy Server Development
yarn dev
```
Frontend sẽ được Build bằng Vite và mở nhanh chóng tại Port 3000 (Localhost). `yarn build` sẽ chạy qua tiến trình `tsc -b` sau đó ra production build.

---

## 🌟 Các chức năng chính (Main Functionalities)

### Frontend
- **Hệ thống Routing an toàn:** 
  - *Protected Router:* Chỉ truy cập được `Profile` và các trang cá nhân nếu đã xác thực.
  - *Rejected Router:* Đã đăng nhập thì chặn truy cập ngược lại `/login` hay `/register`.
- **Trải nghiệm UX/UI tĩnh:**
  - Danh sách sản phẩm (`ProductList`), Trang chi tiết (`ProductDetail`).
  - Giao diện đẹp, chuẩn responsive nhờ **Tailwind CSS**, Validation qua rule chặt chẽ với **Yup**.
  - Cache API mượt mà và re-fetching background bằng **TanStack React Query**.

### Backend
- **Xác thực phi trạng thái (Stateless Auth):**
  - Quản lý Security thông qua JSON Web Token (Access Key, Refresh Key với Expiry tách biệt). ContextHolder được nạp thông qua `PreFilter.doFilterInternal()`.
- **Mô hình RBAC Chuyên sâu (Role-Based Access Control):**
  - Phân tách quyền nghiêm ngặt tới từng role (`sysadmin`, `admin`, `manager`, `user`).
  - Permissions rõ ràng (`Full Access`, `View`, `Add`, `Update`, `Delete`, `Upload`...).
- **Xử lý Query Linh Hoạt, Đa Điều Kiện (Phân trang & Tìm kiếm):**
  - **1. List với 1 tiêu chí sort:**
    `GET /user/list?pageNo=1&pageSize=10&sortBy=lastName:asc`
  - **2. List với nhiều tiêu chí sort:**
    `GET /user/list-order-with-multiple-columns?pageNo=1&pageSize=10&sortBy=lastName:asc,id:desc`
  - **3. List với EntityManager (customize query):**
    `GET /user/list-order-with-multiple-columns-and-search?pageNo=0&pageSize=10&search=th&sortBy=id:asc`
  - **4. List với Criteria:**
    *(Sort 1 cột, search nhiều field của User và 1 field thuộc bảng đã join là Address)*
    `GET /user/list-advance-search-with-specification?page=0&size=5&sort=id&user=firstName~a&address=city~a`
  - **5. List với Specification:**
    *(Sort của pageable, search nhiều field dựa vào Spec join 2 cột, kết nối AND/OR tùy biến)*
    `GET /user/list-advance-search-with-specification?page=0&size=5&sort=id&user=firstName~a&address=city~a,street~T`
  
  *💡 Phân loại xử lý các trường hợp Tìm Kiếm & Lọc:*
  - **TH1:** Không truyền user hay address ➡️ Dùng Spring `findAll` kèm Pageable (tự động Sort).
  - **TH2:** Chỉ có trường dữ liệu user ➡️ Dùng `findAll` kết hợp Specification và Pageable (tự động Sort).
  - **TH3:** Có cả user và address ➡️ Dùng `EntityManager` tự xây dựng query phức tạp. (Đã xử lý Page, Size. ⚠️ **Lưu ý: Sort chưa hoạt động ở TH3! Mình định làm TH3 mà chưa kịp làm xong, có gì sau này sẽ cập nhật thêm nha.**)
- **Tối Ưu Hoá Log (P6Spy): Có gì bật mấy file liên quan tới P6Spy lên là thấy log SQL**
  - Ghi đè cấu hình ghi log SQL siêu trực quan giúp Debug Query thay vì dùng `show-sql` mặc định của Hibernate. Tính performance cho 1 truy vấn database cực nhanh.
- **REST APIs Tự Động Hóa (Swagger):** 
  - UI Docs tại `http://localhost:8080/swagger-ui/index.html`.
  - Xuất ra JSON để import dễ dàng vào Postman (`api-document-get-from-swagger.json`).
- **Tùy chọn Đa Ngôn Ngữ (i18n):**
  - Tự động thay đổi ngôn ngữ Resource theo thẻ Header Request `Accept-Language: vi-VN` | `en-US` | `mx-MX`.

---

## 📐 Architecture Diagram

<img width="2778" height="1460" alt="Untitled (3)" src="https://github.com/user-attachments/assets/9b906369-91a6-4e6a-ae71-ebf8e0a7a583" />

---

### ⚠️ Xử lý `LazyInitializationException`
Khi cấu hình Model với `FetchType.LAZY` (User lấy Role):
- Ở Layer Service, khi trong block Transaction, ta gọi Fetch thì sẽ không lỗi.
- Ở Layer Filter (Ví dụ: `PreFilter`), Spring Security bị mất Session/Transaction, khiến cho việc trích xuất Authorities bị văng exception.
**Giải quyết:** Tách việc load `UserDetails` ra riêng lẻ để Spring luôn kéo được Entity kèm theo Role thông qua truy vấn chuẩn mà không gặp proxy rỗng.

### 🔐 Spring Security + JWT Flow Tóm lược
1. Người dùng POST account cho `/auth/access`.
2. Truy xuất tới `AuthService` -> `AuthManager` -> `DaoAuthProvider` xác minh Hash mật khẩu chuẩn trong DB.
3. Controller trả lại Bearer Token. Phiên (Session) là `STATELESS`.
4. Với những Resource có bảo vệ (ví dụ `/user/1`), hệ thống filter sẽ kiểm tra Header. Nếu hợp lệ, load Token vào SecurityContextHolder để xử lý.
**Lưu ý:** Filter yêu cầu tối ưu gọi Database (đã giảm từ 5 Query/Request xuống tối ưu hơn)!

---
