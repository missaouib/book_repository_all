use users;

INSERT INTO users.roles (role)
SELECT 'USER' WHERE NOT EXISTS (SELECT * FROM users.roles)
UNION
SELECT 'ADMIN' WHERE NOT EXISTS (SELECT * FROM users.roles)



