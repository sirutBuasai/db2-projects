import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;

public class BufferPool {
  // Global variables
  private int NUMRECORD = 100;
  private int BLOCKSIZE = 4096;
  private String evictionString = "";

  // Class attributes
  private Frame[] buffers;
  private int[] bitmap;
  private HashMap<Integer, Integer> map = new HashMap<>(); // K: block_id, V: frame_num
  private int removeIdx;

  // Empty constructor
  public BufferPool() {}

  /*
   * Actual constructor -----------------------------------
   */
  public void initialize(int buffer_size) {
    // initialize the buffer array
    this.buffers = new Frame[buffer_size];
    this.bitmap = new int[buffer_size];
    this.removeIdx = 0;
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
      System.out.println(record_content + "; File " + block_id + " is already in memory; Located in frame " + (frame_num+1) + "; " + evictionString);
      evictionString = "";
    }
    else {
      // if block is brought into memory
      frame_num = this.bringBlock(block_id);
      if (frame_num >= 0) {
        // output the content of the given record
        String record_content = String.valueOf(this.buffers[frame_num].getRecord(r_num));
        System.out.println(record_content + "; Brought file " + block_id + " from disk; Placed in frame " + (frame_num+1) + "; " +  evictionString);
        evictionString = "";
      }
      else {
        System.out.println("The corresponding block #" + block_id + "cannot be accessed from disk because the memory buffers are full");
      }
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
    // check if the block exists in the buffer pool
    int frame_num = this.searchBlock(block_id);
    // if a block is found.
    if (frame_num >= 0) {
      // update the content of the given record
      this.buffers[frame_num].updateRecord(r_num, new_content);
      System.out.println("Write was successful; File " + block_id + " already in memory; Located in frame " + (frame_num+1) + "; " + evictionString);
      evictionString = "";
    }
    else {
      // if block is brought into memory
      frame_num = this.bringBlock(block_id);
      if (frame_num >= 0) {
        // update the content of the given record
        this.buffers[frame_num].updateRecord(r_num, new_content);
        System.out.println("Write was successful; Brought file " + block_id + " from disk; Placed in frame " + (frame_num+1) + "; " + evictionString);
        evictionString = "";
      }
      else {
        System.out.println("The corresponding block #" + block_id + " cannot be accessed from disk because the memory buffers are full");
      }
    }
  }

  /*
   * ------------------------------------------------------
   * Pin a block given a block number
   * Argument: int block_id
   * Return: void
   */
  public void pin(int block_id) {
    // check if the block exists in the buffer pool
    int frame_num = this.searchBlock(block_id);
    // if a block is found.
    if (frame_num >= 0) {
      // if the pinned is already set, do nothing
      if (this.buffers[frame_num].getPinned()) {
        System.out.println("File " + block_id + " pinned in Frame " + (frame_num+1) + "; Already pinned; " + evictionString);
        evictionString = "";
      }
      else {
        // update the content of the given record
        this.buffers[frame_num].setPinned(true);
        System.out.println("File " + block_id + " pinned in frame " + (frame_num+1) + "; Not already pinned; " + evictionString);
        evictionString = "";
      }
    }
    else {
      // if block is brought into memory
      frame_num = this.bringBlock(block_id);
      if (frame_num >= 0) {
        // update the content of the given record
        this.buffers[frame_num].setPinned(true);
        System.out.println("File " + block_id + " pinned in frame " + (frame_num+1) + "; Not already pinned; " + evictionString);
        evictionString = "";
      }
      else {
        System.out.println("The corresponding block " + block_id + " cannot be pinned because the memory buffers are full");
      }
    }
  }

