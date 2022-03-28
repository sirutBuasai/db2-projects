import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;

public class BufferPool {
  // Global variables
  private int NUMRECORD = 100;

  // Class attributes
  private Frame[] buffers;
  private int[] bitmap;
  private HashMap<Integer, Integer> map = new HashMap<>();   // K: block_id, V: frame_num

  // Empty constructor
  public BufferPool() {}

  /*
   * Actual constructor -----------------------------------
   */
  public void initialize(int buffer_size) {
    // initialize the buffer array
    this.buffers = new Frame[buffer_size];
    this.bitmap = new int[buffer_size];
    // populate the buffer array with frames
    for (int i = 0; i < buffer_size; i++) {
      Frame f = new Frame();
      f.initialize();
      this.buffers[i] = f;
    }
  }

  /*
   * Main methods (get,set,pin,unpin) ---------------------
   * Get a record from a block if it exists in a pool and from disk otherwise
   * Argument: int rr_num
   * Return: char[] record_content
   */
  public void get(int rr_num) {
    // process block id and record number
    int block_id = this.calcBlockId(rr_num);
    int r_num = this.calcRecordNum(rr_num);
    // check if the block exists in the buffer pool
    int frame_num = this.searchBlock(block_id);
    // if a block is found.
    if (frame_num >= 0) {
      // output the content of the given record
      String record_content = String.valueOf(this.buffers[frame_num].getRecord(r_num));
      System.out.println(record_content + "; File" + block_id + " is already in memory; Located in frame " + frame_num);
    }
    else {
      // if block is brought into memory
      frame_num = this.bringBlock(block_id);
      if (frame_num >= 0) {
        // output the content of the given record
        String record_content = String.valueOf(this.buffers[frame_num].getRecord(r_num));
        System.out.println(record_content + "; Brought file " + block_id + " from disk; Placed in frame " + frame_num);

      }
      System.out.println("The corresponding block #" + block_id + "cannot be accessed from disk because the memory buffers are full");
    }
  }

  /*
   * ------------------------------------------------------
   * Set a record content to the given new content
   * Argument: int rr_num, char[] new_content
   * Return: void
   */
  public void set(int rr_num, char[] new_content) {
    // process block id and record number
    int block_id = this.calcBlockId(rr_num);
    int r_num = this.calcRecordNum(rr_num);
    // check if the block exists in the buffer pool, and bring to pool if does not exist
    int frame_num = this.searchBlock(block_id);
    // if a block is found or successfully brought to buffer, update the content
    if (frame_num >= 0) {
      this.buffers[frame_num].updateRecord(r_num, new_content);
      System.out.println("Write was successful");
    }
    else {
      System.out.println("There currently is no free frame.");
    }
  }

  /*
   * ------------------------------------------------------
   * Set a record content to the given new content
   * Argument: int rr_num, char[] new_content
   * Return: void
   */
  public void pin(int rr_num, char[] new_content) {
    // process block id and record number
    int block_id = this.calcBlockId(rr_num);
    int r_num = this.calcRecordNum(rr_num);
    // check if the block exists in the buffer pool, and bring to pool if does not exist
    int frame_num = this.searchBlock(block_id);
    // if a block is found or successfully brought to buffer, update the content
    if (frame_num >= 0) {
      this.buffers[frame_num].updateRecord(r_num, new_content);
      System.out.println("Write was successful");
    }
    else {
      System.out.println("There currently is no free frame.");
    }
  }

  /*
   * Other methods ----------------------------------------
   * Conversion from raw record number to block number
   * Eg: raw number of 250 is a block number 3
   * Argument: int rr_num
   * Return: int block_id
   */
  public int calcBlockId(int rr_num) {
    return Math.floorDiv(rr_num-1, NUMRECORD) + 1;
  }

  /*
   * ------------------------------------------------------
   * Conversion from raw record number to normal record number
   * Eg: raw number of 250 is a normal number of 49th indexed record on the third block
   * Argument: int rr_num
   * Return: int r_num
   */
  public int calcRecordNum(int rr_num) {
    int ones = Math.floorDiv(rr_num-1, 1)%10;
    int tens = Math.floorDiv(rr_num-1, 10)%10;
    return (tens*10)+ones+1;
  }

