Names and Responsibilities:
       Changsong Li - Implemented Create table, insert into table, delete from where.
       Goutham Gandham - Implemented Create index, insert into index.
       Sanjana Vijaykumar - Update functionality.
       Jiadao Zou - Query functionality.

1. How to run the code?
execute ./run.sh

2. Create table example:
   CREATE TABLE Persons (PersonID int, SSN int, LastName TEXT);

3. Insert into table example:
  Insert into Persons values (101, 10000012, "Davis 1233452342 Hello world");
  Insert into Persons values (102, 10000012, "Davis 1233452342 Hello world");
  Insert into Persons values (103, 10000012, "Davis 1233452342 Hello world");
  Insert into Persons values (104, 10000012, "Davis 1233452342 Hello world");
  Insert into Persons values (105, 10000012, "Davis 1233452342 Hello world");

4. Query example:
   Select * from Persons;

5. Create Index example:
   CREATE INDEX index_name ON Persons (SSN);

6. Delete from example:
    Delete from Persons where PersonID = 101;
