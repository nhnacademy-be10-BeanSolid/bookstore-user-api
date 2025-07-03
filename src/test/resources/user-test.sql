delete from cart;
delete from point;
delete from point_type;
delete from address;
delete from review;
delete from users;
delete from user_grade;

ALTER TABLE users ALTER COLUMN user_no RESTART WITH 2;

INSERT INTO user_grade(grade_name, required_money)
values ('BASIC', 500),
       ('ROYAL', 750),
       ('GOLD', 1000);

ALTER TABLE point_type ALTER COLUMN type_id RESTART WITH 1;

INSERT INTO point_type (type_name, earning_point, earning_rate, grade_name)
VALUES ('회원가입', 1000, 10, 'BASIC'),
       ('리뷰작성', 300, 5, 'GOLD');

insert into users (user_no, user_id, user_password, user_name, user_phone_number, user_email,
                   user_birth, user_point, is_auth, user_status, created_at, last_login_at, grade_name)
values (1, 'test', '1234','test','010-1234-5678', 'test@test.com',
        '2000-01-12', 500, false,'ACTIVE', '2000-01-12 14:35:25', '2012-04-12 14:35:25', 'BASIC');


