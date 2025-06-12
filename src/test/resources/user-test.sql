delete from point;
delete from users;

DELETE FROM user_grade;

insert into user_grade (grade_name, required_money) values ('BASIC', 0);
insert into user_grade (grade_name, required_money) values ('ROYAL', 100000);


insert into users (user_id, user_password, user_name, user_phone_number, user_email,
                   user_birth, user_point, is_auth, user_status, last_login_at, order_money, grade_name)
values ('test', '1234','test','010-1234-5678', 'test@test.com',
        '2000-01-12', 500, false,'ACTIVE', '2000-01-12 14:35:25', 0, 'BASIC');


