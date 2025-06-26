-- user-api 테이블 생성 코드

CREATE TABLE `user_grade` (
    `grade_name`	VARCHAR(10)	NOT NULL	COMMENT '일반, 로얄, 골드, 플래티넘 등등',
    `required_money`	BIGINT	NOT NULL,

    primary key (grade_name)
);

CREATE TABLE `users` (
    `user_no`	BIGINT	NOT NULL AUTO_INCREMENT,
    `user_id`	VARCHAR(20)	UNIQUE NOT NULL,
    `user_password`	VARCHAR(255)    NULL,
    `provider`	VARCHAR(255)	NULL,
    `provider_id`	BIGINT	NULL,
    `user_name`	VARCHAR(20)	NULL,
    `user_phone_number`	VARCHAR(15)	NULL,
    `user_email`	VARCHAR(50)	NULL,
    `user_birth`	DATE	NULL,
    `user_point`	INT	NOT NULL,
    `is_auth`	BOOLEAN	NOT NULL	        COMMENT '일반회원, 관리자',
    `user_status`	VARCHAR(10)	NOT NULL	COMMENT 'enum(활성화, 휴면, 탈퇴)',
    `last_login_at`	DATETIME	NOT NULL,
    `grade_name`	VARCHAR(10)	NOT NULL    COMMENT '일반, 로얄, 골드, 플래티넘 등등',

    PRIMARY KEY (user_no),
    foreign key (grade_name) references user_grade(grade_name)
);


CREATE TABLE `address` (
    `address_id`	BIGINT	NOT NULL AUTO_INCREMENT,
    `address_nick_name`	VARCHAR(30)	NOT NULL,
    `address_Detail`	VARCHAR(255)	NOT NULL,
    `user_id`	VARCHAR(20)	NOT NULL,

    primary key (address_id),
    foreign key (user_id) references users(user_id),
    UNIQUE KEY uq_user_address (user_id, address_detail)
);


CREATE TABLE `point_type` (
    `type_id`	BIGINT	NOT NULL AUTO_INCREMENT,
    `type_name`	VARCHAR(20)	NOT NULL	COMMENT '회원가입, 리뷰작성, 도서 구매, 3개월 이내 순수 주문금액',
    `earning_point`	BIGINT	NULL	    COMMENT '회원가입 : 5000, 리뷰작성 : 500',
    `earning_rate`	INT	NULL	        COMMENT '등급에 따른 적립률',
    `grade_name`	VARCHAR(10)	NULL	COMMENT '일반, 로얄, 골드, 플래티넘 등등',

    primary key (type_id),
    foreign key (grade_name) references user_grade(grade_name),

    CHECK (earning_point IS NOT NULL OR earning_rate IS NOT NULL)
);


CREATE TABLE `guests` (
    `guest_id`	BIGINT	NOT NULL AUTO_INCREMENT,
    `guest_password`	VARCHAR(255)	NOT NULL,
    `guest_name`	VARCHAR(20)	NOT NULL,
    `guest_phone_number`	VARCHAR(15)	NOT NULL,
    `guest_address`	VARCHAR(255)	NOT NULL,
    `guest_email`	VARCHAR(255)	NOT NULL UNIQUE,

    primary key (guest_id)
);

CREATE TABLE `cart` (
    `cart_id`	BIGINT	NOT NULL AUTO_INCREMENT,
    `book_id`	BIGINT	NOT NULL,
    `user_id`	VARCHAR(20)	NOT NULL,
    `quantity`	INTEGER	NOT NULL,

    primary key (cart_id),
    foreign key (book_id) references book(book_id),
    foreign key (user_id) references users(user_id)
);

CREATE TABLE `review` (
    `review_id`	BIGINT	NOT NULL AUTO_INCREMENT,
    `book_id`	BIGINT	NOT NULL,
    `user_id`	VARCHAR(20)	NOT NULL,
    `evaluation_score`	INTEGER	NOT NULL	COMMENT '1점~5점',
    `review_content`	VARCHAR(255)	NOT NULL,
    `review_photo`	VARCHAR(255)	NULL,
    `reviewed_at`	DATETIME	NOT NULL,
    `updated_at`	DATETIME	NULL,

    primary key (review_id),
    FOREIGN KEY (book_id) references book(book_id),
    foreign key (user_id) references users(user_id)
);

CREATE TABLE `point` (
    `point_id`	BIGINT	NOT NULL AUTO_INCREMENT,
    `user_id`	VARCHAR(20)	NOT NULL,
    `type_id`	BIGINT	NOT NULL,
    `payment_id`	BIGINT	NULL,
    `earned_and_used_at`	DATETIME	NOT NULL,
    `earned_and_used_point`	BIGINT	NOT NULL,

    primary key (point_id),
    foreign key (user_id) references users(user_id),
    foreign key (type_id) references point_type(type_id)
);