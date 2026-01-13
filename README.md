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

Phân trang với 3 cái get bên dưới:

GetUser list với 1 tiêu chí:
`http://localhost:8080/user/list?pageNo=1&pageSize=10&sortBy=lastName:asc`

GetUser list với nhiều tiêu chí:

`http://localhost:8080/user/list-order-with-multiple-columns?pageNo=1&pageSize=10&sortBy=lastName:asc, id:desc`

GetUser list với 1 tiêu dùng với EntityManager đê customize query
`http://localhost:8080/user/list-order-with-multiple-columns-and-search?pageNo=0&pageSize=10&search=th&sortBy=id:asc`

GetUser list với Criteria: sort 1 cột, search (truyền nhiều field của User), 1 field của cột đã join là Address 
`http://localhost:8080/user/list-advance-search-with-criteria?pageNo=0&pageSize=10&sortBy=id:asc&address=Tran&search=email:email, id>8`

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
