Đầu tiên chạy file `docker-compose.yml` với docker:
`docker compose up -d`

Sau đó login web với localhost chỉ định postgre trong thông tin trong file đó trên local host.
Tiếp tục tạo mới database trên postgresql với file `postgresql.sql`
dán nội dung vào chạy ra bảng.

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
