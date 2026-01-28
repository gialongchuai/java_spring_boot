Đầu tiên chạy file `docker-compose.yml` với docker:
`docker compose up -d`

Sau đó login web với localhost chỉ định postgre trong thông tin trong file đó trên local host.
Tiếp tục tạo mới database trên postgresql với file `postgresql.sql`
dán nội dung vào chạy ra bảng.

Phím tắt:
    + tìm kiếm nhanh: ctrl + shift + f
    + chọn nhiều từ giống: alt + j
    + format code: ctrl + alt + l
    + loại bỏ code không dùng: ctrl + alt + o

FIle `call_api_cors.html` để chạy thử cors trên fe call tới api back test...
File `application.yml` có:
`
spring:
    profiles:
        active: dev
`
Để dev cho nó chạy normal không bị lỗi chứ mặc định đúng là: `@spring.profiles.active@`
để khi build java -jar bằng maven ta có thể chỉ định môi trường build
bằng lệnh `mvn package -P test || mvn clean package -P dev
|| mvn clean package -P dev` hoặc hình như `mvn clean package -P test, dev` được luôn 
Có thể `ngoại trừ môi trường dev` như `mvn clean package -P !dev`

`P6Spy` giúp log dễ xem hơn, ta cần tại ra file `spy.properties`, thêm vào `application-dev.yml` và config 
cho file `CompactSqlFormatter` trong package `config`
ex: `[SQL]
ExecutionTime: 3ms | Connection: 0 | UserServiceImpl.saveUser:85 |
insert into tbl_address (address_type,apartment_number,building,city,country,created_at,created_by,floor,street,street_number,updated_at,updated_by,user_id) values (1,'K13','Sunrise City','Ho Chi Minh','Vietnam','2026-01-27T08:26:12.382+0700',NULL,'12','Nguyen Huu Tho','123','2026-01-27T08:26:12.382+0700',NULL,70)
`

Code có kèm document cho swagger thông qua link: http://localhost:8080/swagger-ui/index.html
Với depen openai có kèm swagger trong đó
Từ đó có thể convert qua postman bằng cách bấm vào từ `/v3/api-docs/api-service-1`
để thấy thông tin json sau đó lưu dưới file có trong thư mục như `api-document-get-from-swagger.json`
sau đó import vào postman

---
Method for User:

Post:
`http://localhost:8080/user/`

Update:
`http://localhost:8080/user/13`

Patch:
NONE | ACTIVE | INACTIVE
`http://localhost:8080/user/12?status=NONE`

Delete:
`http://localhost:8080/user/12`

GetUser:
`http://localhost:8080/user/14`

Phân trang với 5 cái get bên dưới:

GetUser list với 1 tiêu chí:
`http://localhost:8080/user/list?pageNo=1&pageSize=10&sortBy=lastName:asc`

GetUser list với nhiều tiêu chí:

`http://localhost:8080/user/list-order-with-multiple-columns?pageNo=1&pageSize=10&sortBy=lastName:asc, id:desc`

GetUser list với 1 tiêu dùng với EntityManager đê customize query
`http://localhost:8080/user/list-order-with-multiple-columns-and-search?pageNo=0&pageSize=10&search=th&sortBy=id:asc`

GetUser list với Criteria: sort 1 cột, search (truyền nhiều field của User), 1 field của cột đã join là Address 
`http://localhost:8080/user/list-advance-search-with-specification?page=0&size=5&sort=id&user=firstName~a&address=city~a`

GetUser list với Specification: sort của pageable, search nhiều field dựa vào speci join 2 column
tự custom mấy toán tử và join 2 bảng thôi qua and hay or:
đối với TH1: không truyền user and add thì dùng spring findAll với pageable là nó sort dùm
tương tự TH2: với có mỗi user cũng dùng findAll với Spec và pageable và nó cũng sort dùm
tuy nhiên TH3: với sort có cả add và user thì dùng entityManager nên phải custome từng cái
thông qua pageable như page, size, sort: code có làm page size rồi nhưng sort chưa !!!
bên dưới câu api rơi TH3 sort không ăn
`http://localhost:8080/user/list-advance-search-with-specification?page=0&size=5&sort=id&user=firstName~a&address=city~a, street~T`

---

Mô hình RBAC : Role-Based Access Control
Phân quyền dựa trên vai trò: outsourcing, ngân hàng, ...
là cái mô hình đang code

Mô hình ACL : Access Control List
thương mại điện tử: AWS, ...

phân tích xíu nè : RBAC

Role:
- "sysadmin" : người có quyền quản trị hệ thống: IT, phần cứng, .. quản trị toàn hệ thống.
Nhưng không thay đổi các thao tác nghiệp vụ như thao tác sửa đổi bộ phận kế toán

