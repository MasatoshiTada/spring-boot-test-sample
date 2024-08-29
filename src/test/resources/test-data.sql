INSERT INTO customer(first_name, last_name, mail_address, birthday) VALUES('友香', '菅井', 'ysugai@sakura.com', '1995-11-29');
INSERT INTO customer(first_name, last_name, mail_address, birthday) VALUES('久美', '佐々木', 'ksasaki@hinata.com', '1996-01-22');


-- password = "user" (Encoded with BCrypt)
INSERT INTO account(id, name, mail_address, password) VALUES(1, 'user', 'user@example.com', '$2a$10$wdVyuUaOrZTawx4Z7LvqeOUlY2k4NzhPyjHmbMEEaIePCgyUnUaPG');
-- password = "admin" (Encoded with BCrypt)
INSERT INTO account(id, name, mail_address, password) VALUES(2, 'admin', 'admin@example.com', '$2a$10$ztH0ZXBexpjNLe9oBzmbzuow8siaSdEIK9dKk9pfPgHcRHXpcyjpi');

INSERT INTO account_authority(account_id, authority_name) VALUES (1, 'ROLE_USER');
INSERT INTO account_authority(account_id, authority_name) VALUES (2, 'ROLE_USER');
INSERT INTO account_authority(account_id, authority_name) VALUES (2, 'ROLE_ADMIN');
