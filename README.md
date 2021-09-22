# Patient-Manager
> 병원 및 환자의 정보를 저장하고 조회합니다.

## 0 빌드 및 실행
    
~~~
// jar build
$ ./mvnw clean package

// 실행
$ java -jar ./target/subject-1.0.jar
~~~

## 1. 개발환경
* Spring-boot-2.5.4
* h2
* querydsl
* spring-docs
* lombok
* java 1.8

## API
### 환자 등록
* 등록시 생성되는 환자 등록번호는 13자리의 랜덤한 숫자로 생성
* Request
~~~
POST /api/v1/patient
* body
{
  "hospitalId": 4,
  "patientName": "김개똥",
  "genderCode": "M",
  "birthDate": "19910221",
  "phoneNumber": "01111123123"
}
~~~

*Response
~~~
{
  "code": "S0000",
  "body": {
    "id": 12,
    "hospitalId": 4,
    "patientName": "김개똥",
    "patientNo": "6721732323565",
    "genderCode": "M",
    "genderName": "남",
    "birthDate": "19910221",
    "phoneNumber": "01111123123"
  }
}
~~~


### 환자 수정
* Request
~~~
PUT /api/v1/patient/16
* parameter
id : 아이디
* body
{
  "hospitalId": 7,
  "patientName": "김개똥2",
  "birthDate": "19910222",
  "phoneNumber": "0111234123"
}
~~~

* Response 
~~~
{
  "code": "S0000",
  "body": {
    "id": 16,
    "hospitalId": 7,
    "patientName": "김개똥2",
    "patientNo": "7550210258662",
    "genderCode": "M",
    "genderName": "남",
    "birthDate": "19910222",
    "phoneNumber": "0111234123"
  }
}
~~~

### 환자 정보 삭제
* Request
~~~
DELETE /api/v1/patient/1
* parameter
id : 아이디
~~~

* Response
~~~
{
  "code": "S0000",
  "body": "삭제"
}
~~~

### 환자 조회
* Request
~~~
GET /api/v1/patient/1
* parameter
id : 아이디
* body
{
  "code": "S0000",
  "body": {
    "id": 13,
    "hospitalId": 5,
    "patientName": "김개똥",
    "patientNo": "123456",
    "genderCode": "M",
    "genderName": "남",
    "birthDate": "19910221",
    "phoneNumber": "01111123123",
    "visits": [
      {
        "id": 1,
        "hospitalId": 5,
        "patientId": 13,
        "receptionDateTime": "2021-09-01T12:59:59",
        "visitStateCode": "1",
        "visitStateName": "방문중"
      }
    ],
    "lastReceptionDateTime": "2021-09-01T12:59:59"
  }
}
~~~

### 환자 목록 조회
* 환자 목록 조회시 Querydsl 사용
* 환자이름, 환자등록번호, 생년월을 조건으로 조회
* 페이지를 위해 스프링 도메인 클래스인 Pageble 사용
* Request
~~~
GET /api/v2/patients?pageNo=1&pageSize=5&patientName=김형익&patientNo=123120&birthDate=19910220
parameter 
    pageNo : 페이지 넘버
    pageSize : 총페이지사이즈
    patientName : 환자명
    patientNop : 환자번호
    birthDate : 생년월일
~~~

* Response
~~~
{
  "code": "S0000",
  "body": {
    "content": [
      {
        "id": 2,
        "hospitalId": 3,
        "patientName": "김개똥0",
        "patientNo": "123120",
        "genderCode": "M",
        "genderName": "남",
        "birthDate": "19910220",
        "phoneNumber": "01111123120"
      }
    ],
    "pageable": {
      "sort": {
        "unsorted": true,
        "sorted": false,
        "empty": true
      },
      "offset": 0,
      "pageSize": 5,
      "pageNumber": 0,
      "unpaged": false,
      "paged": true
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 1,
    "size": 5,
    "number": 0,
    "sort": {
      "unsorted": true,
      "sorted": false,
      "empty": true
    },
    "first": true,
    "numberOfElements": 1,
    "empty": false
  }
}
~~~

## 2. Docs
```
http://localhost:8080/docs/index.html
```
