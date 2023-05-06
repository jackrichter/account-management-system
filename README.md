# Account Management System
A Demo application simulating an account management system for a savings account in Bank,
where typical activities such as, accounts and trcreations creation, deposit, withdrawal and so on, 
are exposed through endpoints os a REST API.

## Design
This demo has been coded as a single atomic microservice whitch in reality wold be part of, and communicate with,
other microservices conserning customer services supplied by a bank, such as loans, creditcards and so on.
Some typical microservice design patterns such as Gateway, Load-balancer and more, has been left out due to lack of need
and time when only one microservice is involved.

## Domain
The choise of domain bounderies has been decided through the task description itself (considered as the business experts)
and common knowladge of the main tasks that a savings account inails.

## Tools and Technologies
1. For the coding tool, IntelliJ 2023.1 was used.
2. Versions used: Java 11.0.18, Spring Boot 2.7.11 and Maven 3.8.7
3. Database: MSSQL 8.0
4. Git
5. Postman as a Rest-client for performing endpoint access.

## Data
As data storage MYSQL 8 was used, as opposed to static data coded in the application.
there are two SQL-script files supplied for setting up the initial state of the two tables: 'savings_accounts' and 'transactions'.
These files are 'accounts-data.sql and 'transactions-data.sql'. Both found under the src/main/resource folder.
### Database connection details
1. Database name: bank_db
2. User: michael
3. Password: mysql@123
