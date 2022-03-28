insert into users(id, user_name, email, pass, first_name, sur_name, created_date, enabled, verification_code)
    values (1, 'administrator', 'admin@nngasu.ru',
            '$2a$12$a8MWJgLXR1d0idwiftAO4.IeN0/iuM2YV7grESuNol2zPchQHVipm',
            'admin', 'adminov', '1000-01-01 00:00:00.000000', true, '1111111111111111');

insert into user_roles(user_id, roles)
    values (1, 'ROLE_ADMIN')