DELETE FROM user_roles;
DELETE FROM users;

TRUNCATE TABLE users RESTART IDENTITY AND COMMIT NO CHECK;

INSERT INTO users ("EMAIL", "FIRST_NAME", "SECOND_NAME", "NICKNAME", "PASSWORD", "STATUS")
VALUES ('ivan.ivanov@mail.net', 'Ivan', 'Ivanov', 'ivanivanov', '{noop}password1', 'ACTIVATED'),
       ('petrov@mail.net', 'Petro', 'Petrov', 'petro314', '{noop}password2', 'ACTIVATED');

INSERT INTO user_roles ("USER_ID", "ROLE")
VALUES ('1', 'USER'),
       ('1', 'ADMIN'),
       ('2', 'USER');