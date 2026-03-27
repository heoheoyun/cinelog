# CineLog — 영화 리뷰 사이트

## 프로젝트 개요

| 항목 | 내용 |
|---|---|
| 프로젝트명 | CineLog |
| 개발 형태 | 개인 프로젝트 |
| 주제 | 영화 정보 공유 + 사용자 리뷰 사이트 |
| 특징 | 예매 없이 영화 정보 조회와 평점/한줄평에 집중 |

---

## 기술 스택

| 분류 | 기술 |
|---|---|
| Backend | Spring Boot, Spring Data JPA |
| Database | Oracle DB |
| Frontend | Thymeleaf, CSS (다크/라이트 테마) |
| 기타 | Lombok, JPA Auditing, Multipart 파일 업로드, Interceptor |

---

## 프로젝트 구조

```
src/main/
├── java/com/example/
│   ├── CineLogApplication.java       @EnableJpaAuditing
│   ├── config/
│   │   └── WebConfig.java            인터셉터 URL 패턴 등록
│   ├── controller/
│   │   ├── MainController.java
│   │   ├── MemberController.java
│   │   ├── MovieController.java
│   │   └── ReviewController.java
│   ├── dto/
│   │   ├── MemberDto.java
│   │   ├── MovieDto.java
│   │   └── ReviewDto.java
│   ├── entity/
│   │   ├── MemberEntity.java
│   │   ├── MovieEntity.java
│   │   ├── ReviewEntity.java
│   │   └── Role.java                 USER / ADMIN enum
│   ├── exception/
│   │   ├── EntityNotFoundException.java
│   │   ├── FileUploadException.java
│   │   └── UnauthorizedException.java
│   ├── interceptor/
│   │   ├── LoginInterceptor.java     비로그인 요청 차단 (redirect URL 보존)
│   │   └── AdminInterceptor.java     비ADMIN 요청 차단
│   ├── repository/
│   │   ├── MemberRepository.java
│   │   ├── MovieRepository.java
│   │   └── ReviewRepository.java
│   ├── service/
│   │   ├── FileService.java          파일 업로드 공통 처리
│   │   ├── MemberService.java
│   │   ├── MovieService.java
│   │   └── ReviewService.java
│   └── util/
│       └── PageHandler.java
│
└── resources/
    ├── application.properties
    ├── data.sql
    ├── static/
    │   ├── css/style.css
    │   └── js/theme.js
    └── templates/
        ├── index.html
        ├── fragments/
        │   ├── head.html             CSS + theme.js 로드
        │   ├── siteHeader.html       헤더 (nav 포함 / 미포함 두 가지)
        │   ├── genre.html            장르 select options
        │   ├── scoreSelect.html      평점 select options
        │   ├── movieFormFields.html  영화 등록/수정 공통 폼 필드
        │   └── pagination.html       페이지 내비게이션
        ├── member/
        │   ├── loginForm.html
        │   ├── regForm.html
        │   └── myPage.html
        ├── movie/
        │   ├── list.html
        │   ├── detail.html
        │   ├── regForm.html
        │   └── modify.html
        └── review/
            └── modify.html
```

---

## DB 설계

### tbl_movie_members (회원)

| 컬럼 | 타입 | 설명 |
|---|---|---|
| username | VARCHAR(20) PK | 아이디 |
| password | VARCHAR(20) | 비밀번호 |
| nickname | VARCHAR(20) | 닉네임 |
| user_role | VARCHAR(10) | USER / ADMIN (Role enum) |

### tbl_movies (영화)

| 컬럼 | 타입 | 설명 |
|---|---|---|
| mno | NUMBER PK | movie_seq 시퀀스로 자동 생성 |
| title | VARCHAR(100) | 제목 |
| director | VARCHAR(50) | 감독 |
| genre | VARCHAR(20) | 장르 |
| release_year | NUMBER | 개봉년도 |
| synopsis | VARCHAR(1000) | 줄거리 |
| poster | VARCHAR(200) | 포스터 파일명 |
| reg_date | DATE | 등록일 (자동) |

### tbl_reviews (리뷰)

