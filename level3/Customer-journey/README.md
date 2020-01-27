# Java Assessment (Customer Journey)

### Assessment Objective

1. Able to work with Java &  MVC
2. Able to design a sample ReST API using spring-boot
3. Able to write unit test and documentation

> **Task**

| Seq | Class Name|  To Do|
|------ | ------ | -----|
|1| CustomerRepoService.java | write/call  JPA methods to query DB|
|2| CustomerController.java | Implement PUT, DELETE & POST  to update, remove and create customer entity respectively.|
|3| CustomerRepoServiceTest.java|Create and write unit test cases for CustomerRepoServiceTest.java class|
|4| CustomerControllerTest.java|Create and write unit test cases for CustomerController.java class|

> Assumptions 
```
1. You can use H2 DB for your project
2. Entity defined is equivalent to your table (no modification required)
3. Autoload SQL file is permitted to put sample values
4. You can include any opensource library if you need to
5. Writing negative test cases is good to have
```
|You can| You cannot|
|------|------|
|You can search on web| Do not copy & paste web example |
| Understand the concept and answer | Don't get it done by other person |



## Assessment Solution (Prithvi Kumar):

Since I created this project from stratch, I have used Maven.
Please let me know if I need to provide a gradle project.

1.Following dependencies have been used in this project:
JpaRepository for Repository management.
Lombok for providing getter, setter and other functionality like hash and equals.
Spring Web - To provide a web server
H2 Database - In memory database

2.All the REST Apis can be found using the swagger link below:
http://localhost:8080/swagger-ui.html

3.CustomerController:
This is a Rest Controller. All the links can be found using swagger.

4.CustomerRepoService:
This provides all the business logic for various user requests.

5.CustomerRepo:
This extends JpaRepository. Customer is an entity.
Added additional methods to find by first and last name. 

6.CustomerControllerTest:
All the unit tests for CustomerController are written here. This uses mockito extensively.

7.CustomerControllerTest:
All the unit tests for CustomerController are written here. This uses mockito extensively.

7.CustomerResponseEntityExceptionHandler:
Manages all the custom exceptions (at runtime)
