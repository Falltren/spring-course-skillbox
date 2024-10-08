INSERT INTO categories (id, title)
values (100, 'Animal'),
       (101, 'Universe');

INSERT INTO users(id, name, email, password)
values (100, 'testUser', 'testuser@user.com', '12345'),
       (101, 'testAdmin', 'testadmin@admin.com', '11111');

INSERT INTO user_roles(user_id, roles)
values (100, 'ROLE_USER'),
       (101, 'ROLE_ADMIN');

INSERT INTO news (id, title, text, create_at, update_at, category_id, user_id)
values (100, 'Bird', 'The ducks have flown south', '2024-10-01 10:00:00.000000+00', null, 100, 100),
       (101, 'New black hole', 'Scientists have discovered a new black hole', '2024-10-02 10:00:00.000000+00', null, 101, 101);

INSERT INTO comments(id, text, create_at, update_at, news_id, user_id)
values (100, 'Where is my rifle', '2024-10-03 10:00:00.000000+00', null, 100, 100),
       (101, 'It is amazing!', '2024-10-03 12:00:00.000000+00', null, 101, 101),
       (102, 'Just comment', '2024-10-03 10:30:00.000000+00', null, 100, 101)