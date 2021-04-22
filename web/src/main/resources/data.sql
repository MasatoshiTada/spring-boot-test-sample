INSERT INTO customer(first_name, last_name, email, birthday) VALUES('友香', '菅井', 'ysugai@sakura.com', '1995-11-29');
INSERT INTO customer(first_name, last_name, email, birthday) VALUES('久美', '佐々木', 'ksasaki@hinata.com', '1996-01-22');
INSERT INTO customer(first_name, last_name, email, birthday) VALUES('美玖', '金村', 'mkanemura@hinata.com', '2002-09-10');
INSERT INTO customer(first_name, last_name, email, birthday) VALUES('夏鈴', '藤吉', 'kfujiyoshi@sakura.com', '2001-08-29');
INSERT INTO customer(first_name, last_name, email, birthday) VALUES('さくら', '遠藤', 'sendo@nogi.com', '2001-10-03');


-- password = "user" (Encoded with BCrypt)
INSERT INTO account(id, name, email, password) VALUES(1, 'user', 'user@example.com', '$2a$10$wdVyuUaOrZTawx4Z7LvqeOUlY2k4NzhPyjHmbMEEaIePCgyUnUaPG');
-- password = "admin" (Encoded with BCrypt)
INSERT INTO account(id, name, email, password) VALUES(2, 'admin', 'admin@example.com', '$2a$10$ztH0ZXBexpjNLe9oBzmbzuow8siaSdEIK9dKk9pfPgHcRHXpcyjpi');

INSERT INTO account_authority(account_id, authority_name) VALUES (1, 'ROLE_USER');
INSERT INTO account_authority(account_id, authority_name) VALUES (2, 'ROLE_ADMIN');
