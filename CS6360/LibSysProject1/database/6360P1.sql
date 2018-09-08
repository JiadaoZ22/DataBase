--USE '/Users/jiadao/eclipse-workspace/6360DB-jxz172230/database/6360P1.sql';
USE 6360P1
------------------
------------------

--First, Import two data sources we have!
CREATE TABLE IF NOT EXISTS books
(
	isbn10 varchar(10),
	isbn13 numeric,
	title text,
	author text,
	cover text,
	publisher text,
	pages smallint
);
COPY books FROM '/Users/jiadao/eclipse-workspace/6360DB-jxz172230/database/books.csv'
(FORMAT CSV ,DELIMITER E'\t', HEADER TRUE );

--------
CREATE TABLE IF NOT EXISTS borrowers
(
	borrower_id integer,
	ssn text,
	first_name text,
	last_name text,
	email text,
	address text,
	city text,
	state text,
	phone text
);
COPY borrowers FROM '/Users/jiadao/eclipse-workspace/6360DB-jxz172230/database/borrowers.csv'
(FORMAT CSV ,DELIMITER E',', HEADER TRUE );


------------
--build DATABASE
------------
DROP TABLE IF EXISTS authors;
CREATE TABLE authors (
  Author_id SERIAL,
  Name text,
  PRIMARY KEY (Author_id)
);
INSERT INTO authors(Name) SELECT DISTINCT author FROM books;


-----
DROP TABLE IF EXISTS book;
CREATE TABLE book (
  Isbn varchar(15) NOT NULL,
  Title text NOT NULL,
  PRIMARY KEY (Isbn)
);
INSERT INTO book(Isbn, Title) SELECT isbn10,Title FROM books;


-------
DROP TABLE IF EXISTS book_authors;
CREATE TABLE book_authors (
  Author_id int NOT NULL,
  Isbn varchar(15) NOT NULL,
  PRIMARY KEY (Author_id, Isbn),
  FOREIGN KEY (Isbn) REFERENCES book(Isbn),
  FOREiGN KEY (Author_id) REFERENCES authors(Author_id)
);
INSERT INTO book_authors(Author_id, Isbn) SELECT Author_id, isbn10
    FROM authors, books WHERE authors.Name=books.author;

--------
DROP TABLE IF EXISTS borrower;
CREATE TABLE borrower (
  Card_id text UNIQUE,
  Ssn char(11) NOT NULL,
  Fname varchar(40) NOT NULL,
  Lname varchar(40) NOT NULL,
  Address varchar(100) NOT NULL,
  Phone char(14) DEFAULT NULL,
  PRIMARY KEY (Card_id)
);


--------
DROP TABLE IF EXISTS book_loans;
CREATE TABLE book_loans (
  Loan_id SERIAL,
  Isbn varchar(15) NOT NULL,
  Card_id text NOT NULL,
  Date_out DATE default current_date,
  Due_date DATE default current_date+14,
  Date_in DATE DEFAULT NULL,
  PRIMARY KEY (Loan_id),
  FOREIGN KEY (Isbn) REFERENCES book(Isbn),
  FOREIGN KEY (Card_id) REFERENCES borrower(Card_id)
);


------
DROP TABLE IF EXISTS fines;
CREATE TABLE fines (
  Loan_id int NOT NULL,
  Fine_amt NUMERIC(7,2) NOT NULL,
  Paid boolean DEFAULT FALSE,
  PRIMARY KEY (Loan_id),
  FOREIGN KEY (Loan_id) REFERENCES book_loans(Loan_id)
);


-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO jd;
-- ALTER TABLE public.book_authors OWNER TO jd;
-- ALTER USER jd WITH SUPERUSER ;