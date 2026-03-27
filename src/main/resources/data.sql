-- ── 회원 ──
-- 테이블명은 tbl_movie_members, 컬럼명은 user_role 사용
INSERT INTO tbl_movie_members (username, password, nickname, user_role) VALUES ('admin', '1234', '관리자', 'ADMIN');
INSERT INTO tbl_movie_members (username, password, nickname, user_role) VALUES ('user1', '1234', '김철수', 'USER');
INSERT INTO tbl_movie_members (username, password, nickname, user_role) VALUES ('user2', '1234', '이영희', 'USER');
INSERT INTO tbl_movie_members (username, password, nickname, user_role) VALUES ('user3', '1234', '박민준', 'USER');
-- ── 영화 ──
INSERT INTO tbl_movies (mno, title, director, genre, release_year, synopsis, poster, reg_date) VALUES (1, '인터스텔라', '크리스토퍼 놀란', 'SF', 2014, '우주를 통해 인류의 생존을 찾는 탐험을 그린 SF 대작', null, SYSDATE);
INSERT INTO tbl_movies (mno, title, director, genre, release_year, synopsis, poster, reg_date) VALUES (2, '기생충', '봉준호', '드라마', 2019, '두 가족의 만남으로 시작되는 계층 갈등을 그린 영화', null, SYSDATE);
INSERT INTO tbl_movies (mno, title, director, genre, release_year, synopsis, poster, reg_date) VALUES (3, '범죄도시', '강윤성', '액션', 2017, '괴물 형사 마석도의 범죄 소탕 작전', null, SYSDATE);
INSERT INTO tbl_movies (mno, title, director, genre, release_year, synopsis, poster, reg_date) VALUES (4, '너의 이름은', '신카이 마코토', '애니메이션', 2016, '두 남녀가 꿈속에서 몸이 바뀌는 신비로운 이야기', null, SYSDATE);
INSERT INTO tbl_movies (mno, title, director, genre, release_year, synopsis, poster, reg_date) VALUES (5, '어바웃 타임', '리처드 커티스', '로맨스', 2013, '시간 여행 능력을 가진 남자의 사랑과 삶 이야기', null, SYSDATE);

-- ── 리뷰 ──
INSERT INTO tbl_reviews (rno, score, content, movie_id, writer, reg_date, modify_date) VALUES (1, 5, '인생 영화입니다. 우주 장면이 압도적이에요.', 1, 'user1', SYSDATE, SYSDATE);
INSERT INTO tbl_reviews (rno, score, content, movie_id, writer, reg_date, modify_date) VALUES (2, 4, '스토리가 복잡하지만 볼수록 매력있어요.', 1, 'user2', SYSDATE, SYSDATE);
INSERT INTO tbl_reviews (rno, score, content, movie_id, writer, reg_date, modify_date) VALUES (3, 5, '놀란 감독의 최고작이라고 생각합니다.', 1, 'user3', SYSDATE, SYSDATE);
INSERT INTO tbl_reviews (rno, score, content, movie_id, writer, reg_date, modify_date) VALUES (4, 5, '칸 황금종려상 수상작. 완벽한 영화입니다.', 2, 'user1', SYSDATE, SYSDATE);
INSERT INTO tbl_reviews (rno, score, content, movie_id, writer, reg_date, modify_date) VALUES (5, 4, '결말이 충격적이었어요. 강추!', 2, 'user3', SYSDATE, SYSDATE);
INSERT INTO tbl_reviews (rno, score, content, movie_id, writer, reg_date, modify_date) VALUES (6, 5, '마동석 형님 믿고 보는 영화.', 3, 'user2', SYSDATE, SYSDATE);
INSERT INTO tbl_reviews (rno, score, content, movie_id, writer, reg_date, modify_date) VALUES (7, 4, '통쾌하고 재미있어요.', 3, 'user1', SYSDATE, SYSDATE);
INSERT INTO tbl_reviews (rno, score, content, movie_id, writer, reg_date, modify_date) VALUES (8, 5, '눈물 펑펑 흘렸습니다. 명작이에요.', 4, 'user2', SYSDATE, SYSDATE);
INSERT INTO tbl_reviews (rno, score, content, movie_id, writer, reg_date, modify_date) VALUES (9, 5, 'OST도 너무 좋아요. 반복 감상 중입니다.', 4, 'user3', SYSDATE, SYSDATE);
INSERT INTO tbl_reviews (rno, score, content, movie_id, writer, reg_date, modify_date) VALUES (10, 4, '따뜻하고 감동적인 영화입니다.', 5, 'user1', SYSDATE, SYSDATE);