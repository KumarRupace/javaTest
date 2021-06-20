# Customer Management Service

Functional Requirements
User stories : Crud for customer entity, please refer to the requirement spec docs for details.

Non-Functional Requirements
Able to work with Java & MVC
Able to design a sample ReST API using spring-boot
Able to write unit test and documentation

##Assumptions/Special cases

Authentication and other security factors were not considered in this exercise

Secure credentials are available as Environment variables in all non dev environments and referred in the application.yml file

Detailed error messages are not passed down to the caller.

EventSourcing/Event Driven Architecture is not implemented as this is written as a simple service though the provision for adopting it is available.

CQRS pattern was not considered at Service layer bcoz of the scope (design a sample ReST API using spring-boot)

For Dev: H2-InMemory was used for testing

## Setup/Installation

Open the POM file or project folder using IntelliJ IDEA or similar IDE for Java and run the application (The application will start listening at localhost:8080)

# Swagger API Docs

Once the application starts running, swagger ui can be viewed at http://localhost:8080/swagger-ui/index.html

# Architecture, Design Patterns, OOPS Principles used

Rest Calls, DI, Inheritance & Interfaces, Transaction, TDD, Custom Exception Handling 

# Data Flow

No separate DTO class was created due to the scope of the project
RestAPI(DTO) --> Service(Entity/DTO) --> Repository(Entity) --> H2InMemoryDB(Record) and vice version

## Usage

Running the App

You can run the app via IntelliJ or any similar IDE (Standard Springboot-Maven)

Use PostmanCanary and import the attached postman scripts (customer-management-service.postman_collection.json), run the collection under customer-management-service title

Basic health check : http://localhost:8080/actuator/health

# Automated Testing

Running the Test

All the tests (unit test and integration tests) are at ...\test\java\com\... folder

Ensure maven is installed and available at PATH (environment) and run the following command from the project root folder

Run via test class at the IDE or use mvn test

#Deployments

Profile files are available for various environments (all credentials has to be passed via environment variables for non dev)
dev test prod

Please thoroughly review application.yml and create application-prod.yml accordingly before publishing to any production environment.

Image Creation
docker build --build-arg ENVIRONMENT=local .
Running in interactive mode
docker run <image_id> -p 8080:8080 -t

# Improvements and Extensions

CQRS and Event Sourcing

Separate class for DTOs

Addition monitoring alert for failures and downtimes

Logging can be more comprehensive

Exceptions to be notified to L1 support via email or sms

Automate build and deployment management including terraform scripts(CI/CD pipeline) e.g., using Jenkins or bitbucket pipelines

Adding cache data to improve performance

Codes for Securing API

Adding more Clear and concise logs that are easy to read and parse

Selenium or similar UI based tool if the UI is in scope of the project

Create deployment pipeline scripts for Kubernetes cluster deployment

## Contributing

N.A.

## License

N.A.

# Support or Queries

email to: renjithkumar1@gmail.com