  /* ------------------------------------------------------
   * Search if a block is already in the buffer pool
   * Argument: int block_id
   * Return: int frame_num if successful, -1 otherwise
   */
  public int searchBlock(int block_id) {
    // search for the block within the hash map
    if (map.containsKey(block_id)) {
      int frame_num = map.get(block_id);
      return frame_num;
    }
    else {
      return -1;
    }
  }

  /*
   * ------------------------------------------------------
   * Bring block into the specified
   * Argument: int block_id
   * Return: int free_frame if succeed, -1 if not
   */
  public int bringBlock(int block_id) {
    int free_frame = this.searchFreeFrame();
    if (free_frame >= 0) {
      // read the block content and bring to memory
      String file_name = "F" + String.valueOf(block_id) + ".txt";
      char[] output = this.readFile(file_name).toCharArray();
      this.buffers[free_frame].setContent(output);
      // update metadata
      this.buffers[free_frame].setBlockId(block_id);
      this.map.put(block_id, free_frame);
      this.bitmap[free_frame] = 1;
      return free_frame;
    }
    else {
      return -1;
    }
  }

  /*
   * ------------------------------------------------------
   * Search for and get the first free frame
   * Argument: void
   * Return: int frame_num, -1 if no frame is free
   */
  public int searchFreeFrame() {
    // iterate through the bitmap to find the first 0
    for (int i = 0; i < this.bitmap.length; i++) {
      if (this.bitmap[i] == 0) {
        return i;
      }
    }
    // if none of the frames are free, return -1
    return this.removableFrame();
  }

  /*
   * ------------------------------------------------------
   * Search for the first removable frame
   * Argument: void
   * Return: int frame_num, -1 if no frame can be removed
   */
  public int removableFrame() {
    // iterate through the buffer array and check for the first frame to be removed
    for (int i = 0; i < this.buffers.length; i++) {
      // if the frame is pinned, skip it, otherwise, check for dirty bit
      if (!this.buffers[i].getPinned()) {
        // if the frame is dirty, write before removing the frame
        if (this.buffers[i].getDirty()) {
          this.writeToBlock(i);
        }
        return i;
      }
    }
    // if none of the frames can be free, return -1
    return -1;
  }

  /*
   * ------------------------------------------------------
   * Write the content of the given frame to the block
   * Argument: int frame_idx
   * Return: void
   */
  public void writeToBlock(int frame_idx) {
    // initialize the new content in the memory
    String data = String.valueOf(this.buffers[frame_idx].getContent());
    try {
      // initialize file and file writer class
      File file = new File(".Project1/F"+String.valueOf(this.buffers[frame_idx].getBlockId())+".txt");
      FileWriter file_writer = new FileWriter(file, false);
      // write the new content onto disk
      file_writer.write(data);
      // close file writer
      file_writer.close();
    // reset all the metadata
    this.buffers[frame_idx].setDirty(false);
    this.buffers[frame_idx].setPinned(false);
    this.buffers[frame_idx].setBlockId(-1);
    }
    catch (Exception e) {
      System.err.println("Error: Cannot find or open file");
      e.printStackTrace();
    }
  }

  /*
   * ------------------------------------------------------
   * Read file function
   * Argument: String file_name
   * Return: String data (content of the file)
   */
  public String readFile(String file_name) {
    // initialize return data
    String data = new String();
    try {
      // initialize file and scanner class
      File file = new File("./Project1/"+file_name);
      Scanner file_reader = new Scanner(file);
      // keep reading file until the end of the file
      while (file_reader.hasNextLine()) {
        // print out line by line
        data += file_reader.nextLine();
      }
      // close file reader
      file_reader.close();
    }
    catch (FileNotFoundException e) {
      System.err.println("Error: Cannot find or open file");
      e.printStackTrace();
    }
    return data;
  }
}