- "admin" : full quyền thao tác nghiệp vụ nhưng không có điều chỉnh đc thao tác hệ thống
Ngược lại với anh ở trên, admin chỉ liên quan tới nghiệp vụ kinh doanh bộ phận ...
CEO, tổng giám đốc 

- "manager" : teamlead, trưởng bộ phận phòng ban

- "user" : nhân viên thường xem thêm sửa nhung không dc xóa

Permission:
- "Full Access" : sysadmin thương có đủ hết

- "View" : user view

- "Add" : thêm bản ghi
- "Update" : thêm bản ghi
Thường outsorce không liên quan tài chính ngân hàng 2 cía là edit, thêm mới or hiểu là chỉnh sửa
Còn tín dụng còn quy trình phê duyệt, chuyên viên tính dụng tạo ra hồ sơ vay vốn, hợp đồng
Nhưng không có quyền approve (chấp thuận) vay hay ko cho vay, này phải có thẩm định viên, giám sát viên, manager
nên tách ra update là trạng thái bản ghi approve

- "Delete" : thường admin

- "Upload" : thường admin cho user, manager tài liệu

- "Import" : xử lý insert hàng loạt, insert file excel cho tải lên mà chưa kiểm soát tài liệu đó
admin manager hoặc user ...

- "Export" : bản báo cáo, excel : tổng kết báo cáo kinh doanh ai được xem
manager, ceo. Cho phép ai được export, vay vốn thu chi công nợ

- "Send"
- "Share" : share mới send được, gán quyền cho xem sửa file ...
  Khi bạn tích hợp Spring Security vào ứng dụng Spring Boot và sử dụng JWT (JSON Web Token) để xác thực stateless, toàn bộ luồng xử lý sẽ diễn ra theo một trình tự rất rõ ràng. Dưới đây là phân tích chi tiết từng bước, từ lúc người dùng gửi yêu cầu đăng nhập cho đến khi gọi một API được bảo vệ bằng token.

---

1. Giai đoạn 1: Đăng nhập – Gửi username và password đến /auth/access
   Người dùng gửi một yêu cầu POST đến endpoint /auth/access kèm theo username và password:
   POST /auth/access
   {
   "username": "sysadmin",
   "password": "123456"
   }
   Bước 1.1: Controller nhận request
   Controller gọi service AuthenticationService.authenticate() để xử lý đăng nhập.

Bước 1.2: Xác thực thông qua AuthenticationManager
Trong service, bạn tạo một đối tượng UsernamePasswordAuthenticationToken chứa username và password, rồi truyền vào authenticationManager.authenticate(). Đây là cách chuẩn để kích hoạt cơ chế xác thực của Spring Security.

Spring Security sẽ tìm AuthenticationProvider phù hợp — trong trường hợp này là DaoAuthenticationProvider mà bạn đã cấu hình trong bean provider().

Bước 1.3: DaoAuthenticationProvider gọi UserDetailsService
DaoAuthenticationProvider tự động gọi phương thức loadUserByUsername(username) trên UserDetailsService mà bạn đã cung cấp. Cụ thể, bạn đã implement UserDetailsService như sau:
return username -> userRepository.findByUsername(username).orElseThrow(...);
→ Điều này dẫn đến lần truy vấn cơ sở dữ liệu đầu tiên:
SELECT ... FROM tbl_user WHERE username = 'sysadmin'
Kết quả trả về là một đối tượng UserDetails chứa thông tin người dùng, bao gồm mật khẩu đã được mã hóa.

Bước 1.4: So sánh mật khẩu
Spring Security sử dụng PasswordEncoder (mà bạn đã cấu hình) để so sánh mật khẩu người dùng gửi lên với mật khẩu đã mã hóa trong cơ sở dữ liệu. Nếu khớp, quá trình xác thực thành công và trả về một đối tượng Authentication đã được xác thực.

Bước 1.5: Truy vấn dư thừa (nếu có)
Trong code hiện tại của bạn, sau khi xác thực thành công, bạn lại gọi:
userRepository.findByUsername(username)
→ Điều này gây ra lần truy vấn thứ hai đến cơ sở dữ liệu với cùng điều kiện. Đây là truy vấn dư thừa, vì bạn hoàn toàn có thể lấy thông tin người dùng từ Authentication.getPrincipal() thay vì truy vấn lại.

Bước 1.6: Tạo và trả về JWT token
Sau khi có thông tin người dùng, bạn tạo một JWT token (thường chứa username hoặc userId trong phần subject) và trả về cho client.