  /*
   * ------------------------------------------------------
   * Unpin a block given a block number
   * Argument: int block_id
   * Return: void
   */
  public void unpin(int block_id) {
    // check if the block exists in the buffer pool
    int frame_num = this.searchBlock(block_id);
    // if a block is found.
    if (frame_num >= 0) {
      // update the pinned if it was originally set
      if (this.buffers[frame_num].getPinned()) {
        this.buffers[frame_num].setPinned(false);
        System.out.println("File " + block_id + " in frame " + (frame_num+1) + " is unpinned; Frame " + (frame_num+1) + " was not already unpinned");
      }
      else {
        // if the pinned is not set, do nothing
        System.out.println("File " + block_id + " in frame " + (frame_num+1) + " is unpinned; Frame was already unpinned");
      }
    }
    else {
      System.out.println("The corresponding block " + block_id + " cannot be unpinned because it is not in memory");
    }
  }

  /*
   * Other methods ----------------------------------------
   * Conversion from raw record number to block number
   * Argument: int rr_num
   * Return: int block_id
   */
  public int calcBlockId(int rr_num) {
    return Math.floorDiv(rr_num-1, NUMRECORD) + 1;
  }

  /*
   * ------------------------------------------------------
   * Conversion from raw record number to normal record number
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
   * Bring block into a free frame
   * Argument: int block_id
   * Return: int free_frame if succeed, -1 if not
   */
  public int bringBlock(int block_id) {
    int free_frame = this.searchFreeFrame();
    if (free_frame >= 0) {
      // read the block content and bring to memory
      String file_name = "F" + String.valueOf(block_id) + ".txt";
      String str = this.readFile(file_name);
      // if the reading does not return an error
      if (str != "Error") {
        char[] output = str.toCharArray();
        this.buffers[free_frame].setContent(output);
        // update metadata
        this.buffers[free_frame].setBlockId(block_id);
        this.bitmap[free_frame] = 1;
        this.map.put(block_id, free_frame);
        return free_frame;
      }
    }
    return -1;
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
    // if none of the frames are free, try to remove one of the frames, if not possible return -1
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
    for (int i = removeIdx; i < this.buffers.length; i++) {
      // if the frame is pinned, skip it, otherwise, check for dirty bit
      if (!this.buffers[i].getPinned()) {
        // move the removeIdx for the last evicted frame
        removeIdx = i+1;
        // if the frame is dirty, write before removing the frame
        if (this.buffers[i].getDirty()) {
          this.writeToBlock(i);
        }
        // reset all the metadata
        evictionString = "Evicted file " + this.buffers[i].getBlockId() + " from frame " + (i+1);
        map.remove(this.buffers[i].getBlockId());
        this.buffers[i].setContent(new char[BLOCKSIZE]);
        this.buffers[i].setDirty(false);
        this.buffers[i].setPinned(false);
        this.buffers[i].setBlockId(-1);
        this.bitmap[i] = 0;
        return i;
      }
    }
    // iterate in again from index 0 to the removeIdx as a circular motion
    for (int i = 0; i < removeIdx; i++) {
      // if the frame is pinned, skip it, otherwise, check for dirty bit
      if (!this.buffers[i].getPinned()) {
        // move the removeIdx for the last evicted frame
        removeIdx = i+1;
        // if the frame is dirty, write before removing the frame
        if (this.buffers[i].getDirty()) {
          this.writeToBlock(i);
        }
        // reset all the metadata
        evictionString = "Evicted file " + this.buffers[i].getBlockId() + " from frame " + (i+1);
        map.remove(this.buffers[i].getBlockId());
        this.buffers[i].setContent(new char[BLOCKSIZE]);
        this.buffers[i].setDirty(false);
        this.buffers[i].setPinned(false);
        this.buffers[i].setBlockId(-1);
        this.bitmap[i] = 0;
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
      File file = new File("./Project1/F"+String.valueOf(this.buffers[frame_idx].getBlockId())+".txt");
      FileWriter file_writer = new FileWriter(file, false);
      // write the new content onto disk
      file_writer.write(data);
      // close file writer
      file_writer.close();
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
      return data;
    }
    catch (FileNotFoundException e) {
      System.err.println("Error: Cannot find or open file");
      e.printStackTrace();
      return "Error";
    }
  }
}
