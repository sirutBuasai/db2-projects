# CS 4432 Project 1
### Sirut Buasai
### sbuasai2@wpi.edu
### Student id: 898424678
-------------------------
## Step 1: Compilation and Running code
- Run `javac CS4432_Project1_sbuasai2.java` to compile the code
- Run `java CS4432_Proejct1_sbuasai2 <buffer_size>` to run the code
-------------------------
## Step 2: Test Results
- My program is outputting *almost* exactly the same results as the test cases.
- My implementation of the replacement policy is working as expected since the eviction message is outputting correctly according to the test cases
-------------------------
## Step 3: Design Decisions
- Overview
  - In this project I separate record number into two categories: 1. raw record number `rr_num` and normal record number `r_num`.
  - `rr_num` refers the to raw given record number by the user. Eg: 1,5,656,135,92,300,509,... etc.
  - `r_num` refers to the computed record number based on the raw record number. Eg: `rr_num` of 250 will have a `r_num` of 50 because the record 250 is in block 3 record 50.
- Frame class:
  - `initialize()` function is the actual constructor of the class as opposed to the conventional constructor. I chose to do this because in the project description, the Prof. indicated that we should have a function called 'initialize' to initialize the objects.
  - Apart from the standard setters and getters, I have two extra functions `getRecord()` and `updateRecord()`
    - `getRecord()` get the record content based on the given record number.
    - `updateRecord()` that updates the record content to a new content based on a given record number.
- BufferPool class:
  - `get()`, `set()`, `pin()`, and `unpin()` are all implemented according to the guidelines given in the project description.
  - `initialize()` function is the actual constructor of the class as opposed to the conventional constructor.
  - `calcBlockId()` and `calcRecordNum()` takes in raw record number and output the block id and the normal record number based on the given raw input.
  - To search if a given block id is in the frame, I implemented a function `searchBlock()` that uses `HashMap map` class attribute to keep track of the block id in the frames.
    - The (key,value) pairs for the HashMap is (block_id,frame_num).
  - To bring a block to the frame, I implemented a function `bringBlock()` that tries to read the file and copy the file content to memory in the frame content. If successful, return the frame number and -1 otherwise.
  - To search for a free frame, I implemented a function `searchFreeFrame()` that utilizes the class attribute `int[] bitmap` to see if any of the frames are free.
    - The `bitmap` array is an array of the same length with `Frame[] buffers`. Each index corresponds to each frame_num and the values are 0 if a frame is free and 1 if not free.
  - `removableFrame()` interates through the buffer and check if any block is removable from the frame. This function is called inside `searchFreeFrame()` in the case that there is not free frame available.
    - This function will return -1 if none if all the frames are full and all blocks are pinned.
    - This function also encapsulates the replacement policy of last evicted frame using the class attribute `int removeIdx`.
  - To write a modified content back to disk, I implemented a function `writeToBlock()` that try to overwrite the file with the new content using `FileWriter`.
  - To read a file from disk to memory, I implemented a function `readFile()` that read the file and return its content
- CS4432_Project1_sbuasai2 class
  - This is the main class for this project program.
  - First switch case statement handles the argument given to the program, specifically, the buffer size given by the user.
  - After argument handling, I initialzed all necessary variables and the buffer pool to store all the frames.
  - After initialization, the program goes into a loop waiting for commands `GET, SET, PIN, UNPIN, HELP, EXIT` using a switch case statement.
  - I implemented a `toUpperCase()` in the switch case to make sure that as long as the user spell the commands correctly, the program will execute the commands without case sensitivity.
  - In addition to the required commands `GET, SET, PIN, UNPIN`, I implemented `HELP` for the user to see help within the program and `EXIT` when the user is done executing commands and exist the program.
    - Note that the program will loop forever if the user does not execute `EXIT` at the end.
