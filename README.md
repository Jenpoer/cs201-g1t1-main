# CS201 G1T1 Yelp Project

## Team Members

- CHUA PEI WEN REGINA
- JENNIFER POERNOMO
- MICHELLE LEONG HWEE-LING
- NAOMI OH JIA LI
- SARAH ANN HOGAN

## Problem Statement 

Finding the most commonly occuring business category in an area.

## Objectives 

1. To find out the businesses in an area defined by a rectangle of latitude and longitude
2. To find the most popular category of business in that area

## User Guide 
### Steps

1. Install Maven extension and Spring Boot extensions in IDE
2. Create a new database connection and enter all the required connection parameters of the AWS MySQL database server. 

Host: cs201db.cowijpva0ytl.us-east-1.rds.amazonaws.com
Port: 3306
DB name: cs201g1t1

3. Click on the Test Connection button to connect to the database server.
4. Run the application using the command - "mvn spring-boot:run" in your terminal
5. Test HTTP methods using browser or REST Client 

### HTTP methods 
Get the list of all the businesses residing in a particular area identified by its longiture and latitude using a kd-tree and range tree respectively:

GET http://localhost:8080/kd-tree/range-query

GET http://localhost:8080/range-tree/range-query


Find the most popular category amonst these businesses using a list and hashmap respectively:

GET http://localhost:8080/businesses/popular

GET http://localhost:8080/unique

