DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS main_category;
DROP TABLE IF EXISTS sub_category;

CREATE TABLE member
(
    member_id     SERIAL PRIMARY KEY,
    email         VARCHAR(255) UNIQUE NOT NULL,
    password      VARCHAR(255)        NOT NULL,
    nickname      VARCHAR(255) UNIQUE NOT NULL,
    phone         VARCHAR(20)         NOT NULL,
    profile_image VARCHAR(255),
    provider      VARCHAR(20)         NOT NULL,
    role          VARCHAR(20)         NOT NULL,
    referral_code VARCHAR(255)
);

CREATE TABLE main_category
(
    main_category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(60) NOT NULL
);

CREATE TABLE sub_category
(
    sub_category_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(60) NOT NULL,
    main_category_id BIGINT       NOT NULL,
    FOREIGN KEY (main_category_id) REFERENCES main_category (main_category_id)
);