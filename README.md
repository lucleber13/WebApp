#WebApp

Web Application for Car Dealers process management

##Installation

Maven is required to build the project. The project is built using the following command:

##Clone the repository
Run the following command in the root directory of the project:
mvn clean install

##Running the application

The application can be run using the following command:

mvn spring-boot:run

The application will be running on the following URL:

http://localhost:8080/

##API Documentation

The API documentation can be accessed using the following URL:

http://localhost:8080/api/v1/swagger-ui/index.html#/

##Database

The application uses an PostgreSQL database. The database configuration can be found in the application.yaml file. Please make sure to create the database before running the application. The database schema is created automatically by the application. Enter your database credentials in the application.yaml file.

##Authentication
