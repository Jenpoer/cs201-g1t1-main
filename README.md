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
2. Create a new database connection on MySQL Workbench and enter all the required connection parameters of the AWS MySQL database server.

Host: cs201db.cowijpva0ytl.us-east-1.rds.amazonaws.com<br/>
Port: 3306<br/>
DB name: cs201g1t1

3. Click on the Test Connection button to connect to the database server.
4. Run the application using the command - "mvn spring-boot:run" in your terminal

**For User Interface**
<br/>
See README.md of cs201-g1t1-ui repository

## HTTP Methods

### KD-Tree

Specified in: com.cs201.g1t1.spatial.kdtree.KDTreeController

GET /kd-tree/range-query

- Ranged query test for KD-tree with hard-coded city and rectangle, used for testing purposes
- Returns: list of businesses within query rectangle

GET /kd-tree/simple-test

- Simple ranged query test for KD-tree with hard-coded rectangle and set of points, used for testing purposes
- Returns: list of points within query rectangle

### Range Tree

Specified in: com.cs201.g1t1.spatial.kdtree.RangeTreeController

GET /range-tree/range-query

- Ranged query test for range-tree with hard-coded city and rectangle, used for testing purposes
- Returns: list of businesses within query rectangle

GET /range-tree/simple

- Simple ranged query test for range-tree with hard-coded rectangle and set of points, used for testing purposes
- Returns: list of points within query rectangle

### List

Specified in: com.cs201.g1t1.list.ListController

GET /list/most-popular-category

- Get most frequently occurring business in a hard-coded city
- Returns name of business category with the highest number of occurrences

GET /list/categories/{categoryName}

- Get the number of occurrences of a specified category name in a hard-coded city
- Params:
  - categoryName (String) - Path : name of the category to get the number of occurrences for
- Returns number of occurrences for that category

### Hash Map

Specified in: com.cs201.g1t1.map.HashmapController

GET /hashmap/most-popular-category

- Get the number of occurrences of a specified category name in a hard-coded city
- Returns name of business category with the highest number of occurrences

GET /hashmap/categories/{city}

- Get most frequently occurring category in a city
- Params:
  - city (String) - Path : name of the city in which to find the most frequently occurring category
- Returns the name of the business category with the highest number of occurrences

GET /hashmap/collisions

- Get the number of collisions with a hard-coded hash function
- Returns number of collisions

### UI-related

Specified in: com.cs201.g1t1.util.UIController

GET /api/kyle

- Get all businesses in the city of Kyle (the only supported city for now in the UI)
- Returns all businesses in the city of Kyle

GET /api/kd-tree/list

- Use kd-tree and list algorithms to find the most popular category of a selected area
- Params:
  - xMin (double) - Query : minimum longitude
  - xMax (double) - Query : maximum longitude
  - yMin (double) - Query : minimum latitude
  - yMax (double) - Query : maximum latitude

GET /api/kd-tree/hashmap

- Use kd-tree and hashmap algorithms to find the most popular category of a selected area
- Params:
  - xMin (double) - Query : minimum longitude
  - xMax (double) - Query : maximum longitude
  - yMin (double) - Query : minimum latitude
  - yMax (double) - Query : maximum latitude

GET /api/range-tree/list

- Use range-tree and list algorithms to find the most popular category of a selected area
- Params:
  - xMin (double) - Query : minimum longitude
  - xMax (double) - Query : maximum longitude
  - yMin (double) - Query : minimum latitude
  - yMax (double) - Query : maximum latitude

GET /api/range-tree/hashmap

- Use range-tree and hashmap algorithms to find the most popular category of a selected area
- Params:
  - xMin (double) - Query : minimum longitude
  - xMax (double) - Query : maximum longitude
  - yMin (double) - Query : minimum latitude
  - yMax (double) - Query : maximum latitude

## Dataset

All data used for the experimentation and testing is imported from yelp_academic_dataset_business.json into a MySQL database hosted on Amazon RDS

## Relevant Links

- Final Presentation Video: https://youtu.be/gFRmH8KLIuY
- Github Repo (Main): https://github.com/Jenpoer/cs201-g1t1-main
- Github Repo (UI): https://github.com/Jenpoer/cs201-g1t1-ui
