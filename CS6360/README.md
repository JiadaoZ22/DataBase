# Jiadao's CS6360 DB project, summer 2018
## Project 1
> Individual project
### requirement
> This SQL programming project involves the creation of a database host application that interfaces with a backend SQL database implementing a Library Management System. Users of the system are understood to be librarians (not book borrowers). 

## Project 2
> Group project
### requirement
> The goal of this project is to implement a (very) rudimentary database engine that is loosely based on a hybrid between MySQL and SQLite, which I call DavisBase. Your implementation should operate entirely from the command line and API calls (no GUI). 
Your database will only need to support actions on a single table at a time, no joins or nested query functionality is required. Like MySQL's InnoDB data engine (SDL), your program will use file-per-table approach to physical storage. Each database table will be physically stored as a separate single file. Each table file will be subdivided into logical sections of fixed equal size call pages. Therefore, each table file size will be exact increments of the global page_size attribute, i.e. all data files must share the same page_size attribute. You may make page_size be a configurable attribute, but you must support a page size of 512 Bytes. The test scenarios for grading will be based on a page_size of 512B. Once a database is initialized, your are not required to support a reformat change to its page_size (but you may implement such a feature if you choose). 



