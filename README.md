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
| Backend | Spring Boot 4.0.5, Spring Data JPA, Spring Security |
| Database | Oracle DB |
| Frontend | Thymeleaf, CSS (다크/라이트 테마) |
| 기타 | Lombok, JPA Auditing, Multipart 파일 업로드, Interceptor, BCrypt |

---

## 프로젝트 구조

```
src/main/
├── java/com/example/
│   ├── CineLogApplication.java        @EnableJpaAuditing, @EnableScheduling
│   ├── auth/
│   │   └── MemberDetails.java         Security 인증 래퍼
│   ├── config/
│   │   ├── SecurityConfig.java        인증/인가/remember-me 설정
│   │   └── WebConfig.java             로그 인터셉터 등록
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
│   │   ├── Member.java
│   │   ├── Movie.java
│   │   ├── Review.java
│   │   └── Role.java                  USER / ADMIN enum
│   ├── interceptor/
│   │   ├── ActivityLogInterceptor.java  사용자 활동 기록
│   │   └── ErrorLogInterceptor.java     에러 발생 기록
│   ├── repository/
│   │   ├── MemberRepository.java
│   │   ├── MovieRepository.java
│   │   └── ReviewRepository.java
│   ├── service/
│   │   ├── MemberDetailsService.java  Security UserDetailsService 구현
│   │   ├── MemberService.java
│   │   ├── MovieService.java
│   │   └── ReviewService.java
│   └── util/
│       ├── FileService.java           파일 업로드 공통 처리
│       ├── PageHandler.java           페이지 네비게이션 계산
│       └── TokenCleanupScheduler.java 만료 토큰 정리 (매일 새벽 3시)
│
└── resources/
    ├── application.properties
    ├── data.sql
    ├── logback-spring.xml             로그 파일 설정 (logs/app.log)
    ├── static/
    │   ├── css/style.css
    │   └── js/theme.js
    └── templates/
        ├── index.html
        ├── error/
        │   ├── 403.html
        │   ├── 404.html
        │   └── 500.html
        ├── fragments/
        │   ├── genre.html
        │   ├── head.html
        │   ├── movieFormFields.html
        │   ├── pagination.html
        │   ├── scoreSelect.html
        │   └── siteHeader.html
        ├── member/
        │   ├── loginForm.html
        │   ├── myPage.html
        │   └── regForm.html
        ├── movie/
        │   ├── detail.html
        │   ├── list.html
        │   ├── modify.html
        │   └── regForm.html
        └── review/
            └── modify.html
```

---

## DB 설계

### tbl_movie_members (회원)

| 컬럼 | 타입 | 설명 |
|---|---|---|
| username | VARCHAR(20) PK | 아이디 |
| password | VARCHAR(100) | BCrypt 암호화 비밀번호 |
| nickname | VARCHAR(20) | 닉네임 |
| user_role | VARCHAR(10) | USER / ADMIN |

### tbl_movies (영화)

| 컬럼 | 타입 | 설명 |
|---|---|---|
| mno | NUMBER PK | movie_seq 시퀀스 자동 생성 |
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
| rno | NUMBER PK | review_seq 시퀀스 자동 생성 |
| score | NUMBER | 평점 1~5 |
| content | VARCHAR(500) | 한줄평 |
| movie_id | NUMBER FK | 영화 참조 |
| writer | VARCHAR(20) FK | 회원 참조 |
| reg_date | DATE | 작성일 (자동) |
| modify_date | DATE | 수정일 (자동) |

### persistent_logins (Remember-me 토큰)
SQL Developer에서 직접 생성 필요:
```sql
CREATE TABLE persistent_logins (
    username  VARCHAR2(64) NOT NULL,
    series    VARCHAR2(64) PRIMARY KEY,
    token     VARCHAR2(64) NOT NULL,
    last_used TIMESTAMP    NOT NULL
);
```

---

## URL 매핑

| URL | 메서드 | 기능 | 권한 |
|---|---|---|---|
| `/` | GET | 메인 페이지 | 누구나 |
| `/member/reg` | GET/POST | 회원가입 | 누구나 |
| `/member/login` | GET | 로그인 폼 | 누구나 |
| `/member/loginProc` | POST | 로그인 처리 (Security) | 누구나 |
| `/member/logout` | GET | 로그아웃 (Security) | 누구나 |
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
- Spring Security + BCrypt 비밀번호 암호화
- Remember-me 자동 로그인 (7일, persistent_logins 테이블)
- ADMIN / USER 역할 구분 (Role enum)
- 중복 아이디 방지, 입력값 유효성 검증 (@Valid)

### 영화 관리
- ADMIN만 등록·수정·삭제 가능
- 포스터 이미지 업로드 (UUID_원본파일명)
- 제목 / 감독 / 통합 키워드 검색
- 페이징 처리 (PageHandler)
- 4열 카드 그리드 UI (반응형)

### 리뷰 시스템
- 로그인 사용자만 작성 가능
- 본인 리뷰만 수정·삭제 가능
- 평점 1~5점 + 한줄평 (최대 200자)
- 평균 평점 @Query N+1 제거

### 로그
- ActivityLogInterceptor: 모든 요청 활동 기록
- ErrorLogInterceptor: 예외 발생 기록
- logback-spring.xml: logs/app.log, 날짜별 롤링

### UI
- 다크 / 라이트 테마 (localStorage 저장, FOUC 방지)
- 커스텀 에러 페이지 (403, 404, 500)

---

## 구현 시 고민했던 점

### Spring Security 적용
기존 `LoginInterceptor`, `AdminInterceptor`를 제거하고 `SecurityConfig`의 URL 접근 규칙으로 대체했습니다. 로그인 성공 시 `successHandler`에서 `loginUser` 세션을 직접 저장해 기존 Thymeleaf 템플릿과의 호환성을 유지했습니다.

### 로그인 후 원래 페이지 복귀
`SavedRequestAwareAuthenticationSuccessHandler`를 활용해 로그인 전에 접근하려던 URL로 자동 복귀합니다.

### 평균 평점 N+1 문제 해결
영화 목록에서 각 영화마다 개별 쿼리 대신 `@Query`로 영화 ID 목록을 한 번에 넘겨 쿼리 1번으로 처리했습니다.

### 리뷰 writer 보안 처리
폼의 hidden 필드 대신 컨트롤러에서 세션으로 직접 꺼내 설정합니다.

### Thymeleaf 프래그먼트 활용
반복되는 HTML 요소를 프래그먼트로 분리해 중복을 최소화했습니다.