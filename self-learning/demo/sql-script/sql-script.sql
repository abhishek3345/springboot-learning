USE spring_demo;

-- Drop tables if they exist
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS `user`;

-- Create USER table
CREATE TABLE `user` (
    userid INT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL
);

-- Create USER_ROLE table
CREATE TABLE user_role (
    userid INT,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (userid, role),
    CONSTRAINT fk_user
        FOREIGN KEY (userid) REFERENCES `user`(userid)
        ON DELETE CASCADE
);

-- Insert data into USER table
INSERT INTO `user` (userid, username, password) VALUES
(1, 'jack', 'pass_word'),
(2, 'bob', 'pass_word'),
(3, 'apple', 'pass_word'),
(4, 'glaxo', 'pass_word');

-- Insert data into USER_ROLE table
INSERT INTO user_role (userid, role) VALUES
(1, 'CONSUMER'),
(2, 'CONSUMER'),
(3, 'SELLER'),
(4, 'SELLER');