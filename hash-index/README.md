# CS 4432 Project 2
### Sirut Buasai
### sbuasai2@wpi.edu
### Student id: 898424678
-------------------------
## Step 1: Compilation and Running code
- Run `javac CS4432_Project2_sbuasai2.java` to compile the code
- Run `java CS4432_Proejct2_sbuasai2 <buffer_size>` to run the code
-------------------------
## Step 2: Project Completion
- I believe that all of my code works according to the project description. The order of output may vary but the essential output is all there.
-------------------------
## Step 3: Design Decisions
- Overview
  - In this project, I utilize `Iterator<String>` class to allow DBReader class and Database class to read the files like a long string
- HashIndex class:
  - `add()` method is called when the user issue the command `CREATE INDEX`.
    - This function takes a `RandomV` as the key and the record location as value.
    - The record locations are stored in the format of `file_id1:record_id1,record_id2;file_id2:record_id1,record_id2`. This way I can easily distinguish between multiple records in a file using `,` and multiple files using `;`.
  - `read()` method is called when the user is trying to query a specific record.
- ArrayIndex class:
  - `add()` method is called when the user issue the command `CREATE INDEX`.
    - The array index stores the `RandomV` as the index of the array and the record locations as a string in the specified index.
    - The record locations are stored in the format of `file_id1:record_id1,record_id2;file_id2:record_id1,record_id2`. This way I can easily distinguish between multiple records in a file using `,` and multiple files using `;`.
  - `readRange()` method is called when the use is trying to query records within a specific range.
    - The method tries to find all the files that needs to be accessed first to reduce the I/Os needed.
- DBReader class:
  - This is the file reader class for the Database. All I/Os are being performed by this class.
  - This class implements `Iterator<String>` so that all the files read can be treated as list of Strings which allows for cleaner and more concise code.
  - `hasNext()` and `next()` are overriding methods on the `Iterator<String\>` class as we need this to treat the files as String.
  - `readFile()` method is used to read a specific file.
  - `printRecords()` methods are used to read the records given a list of files and records needed to read and output all the records needed to standard output.
- Database class:
  - This class is a controller for the different types of searches.
  - This class implements `Iterator<String>` class so that I can use enhanced for-each loop which allows for cleaner and more concise code.
  - `interator()` method is an overide to the `Iterator<String\>` class so that I can call DBReader class to read the files.
  - `createIndex()` method is used when the user issue a `CREATE INDEX` commands that create both hash index and array index.
  - `search()` method is the main search handling that calls other search function depending on whether or not indices are available.
  - `equalitySearch()`, `rangeSearch()`, `equalityTableScan()`, `rangeTableScan()`, and `inequalityTableScan()` are the different type of queries available according to the project description.
- CS4432_Project2_sbuasai2 class:
  - This is the main class for this project program.
  - The program goes into an infinite loop unless a command `EXIT` is issued by the user.
  - The program will handle case insensitive input. This means that there is no difference between type `select` and `SELECT`. However, the program is whitespace-sensitive. This means that the amount of spaces within the input command must be exactly as what the program expects.
  - To see the list of commands that program allows for input, the user can type `HELP`.
