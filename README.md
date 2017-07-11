## Description
Extend BestDeal to use MySQL database engine to support the following features/functionalities:
1. Account Management Feature: Use MySQL to store all accounts login information
2. Order/Transaction Management Feature: Use MySQL to store All Customers transactions/orders

## Installation, Compliation and Execution
- Unzip the compressed file.
- Put unzipped folder "BestDealDB" under Tomcat's webapp path (%TOMCAT_HOME\webapps).
- All source codes are under the folder \BestDealDB\WEB-INF\classes. In case you need to recompile, open command prompt window, navigate to %TOMCAT_HOME\webapps\BestDealDB\WEB-INF\classes and issue javac command.
- Make sure MySQL database is setup and running correctly;
- Make sure an database called "LocalDB" has been created previously. If not, issue "CREATE DATABASE LocalDB;" command in MySQL Workbench or change the database name to any existing databases in file context.xml under %TOMCAT_HOME\webapps\BestDealDB\META-INF\
- Launch Tomcat server, the servlet application now can be accessed via http://localhost/BestDealDB/

## Notes
- For testing purpose, 3 users will be created upon launching. These users are:
    1. username = "aa", password = "aa", type = "Customer";
    2. username = "as", password = "as", type = "Salesman";
    3. username = "ad", password = "ad", type = "Store Manager".
You can also create your own user accounts as usual.
- In case needs to move the "BestDealDB" folder into different location or change its name, you can simply modified the path information by editing Properties.java. There are 6 global attributes defined in that file:
    1. TOMCAT_HOME: defines the Tomcat installation location;
    2. WEBAPP_PATH: defines location of webapps folder;
    3. PROJECT_PATH: defines location of BestDealDB folder;
    4. PRODUCT_CATALOG_PATH: defines the name of product catalog XML file;
    5. USER_DETAILS_PATH: defines the name of user details file;
    6. PAYMENT_DETAILS_PATH: defines the name of payment details file.
- The following SQL queries could be useful when verifying results (can also be found in %TOMCAT_HOME\webapps\BestDealDB\META-INF\LocalDB.sql):
    1. "USE LocalDB;" = use database named "LocalDB"
    2. "SHOW TABLES;" = show names of all tables in current database
    3. "SELECT * FROM Registration;" = show all entries in table "Registration"
    4. "SELECT * FROM Orders;" = show all entries in table "Orders"
    5. "SELECT * FROM OrderItems;" = show all entries in table "OrderItems"
