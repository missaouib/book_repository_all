use book_repository;

INSERT INTO book_repository.roles (role)
SELECT 'USER' WHERE NOT EXISTS (SELECT * FROM book_repository.roles)
UNION
SELECT 'ADMIN' WHERE NOT EXISTS (SELECT * FROM book_repository.roles)