| 컬럼 | 타입 | 설명 |
|---|---|---|
| rno | NUMBER PK | review_seq 시퀀스로 자동 생성 |
| score | NUMBER | 평점 1~5 |
| content | VARCHAR(500) | 한줄평 |
| movie_id | NUMBER FK | 영화 참조 |
| writer | VARCHAR(20) FK | 회원 참조 |
| reg_date | DATE | 작성일 (자동) |
| modify_date | DATE | 수정일 (자동) |

---

## URL 매핑

| URL | 메서드 | 기능 | 권한 |
|---|---|---|---|
| `/` | GET | 메인 페이지 | 누구나 |
| `/member/reg` | GET/POST | 회원가입 | 누구나 |
| `/member/login` | GET/POST | 로그인 | 누구나 |
| `/member/logout` | GET | 로그아웃 | 누구나 |
| `/member/mypage` | GET | 내 정보 + 내 리뷰 목록 | 로그인 |
| `/member/edit/nickname` | POST | 닉네임 변경 | 로그인 |
| `/member/edit/password` | POST | 비밀번호 변경 | 로그인 |
| `/movie/list` | GET | 목록 + 검색 + 페이징 | 누구나 |
| `/movie/detail` | GET | 상세 + 리뷰 목록 | 누구나 |
| `/movie/reg` | GET/POST | 영화 등록 | ADMIN |
| `/movie/modify` | GET/POST | 영화 수정 | ADMIN |
| `/movie/delete` | POST | 영화 삭제 | ADMIN |
| `/review/write` | POST | 리뷰 작성 | 로그인 |
| `/review/modify` | GET/POST | 리뷰 수정 | 본인만 |
| `/review/delete` | POST | 리뷰 삭제 | 본인만 |

---

## 주요 기능

### 회원 관리
- 회원가입 / 로그인 / 로그아웃
- Spring Security 없이 `HttpSession`으로 직접 인증 처리
- ADMIN / USER 역할 구분 (`Role` enum)
- 중복 아이디 가입 방지

### 내 정보 페이지
- 닉네임 이니셜 아바타, 역할 배지 표시
- 닉네임 변경 / 비밀번호 변경 (현재 비밀번호 검증)
- 내가 쓴 리뷰 목록 조회 및 수정·삭제

### 영화 관리
- ADMIN만 영화 등록·수정·삭제 가능
- 포스터 이미지 업로드 (`FileService`, `UUID_원본파일명` 형식으로 서버 저장)
- 제목 / 감독 / 통합 키워드 검색 (파생 쿼리 + JPQL `@Query`)
- 페이징 처리 (`PageHandler` 유틸 클래스)
- 영화 목록을 4열 카드 그리드로 표시 (반응형)

### 리뷰 시스템
- 로그인한 사용자만 리뷰 작성 가능
- 본인 리뷰만 수정·삭제 가능
- 평점 1~5점 + 한줄평 (최대 200자)
- 평균 평점은 `@Query`로 DB에서 직접 계산 (N+1 제거)
- 마이페이지에서 수정·삭제 시 마이페이지로 복귀 (`from` 파라미터)

### 인터셉터
- `LoginInterceptor` — `/member/mypage`, `/member/edit/**`, `/review/**` 차단, 로그인 후 원래 페이지로 자동 복귀
- `AdminInterceptor` — `/movie/reg`, `/movie/modify`, `/movie/delete` 차단

### UI 테마
- 다크 / 라이트 모드 전환 (헤더 🌙☀️ 버튼)
- `localStorage`에 선택 저장, `theme.js`를 CSS 전에 로드해 FOUC 방지

### 로그인 후 원래 페이지 복귀

`LoginInterceptor`에서 접근 URL을 인코딩해 `redirect` 파라미터로 전달하고, 로그인 성공 시 디코딩해서 원래 페이지로 이동합니다.

```java
String encoded = URLEncoder.encode(redirectUrl, StandardCharsets.UTF_8);
response.sendRedirect("/member/login?redirect=" + encoded);
```

### Thymeleaf 프래그먼트 활용

반복되는 HTML 요소를 프래그먼트로 분리해 중복을 최소화했습니다.

| 프래그먼트 | 적용된 곳 |
|---|---|
| `siteHeader` | 모든 페이지 |
| `movieFormFields` | 영화 등록 / 수정 |
| `scoreSelect` | 리뷰 작성 / 수정 |
| `genre` | 영화 등록 / 수정 |
| `pagination` | 영화 목록 |