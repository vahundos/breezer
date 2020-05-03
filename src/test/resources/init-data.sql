DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM user_pictures;

TRUNCATE TABLE users RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE user_pictures RESTART IDENTITY AND COMMIT NO CHECK;

INSERT INTO user_pictures ("PICTURE")
VALUES (''), (''), (''), ('');

INSERT INTO users ("EMAIL", "FIRST_NAME", "SECOND_NAME", "NICKNAME", "PASSWORD", "STATUS", "PICTURE_ID")
VALUES ('ivan.ivanov@mail.net', 'Ivan', 'Ivanov', 'ivanivanov', '$2a$10$mtOnUvJC3gi5aK.6kii3TucpnglGmaldQuOulmJO3LoX54RqQ8aOa', 'ACTIVATED', 1),
       ('petrov@mail.net', 'Petro', 'Petrov', 'petro314', '$2a$10$9LAnAaMOy22u35lptsXZ/ebDfbTKlRw2JUd7hKV9TZlR9a/HvVvq2', 'ACTIVATED', 2),
       ('dmytro@mail.net', 'Dmytro', 'Dmytrov', 'dima23', '$2a$10$Q76AhLaCz90jfeNXmYUS8OAl7ZOQ8FedEnIu8QG/ZBOmZQQhN0386', 'BANNED', 3),
       ('semen@mail.net', 'Semen', 'Semenov', 'semen1', '$2a$10$bWr0mRZJu0yV0l/TpZmwDeEiff6XOqHkp7M1NjCtjPMB/hChzJaY.', 'REGISTERED', 4);

INSERT INTO user_roles ("USER_ID", "ROLE")
VALUES ('1', 'USER'),
       ('1', 'ADMIN'),
       ('2', 'USER');