→ Kết thúc giai đoạn đăng nhập: client có token, server không lưu session (vì bạn dùng SessionCreationPolicy.STATELESS).
2. Giai đoạn 2: Gọi API được bảo vệ – Gửi token đến /user/1
   Người dùng gửi yêu cầu GET đến /user/1 kèm theo token trong header:
   GET /user/1
   Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
   Bước 2.1: Spring Security kiểm tra quyền truy cập
   Do endpoint /user/1 không nằm trong danh sách WHITE_LIST, Spring Security yêu cầu xác thực. Trước khi vào controller, request sẽ đi qua chuỗi các filter.

Bạn đã cấu hình:
.addFilterBefore(preFilter, UsernamePasswordAuthenticationFilter.class);
→ Do đó, PreFilter luôn được gọi trước cho mọi request (kể cả request không có token).

Bước 2.2: PreFilter xử lý token
Trong PreFilter.doFilterInternal():

Lấy giá trị từ header Authorization.
Nếu header rỗng hoặc không bắt đầu bằng "Bearer ", filter sẽ bỏ qua và chuyển tiếp request. Lúc này, nếu endpoint yêu cầu xác thực, Spring Security sẽ chặn và trả về lỗi 401.
Nếu có token hợp lệ, filter sẽ:
Trích xuất username từ phần subject của JWT.
Kiểm tra xem SecurityContext hiện tại đã có thông tin xác thực chưa (để tránh ghi đè).
Gọi UserDetailsService.loadUserByUsername(username) → lần truy vấn thứ ba đến cơ sở dữ liệu:

SELECT ... FROM tbl_user WHERE username = 'sysadmin'
Xác minh tính hợp lệ của token (thời hạn, chữ ký...).
Nếu hợp lệ, tạo đối tượng Authentication và lưu vào SecurityContextHolder.
→ Từ thời điểm này, trong suốt vòng đời của request này, Spring Security biết ai đang gọi API.

Bước 2.3: Vào controller
Sau khi PreFilter hoàn tất, request được chuyển tiếp đến controller /user/1.

Controller gọi:
userService.getUser(1L)
→ Bên trong, service gọi:

userRepository.findById(1L)

→ Lần truy vấn thứ tư:

SELECT ... FROM tbl_user WHERE id = 1

Bước 2.4: Serialize phản hồi và lazy load
Entity User của bạn có quan hệ với Address (có thể là @OneToMany với FetchType.LAZY). Khi Jackson serialize đối tượng User ra JSON, nó sẽ gọi getter getAddresses(), từ đó kích hoạt lazy load.

→ Lần truy vấn thứ năm:
SELECT ... FROM tbl_address WHERE user_id = 1
3. Tổng kết toàn bộ luồng
   Mỗi request có token đều đi qua PreFilter, kể cả request đầu tiên hay thứ N.
   Không có khái niệm “lần đầu không kèm token được thông qua” — nếu endpoint yêu cầu xác thực, thì bắt buộc phải có token hợp lệ.
   Các request nằm trong WHITE_LIST (như /auth/access, /swagger-ui/**) sẽ không bị chặn, dù có hay không có token.
   Với cấu hình hiện tại, tổng cộng có 5 lần truy vấn cơ sở dữ liệu cho 2 request (1 login + 1 gọi API):
   Xác thực login (bắt buộc)
   Truy vấn dư thừa khi login
   Load user từ token trong PreFilter
   Load user theo ID trong controller
   Lazy load địa chỉ khi serialize

---

Có thể truyền thêm header: Accept-Language: vi-VN ; en-US ; mx-MX

---
json cho post, update , ...
```json
{
  "firstName": "Nguyen",
  "lastName": "Van D",
  "email": "nguyenvana@gmail.com",
  "phone": "0901234567",
  "dateOfBirth": "1995-08-20",
  "gender": "female",
  "username": "nguyenvana",
  "password": "123456",
  "type": "user",
  "status": "active",
  "addresses": [
    {
      "apartmentNumber": "K13",
      "floor": "12",
      "building": "Sunrise City",
      "streetNumber": "123",
      "street": "Nguyen Huu Tho",
      "city": "Ho Chi Minh",
      "country": "Vietnam",
      "addressType": 1
    },
    {
      "apartmentNumber": "H19",
      "floor": "5",
      "building": "Landmark 81",
      "streetNumber": "208",
      "street": "Nguyen Huu Canh",
      "city": "Ho Chi Minh",
      "country": "Vietnam",
      "addressType": 2
    },
    {
      "apartmentNumber": "B10",
      "floor": "5",
      "building": "Landmark 81",
      "streetNumber": "208",
      "street": "Nguyen Huu Canh",
      "city": "Ho Chi Minh",
      "country": "Vietnam",
      "addressType": 2
    }
  ]
}
```

<img width="638" height="393" alt="image" src="https://github.com/user-attachments/assets/101f06b8-4dd0-420c-855f-660a2c31ac60" />
