DELETE FROM users;

TRUNCATE TABLE users RESTART IDENTITY AND COMMIT NO CHECK;

INSERT INTO users ("EMAIL", "FIRST_NAME", "SECOND_NAME", "NICKNAME", "PASSWORD", "STATUS")
VALUES ('ivan.ivanov@mail.net', 'Ivan', 'Ivanov', 'ivan.ivanov', 'password1', 'REGISTERED'),
       ('petrov@mail.net', 'Petro', 'Petrov', 'petro314', 'password2', 'ACTIVATED');