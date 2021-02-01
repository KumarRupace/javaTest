DROP TABLE IF EXISTS Customer;

CREATE TABLE Customer (
  id BIGINT AUTO_INCREMENT  PRIMARY KEY,
  firstName VARCHAR(250) NOT NULL,
  lastName VARCHAR(250) NOT NULL
);

INSERT INTO Customer (firstName, lastName) VALUES ('Charu', 'Bhatt');