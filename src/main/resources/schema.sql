DROP TABLE IF EXISTS account_authority;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS customer;
DROP SEQUENCE IF EXISTS seq_customer_id;

CREATE SEQUENCE seq_customer_id START WITH 1 INCREMENT BY 1 NO CYCLE;

CREATE TABLE customer (
  id INTEGER PRIMARY KEY DEFAULT nextval('seq_customer_id'),
  first_name VARCHAR(32) NOT NULL,
  last_name VARCHAR(32) NOT NULL,
  mail_address VARCHAR(128) NOT NULL,
  birthday DATE NOT NULL
);

CREATE TABLE account (
  id INTEGER PRIMARY KEY,
  name VARCHAR(64) NOT NULL,
  mail_address VARCHAR(128) UNIQUE NOT NULL,
  password VARCHAR(256) NOT NULL
);

CREATE TABLE account_authority (
  account_id INTEGER REFERENCES account(id),
  authority_name VARCHAR(32),
  CONSTRAINT fk_account_id FOREIGN KEY (account_id) REFERENCES account(id),
  PRIMARY KEY (account_id, authority_name)
);
