DROP TABLE IF EXISTS CUSTOMER;

CREATE TABLE CUSTOMER (
  id INT AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL
);

INSERT INTO CUSTOMER (first_name, last_name) VALUES
  ('Phil', 'Dunphy'),
  ('Claire', 'Dunphy'),
  ('Jay', 'Prichett'),
  ('Mitchell', 'Prichett'),
  ('Gloria', 'Delgado');