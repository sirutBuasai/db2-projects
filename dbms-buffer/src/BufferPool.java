import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class BufferPool {
  // Class attributes
  private Frame[] buffers;
  private int[] bitmap;
  // K: block_id, V: frame_num
  private HashMap<Integer, Integer> map = new HashMap<>();

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
   * Other methods ----------------------------------------
   *
   * Search if a block is available in the buffer pool
   * Argument: int block_id
   * Return: int frame_num if block is in buffer, -1 otherwise
   */
  public Integer searchBlock(int block_id) {
    if (map.containsKey(block_id)) {
      Integer frame_num = map.get(block_id);
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
   * Being block into frame
   * Argument: int block_id
   * Return: int 1 if succeed, 0 if not
   */
  public int bringBlock(int block_id) {
    // get the block content and store it in outpit variable
    String file_name = "F" + String.valueOf(block_id) + ".txt";
    char[] output = this.readFile(file_name).toCharArray();

    // find a free frame
    int frame_num = this.searchFreeFrame();
    // copy the block content to the frame if found
    if (frame_num >= 0) {
      this.buffers[frame_num].setContent(output);
      this.buffers[frame_num].setBlockId(block_id);
      // update metadata
      this.map.put(block_id, frame_num);
      this.bitmap[frame_num] = 1;
      return 1;
    }
    // frame is not found, return 0
    else {
      return 0;
    }

  }

  /*
   * ------------------------------------------------------
   * Get a content of the block
   * Argument: int block_id
   * Return: Record[] block_content
   */
  public Frame getBlockContent(int block_id) {
    // search if the block exists in the buffer pool first
    int frame_num = this.searchBlock(block_id);
    // if the block is in the buffer pool, get the content
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
