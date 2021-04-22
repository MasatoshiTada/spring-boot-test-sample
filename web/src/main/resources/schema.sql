DROP TABLE IF EXISTS account_authority;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS customer;

CREATE TABLE customer (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  first_name VARCHAR(32) NOT NULL,
  last_name VARCHAR(32) NOT NULL,
  email VARCHAR(128) NOT NULL,
  birthday DATE NOT NULL
);

CREATE TABLE account (
  id INTEGER PRIMARY KEY,
  name VARCHAR(64) NOT NULL,
  email VARCHAR(128) UNIQUE NOT NULL,
  password VARCHAR(256) NOT NULL
);

CREATE TABLE account_authority (
  account_id INTEGER REFERENCES account(id),
  authority_name VARCHAR(32),
  CONSTRAINT fk_account_id FOREIGN KEY (account_id) REFERENCES account(id),
  PRIMARY KEY (account_id, authority_name)
);
