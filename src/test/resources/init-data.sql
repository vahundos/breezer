DELETE FROM user_roles;
DELETE FROM users;

TRUNCATE TABLE users RESTART IDENTITY AND COMMIT NO CHECK;

INSERT INTO users ("EMAIL", "FIRST_NAME", "SECOND_NAME", "NICKNAME", "PASSWORD", "STATUS")
VALUES ('ivan.ivanov@mail.net', 'Ivan', 'Ivanov', 'ivanivanov', '$2a$10$mtOnUvJC3gi5aK.6kii3TucpnglGmaldQuOulmJO3LoX54RqQ8aOa', 'ACTIVATED'),
       ('petrov@mail.net', 'Petro', 'Petrov', 'petro314', '$2a$10$9LAnAaMOy22u35lptsXZ/ebDfbTKlRw2JUd7hKV9TZlR9a/HvVvq2', 'ACTIVATED'),
       ('dmytro@mail.net', 'Dmytro', 'Dmytrov', 'dima23', '$2a$10$Q76AhLaCz90jfeNXmYUS8OAl7ZOQ8FedEnIu8QG/ZBOmZQQhN0386', 'BANNED'),
       ('semen@mail.net', 'Semen', 'Semenov', 'semen1', '$2a$10$bWr0mRZJu0yV0l/TpZmwDeEiff6XOqHkp7M1NjCtjPMB/hChzJaY.', 'REGISTERED');

INSERT INTO user_roles ("USER_ID", "ROLE")
VALUES ('1', 'USER'),
       ('1', 'ADMIN'),
       ('2', 'USER');