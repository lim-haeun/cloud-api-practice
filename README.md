# 클라우드 아키텍처 설계 과제

## 1. 프로젝트 소개

Spring Boot 기반의 팀원 정보 관리 서비스를 AWS 환경에 배포한 프로젝트입니다.  
로컬 환경과 운영 환경을 분리하고, EC2 · RDS · S3 · Parameter Store를 활용하여  
클라우드 환경에서 안정적으로 동작하도록 구성했습니다.

### 주요 기능
- 팀원 정보 저장 API
- 팀원 정보 조회 API
- 프로필 이미지 업로드 API
- Presigned URL 기반 이미지 조회 API
- Actuator 기반 상태 확인 및 정보 확인

---

## 2. 기술 스택

### Backend
- Java 17
- Spring Boot
- Spring Data JPA
- Spring Validation
- Spring Actuator

### Database
- H2 Database
- MySQL

### Infra / Cloud
- AWS EC2
- AWS RDS
- AWS S3
- AWS Systems Manager Parameter Store
- AWS IAM Role

---

## 3. 프로젝트 구조

```text
src
 ┣ main
 ┃ ┣ java
 ┃ ┃ ┗ com
 ┃ ┃    ┗ haeun
 ┃ ┃       ┗ apipractice
 ┃ ┃          ┣ global
 ┃ ┃          ┃  ┣ exception
 ┃ ┃          ┃  ┃  ┣ ErrorResponse
 ┃ ┃          ┃  ┃  ┣ GlobalExceptionHandler
 ┃ ┃          ┃  ┃  ┗ MemberNotFoundException
 ┃ ┃          ┃  ┗ logging
 ┃ ┃          ┃     ┗ LoggingFilter
 ┃ ┃          ┣ member
 ┃ ┃          ┃  ┣ controller
 ┃ ┃          ┃  ┃  ┗ MemberController
 ┃ ┃          ┃  ┣ dto
 ┃ ┃          ┃  ┃  ┣ CreateMemberRequest
 ┃ ┃          ┃  ┃  ┣ MemberResponse
 ┃ ┃          ┃  ┃  ┣ ProfileImageDownloadResponse
 ┃ ┃          ┃  ┃  ┗ ProfileImageUploadResponse
 ┃ ┃          ┃  ┣ entity
 ┃ ┃          ┃  ┃  ┗ Member
 ┃ ┃          ┃  ┣ repository
 ┃ ┃          ┃  ┃  ┗ MemberRepository
 ┃ ┃          ┃  ┗ service
 ┃ ┃          ┃     ┗ MemberService
 ┃ ┃          ┗ ApiPracticeApplication
 ┃ ┗ resources
 ┃    ┣ application.yml
 ┃    ┣ application-local.yml
 ┃    ┗ application-prod.yml
```

## 4. 과제 제출 사항

설정 완료된 AWS Budgets 화면
<img width="1307" height="401" alt="image" src="https://github.com/user-attachments/assets/a578e546-6d06-4c66-9b5d-bd4510a293fb" />

Actuator Info 엔드포인트 URL
http://43.203.245.169:8080/actuator/info

<img width="439" height="136" alt="image" src="https://github.com/user-attachments/assets/9070d9bb-739d-40e2-bcb0-3fac341bed31" />

소스(Source) 부분에 EC2의 보안 그룹 ID (sg-xxxxx)가 등록되어 있음
<img width="852" height="247" alt="image" src="https://github.com/user-attachments/assets/b256cba4-d824-4830-9a07-aef9d78812e2" />

발급받은 Presigned URL
https://haeun-aws-study-files.s3.ap-northeast-2.amazonaws.com/members/1/0d164371-f16a-492f-ab9e-8405511c5e53.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20260313T035317Z&X-Amz-SignedHeaders=host&X-Amz-Credential=AKIA3CNGQZ4YDLGXWYUK%2F20260313%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Expires=604800&X-Amz-Signature=a36274e5c09554dcfd58ffdeb72a9401415ff52794afd4f6b1e64b1ada4c8eee

Presigned URL 만료시간
"2026-03-20T03:53:17.33464312"

