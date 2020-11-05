CREATE TABLE `customer` (
`id` VARCHAR(10),
`name` VARCHAR(30),
`date_of_birth` INT,
`gender` VARCHAR(6),
PRIMARY KEY (`id`)
);

SELECT * FROM CUSTOMER;

INSERT INTO `customer` VALUES(
'S1234567A',
'John Doe',
19901225,
'male'
);