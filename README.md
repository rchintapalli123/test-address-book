
### Address Book
```
AddressBook application is a Java Spring Boot Application that enables users to 
1. Create Address Books
2. View AddressBook
3. Add Customers to Address Book
4. Remove Customers from Address Book
5. List all customers of a Address Book
6. List distinct customers across all Address Books
```
#### Assumptions
```
1. AddressBook is the parent entity and Customer is child entity. Customers are always accessed through AddressBook only
2. Customer has only one Phone number. Another customer record can be created with same firstName and lastName but with different Phone Number
3. Customers are loaded eagerly when Address Book is retrieved
4. Note:- This is not a production quality code but just a demonstration of a few aspects of
   Java and Spring Boot
```
#### Tech Stack used for implementation
```
1. OpenJDK Corretto 18.0.2
2. gradle 8.2.1
3. Docker version 24.0.2
4. Spring Boot version 3.1.1
5. H2 in-memory database for persistence
6. Junit for unit testing
```

#### Steps to Run Address Book application
```
The Spring Boot Application can be executed locally by 
1. Running command - ./gradlew bootRun
2. Running the application main class - AddressBookApplication
3. Deploying application in Docker container by executing below commands at application root folder
        a) ./gradlew build 
        b) docker build -t address-book .
        c) docker run -d -p 8080:8080 --name test-addressbook address-book
    OR 
    execute run.sh file by running -  ./run.sh     
  Note:- The application jar is created in build/libs folder   
         
```
### Steps to Access Application when it is up and running    
 ```
1. The swagger-ui of the Address Book can be accessed at :-
        http://localhost:8080/swagger-ui/index.html
2. The H2 database the Address Book application can be accessed at :-
        http://localhost:8080/h2-console/
        using below values :- 
        url: jdbc:h2:mem:addressbookdb
        username: test
        password: test#123
        
    Refer to screenshots in images folder 
         
3. The Swagger endpoint provides documentation of all APIs AddressBook application exposes and sample payloads

4. The API returns 404 Response code when AdressBook is not found and for any Runtime exceptions it gives 500 internal server error



