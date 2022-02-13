INSERT INTO role (id, name)
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN');

INSERT INTO user (id, age, email, last_name, username, password)
VALUES (1, 18, 'admin', 'admin', 'admin', '$2a$12$gichxirQYd/rS9yhMcLRTuV6mcH9gErFBRQiLL3A8J2sH2UWCS8LS');
INSERT INTO user (id, age,email, last_name, username, password)
VALUES (2, 18, 'user', 'user', 'user', '$2a$12$zj5n/AKdgHfsAm6ZGKzidO05njJDzxecoHmE581bZGk3qHrrvZVoe');

INSERT INTO user_roles
VALUES (1, 2),
       (2, 1);
