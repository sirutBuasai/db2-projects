import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
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
   *
   * Get a record from a block if it exists in a pool and from disk otherwise
   * Argument: int rr_num
   *           record number is in the raw format of 1,..,100,..,n
   * Return: char[] record_content
   */
  public void get(int rr_num) {
    // process block id and record number
    int block_id = this.calcBlockId(rr_num);
    int r_num = this.calcRecordNum(rr_num);
    // check if the block exists in the buffer pool
    int frame_num = this.searchBlock(block_id);
    // if a frame is found, print out the content of the record
    if (frame_num >= 0) {
      char[] record_content = this.buffers[frame_num].getRecord(r_num);
      String output = String.valueOf(record_content);
      System.out.println(output);
    }
    // if a frame is not found, check if we can bring the block into memory
    else {
      int free_frame = this.searchFreeFrame();
      System.out.println(free_frame);
      // if there is a free frame, bring block to memory
      if (free_frame >= 0) {
        this.bringBlock(block_id, free_frame);
        // afterwards, read out the record content
        char[] record_content = this.buffers[free_frame].getRecord(r_num);
        String output = String.valueOf(record_content);
        System.out.println(output);
      }
      // if there is not free frame, notify the user
      else {
        System.out.println("There currently is no free frame");
      }
    }
  }
 
  /*
   * Other methods ----------------------------------------
   * Conversion from raw record number to block number
   * Argument: int rr_num
   * Return: int block_id
   */
  public int calcBlockId(int rr_num) {
    return Math.floorDiv(rr_num, 100) + 1;
  }

  /*
   * ------------------------------------------------------
   * Conversion from raw record number to normal record number
   * Argument: int rr_num
   * Return: int r_num
   */
  public int calcRecordNum(int rr_num) {
    int ones = Math.floorDiv(rr_num, 1)%10;
    int tens = Math.floorDiv(rr_num, 10)%10;
    return (tens*10)+ones;
  }
  /* ------------------------------------------------------
   * Search if a block is available in the buffer pool
   * Argument: int block_id
   * Return: int frame_num if block is in buffer, -1 otherwise
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
    return -1;
  }

  /*
   * ------------------------------------------------------
   * Bring block into the specified
   * Argument: int block_id, int free_frame
   * Return: int 1 if succeed, 0 if not
   */
  public void bringBlock(int block_id, int free_frame) {
    // get the block content and store it in outpit variable
    String file_name = "F" + String.valueOf(block_id) + ".txt";
    char[] output = this.readFile(file_name).toCharArray();

    // find a free frame
    this.buffers[free_frame].setContent(output);
    this.buffers[free_frame].setBlockId(block_id);
    // update metadata
    this.map.put(block_id, free_frame);
    this.bitmap[free_frame] = 1;
  }

  /*
   * ------------------------------------------------------
   * Get a content of the block
   * Argument: int block_id
   * Return: Record[] block_content
   */
  public Frame getBlock(int block_id) {
    // search if the block exists in the buffer pool first
    int frame_num = this.searchBlock(block_id);
    // if the block is in the buffer pool, get the frame containing the block
    if (frame_num >= 0) {
      return this.buffers[frame_num];
    }
    // if the block is not in the buffer pool, put block into frame
    else {
      return new Frame();
    }
  }

  /*
   * ------------------------------------------------------
   * Read file function
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
