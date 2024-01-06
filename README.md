<div align="center">
    <img src="https://download.logo.wine/logo/Spring_Framework/Spring_Framework-Logo.wine.png" width="300" alt="spring boot" />
</div>

In this project the integration is carried out with the Google Calendar API, with each of its events, **List**, **Create**, **Edit**, **Delete**# googleCalendarEvents

# Technology
- Java
- Maven
- Google Cloud
- Google Calendar API
- Spring Boot
- H2 Database
- JPA

# Requirements
- Java 17
- maven 3.9
- Google Cloud credentials

# Installation

- For data persistence, the *H2 database* with files was used, remember that you can have any **database manager** to enter our database we go to the following **url**
>```http request
>GET http://localhost:8080/h2-ui
>```

- Now we proceed to log in and execute the following **script**

>```sql
>-- CREATE TABLE USERS
>CREATE TABLE users (
>   id INT NOT NULL AUTO_INCREMENT,
>   name VARCHAR(250) NOT NULL,
>   email VARCHAR(250) NOT NULL ,
>   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
>   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
>);
>
>-- CREATE TABLE USERS TOKENS 
>CREATE TABLE user_tokens (
>   id INT NOT NULL AUTO_INCREMENT,
>   user_id INT NOT NULL,
>   token TEXT NOT NULL,
>   refresh_token TEXT NOT NULL,
>   expiry_time_seconds INT NOT NULL,
>   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
>updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
>);
>
>-- INSERT DATA TABLE USERS
>INSERT INTO users (name, email) values ('aftervery', 'aftervery@osukarusof.com');
>
>```
- If you're using Maven, then edit your project's pom.xml and add this to the <dependencies> section:
>```xml
><dependency>
>   <groupId>com.google.api-client</groupId>
>   <artifactId>google-api-client</artifactId>
>   <version>2.2.0</version>
></dependency>
>
><dependency>
>   <groupId>com.google.apis</groupId>
>   <artifactId>google-api-services-calendar</artifactId>
>   <version>v3-rev20220715-2.0.0</version>
></dependency>
>```

- The following settings must be added in our **application.properties** file
>```properties
># GOOGLE CALENDAR configuration
>google.calendar.credentials.file.path= # You must enter the location of the credentials
>google.calendar.redirect.url= # Place the redirect url that I placed in the google clound configuration
>```
