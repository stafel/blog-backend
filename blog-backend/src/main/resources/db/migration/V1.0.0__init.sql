 -- initial mariadb file for flyway
 -- according to https://mariadb.com/kb/en/string-data-types/ TEXT holds 65k characters
 -- varchar(255) could be replaced with TINYTEXT
 -- foreign keys ref https://mariadb.com/kb/en/foreign-keys/

CREATE TABLE Blog (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nickname VARCHAR(255) NOT NULL,
    description TEXT,
    logoUrl VARCHAR(255) NOT NULL
)

CREATE TABLE BlogUser (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nickname VARCHAR(255) NOT NULL,
    description TEXT,
    signupDate DATE
);

CREATE TABLE Post (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    creationDate DATE,
    CONSTRAINT `fk_author`
        FOREIGN KEY (author) REFERENCES BlogUser (id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT
);

CREATE TABLE Comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    text MEDIUMTEXT,
    CONSTRAINT `fk_post`
        FOREIGN KEY (post) REFERENCES Post (id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT
    CONSTRAINT `fk_author`
        FOREIGN KEY (author) REFERENCES BlogUser (id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT
)