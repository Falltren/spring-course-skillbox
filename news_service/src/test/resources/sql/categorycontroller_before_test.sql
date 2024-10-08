INSERT INTO categories (id, title)
values (100, 'Animal'),
       (101, 'Universe');

INSERT INTO users(id, name, email, password)
values (100, 'testUser', 'testuser@user.com', '12345'),
       (101, 'testAdmin', 'testadmin@admin.com', '11111');

INSERT INTO user_roles(user_id, roles)
values (100, 'ROLE_USER'),
       (101, 'ROLE_ADMIN');