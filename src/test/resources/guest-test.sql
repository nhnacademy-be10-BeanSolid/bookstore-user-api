delete from guests;

insert into guests (guest_password, guest_name, guest_phone_number, guest_address, guest_email)
values ('12345', '홍길동', '010-1234-5678', '조선대학교', 'hong@test.com'),
       ('123456', '이순신', '010-4444-2222', '전라좌수영', 'lee@test.com'),
       ('1234567', '권율', '010-1111-3333', '행주대첩', 'Kwon@test.com');
