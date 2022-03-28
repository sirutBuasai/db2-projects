import java.util.HashMap;

public class BufferPool {
  // Class attributes
  private Frame[] buffers;
  private int[] frame_bitmap;
  private HashMap<Integer, Integer> map = new HashMap<>();

  // Empty constructor
  public BufferPool() {}

  /*
   * Actual constructor -----------------------------------
   */
  public void initialize(int buffer_size) {
    // initialize the buffer array
    this.buffers = new Frame[buffer_size];
    this.frame_bitmap = new int[buffer_size];
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
    for (int i = 0; i < this.frame_bitmap.length; i++) {
      if (this.frame_bitmap[i] == 0) {
        return i;
      }
    }
    // if none of the frames are free, return -1
    return -1;
  }

  /*
   * ------------------------------------------------------
   * Get a content of the block
   */
  public Record[] getBlockContent(int block_id) {
    // search if the block exists in the buffer pool first
    Integer frame_num = this.searchBlock(block_id);
    // if the block is in the buffer pool, get the content
    if (frame_num > 0) {
      return this.buffers[frame_num].getContent();
    }
    // if the block is not in the buffer pool, put block into frame
    else {
      return new Record[] {};
    }
  }

}
