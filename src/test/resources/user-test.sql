delete from cart;
delete from point;
delete from point_type;
delete from address;
delete from review;
delete from users;
delete from user_grade;

INSERT INTO user_grade(grade_name, required_money)
values ('BASIC', 500),
       ('ROYAL', 750),
       ('GOLD', 1000);

insert into users (user_id, user_password, user_name, user_phone_number, user_email,
                   user_birth, user_point, is_auth, user_status, last_login_at, grade_name)
values ('test', '1234','test','010-1234-5678', 'test@test.com',
        '2000-01-12', 500, false,'ACTIVE', '2000-01-12 14:35:25', 'BASIC');


