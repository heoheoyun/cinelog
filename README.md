CineLog — 영화 리뷰 사이트
프로젝트 개요
항목내용프로젝트명CineLog개발 형태개인 프로젝트주제영화 정보 공유 + 사용자 리뷰 사이트특징예매 없이 영화 정보 조회와 평점/한줄평에 집중

기술 스택
분류기술BackendSpring Boot, Spring Data JPADatabaseOracle DBFrontendThymeleaf, CSS (다크/라이트 테마)기타Lombok, JPA Auditing, Multipart 파일 업로드, Interceptor

프로젝트 구조
src/main/
├── java/com/example/
│   ├── Exam2Application.java         @EnableJpaAuditing
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
│   │   └── ReviewEntity.java
│   ├── interceptor/
│   │   ├── LoginInterceptor.java     비로그인 요청 차단
│   │   └── AdminInterceptor.java     비ADMIN 요청 차단
│   ├── repository/
│   │   ├── MemberRepository.java
│   │   ├── MovieRepository.java
│   │   └── ReviewRepository.java
│   ├── service/
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

DB 설계
tbl_movie_members (회원)
컬럼타입설명usernameVARCHAR(20) PK아이디passwordVARCHAR(20)비밀번호nicknameVARCHAR(20)닉네임user_roleVARCHAR(10)USER / ADMIN
tbl_movies (영화)
컬럼타입설명mnoNUMBER PK자동 생성titleVARCHAR(100)제목directorVARCHAR(50)감독genreVARCHAR(20)장르release_yearNUMBER개봉년도synopsisVARCHAR(1000)줄거리posterVARCHAR(200)포스터 파일명reg_dateDATE등록일 (자동)
tbl_reviews (리뷰)
컬럼타입설명rnoNUMBER PK자동 생성scoreNUMBER평점 1~5contentVARCHAR(500)한줄평movie_idNUMBER FK영화 참조writerVARCHAR(20) FK회원 참조reg_dateDATE작성일 (자동)modify_dateDATE수정일 (자동)

URL 매핑
URL메서드기능권한/GET메인 페이지누구나/member/regGET/POST회원가입누구나/member/loginGET/POST로그인누구나/member/logoutGET로그아웃누구나/member/mypageGET내 정보 + 내 리뷰 목록로그인/member/edit/nicknamePOST닉네임 변경로그인/member/edit/passwordPOST비밀번호 변경로그인/movie/listGET목록 + 검색 + 페이징누구나/movie/detailGET상세 + 리뷰 목록누구나/movie/regGET/POST영화 등록ADMIN/movie/modifyGET/POST영화 수정ADMIN/movie/deletePOST영화 삭제ADMIN/review/writePOST리뷰 작성로그인/review/modifyGET/POST리뷰 수정본인만/review/deletePOST리뷰 삭제본인만

주요 기능
회원 관리

회원가입 / 로그인 / 로그아웃
Spring Security 없이 HttpSession으로 직접 인증 처리
ADMIN / USER 역할 구분

내 정보 페이지

닉네임 이니셜 아바타, 역할 배지 표시
닉네임 변경 / 비밀번호 변경 (현재 비밀번호 검증)
내가 쓴 리뷰 목록 조회 및 수정·삭제

영화 관리

ADMIN만 영화 등록·수정·삭제 가능
포스터 이미지 업로드 (UUID_원본파일명 형식으로 서버 저장)
제목 / 감독 키워드 검색 (파생 쿼리 Containing)
페이징 처리 (PageHandler 유틸 클래스)
영화 목록을 4열 카드 그리드로 표시 (반응형)

리뷰 시스템

로그인한 사용자만 리뷰 작성 가능
본인 리뷰만 수정·삭제 가능
평점 1~5점 + 한줄평 (최대 200자)
평균 평점은 리뷰 목록 Stream으로 계산 (@Query 미사용)

인터셉터

LoginInterceptor — /member/mypage, /member/edit/**, /review/**
AdminInterceptor — /movie/reg, /movie/modify, /movie/delete

UI 테마

다크 / 라이트 모드 전환 (헤더 🌙☀️ 버튼)
localStorage에 선택 저장, theme.js를 CSS 전에 로드해 FOUC 방지


🔍 구현 시 고민했던 점
리뷰 writer 보안 처리
form의 hidden 필드로 writer를 받으면 사용자가 값을 임의로 조작할 수 있어 컨트롤러에서 세션으로 직접 꺼내 set하는 방식으로 변경했습니다.
javaMemberEntity loginUser = (MemberEntity) session.getAttribute("loginUser");
dto.setWriter(loginUser.getUsername());

Thymeleaf 프래그먼트 활용
반복되는 HTML 요소를 프래그먼트로 분리해 중복을 최소화했습니다.
프래그먼트적용된 곳siteHeader모든 페이지movieFormFields영화 등록 / 수정scoreSelect리뷰 작성 / 수정genre영화 등록 / 수정pagination영화 목록
