# CS 4432 Project 3
### Sirut Buasai
### sbuasai2@wpi.edu
### Student id: 898424678
-------------------------
## Step 1: Compilation and Running code
- Run `javac CS4432_Project3_sbuasai2.java` to compile the code
- Run `java CS4432_Proejct3_sbuasai2` to run the code
-------------------------
## Step 2: Project Completion
- I believe that all of my code works according to the project description. The order of output may vary but the essential output is all there.
-------------------------
## Step 3: Design Decisions
- Overview
  - In this project, since we were allowed to choose 2 out of 3 problems, I chose to do `Hash-Based Join` and `Block-Level Nested-Loop Join`
- Database class:
  - This class contains both Hash-Based join and Nested-Loop join of the database system.
  - The conditions in both Hash-Based join and Nested-Loop join are both hardcoded because the project description specified that the testing conditions will be fixed.
  - `getRecord()` is a method that return the record content given a file content and the record number within that file.
  - `getRandomV()` is a method that return the `randomV` field of a record.
  - Hash-Based join
    - For the `hash_table`, I implemented my own hash table using `ArrayList<>` class as opposed to the built-in `HashTable`. This is so that I can implement my own hash function, namely `hashFunc()` so that we have a limited number of buckets.
    - `hashFunc()` is my own implementation of the hash function that limits the bucket number to 50 as states in the project description.
    - `hashPut()` is a method for putting the record into my custom hash table using `randomV` as a key and `record_content` as value.
    - `buildTable()` is a method for reading relation A and build a hash index on randomV of relation A.
    - `joinTable()` is a method for read records in relation B and compare it to relation A using the hash table built by `buildTable()` on A.randomV. This method output query is hardcoded based on the given condition `A.RandomV = B.RandomV`.
  - Nested-Loop join
    - `data_array` is the variable to store all the content in relation A in memory.
    - `buildArray()` is a method to read all the records in all files in relation A one by one and store them in memory, namely `data_array`.
    - `countStar()` is a method that count the number of tuples in relation A and B based on a hardcoded condition `A.RandomV > B.RandomV`.
- CS4432_Project2_sbuasai2 class:
  - This is the main class for this project program.
  - The program goes into an infinite loop unless a command `EXIT` is issued by the user.
  - The program will handle case insensitive input. This means that there is no difference between type `select` and `SELECT`. However, the program is whitespace-sensitive. This means that the amount of spaces within the input command must be exactly as what the program expects.
  - To see the list of commands that program allows for input, the user can type `HELP`. If `HELP` is provided, you can copy the command directly from help output and use them as input to grade.
