public class Frame {
  // Constant variables
  private int RECORDSIZE = 40;
  private int BLOCKSIZE = 4096;

  // Class attributes
  private char[] content;
  private boolean dirty;
  private boolean pinned;
  private int block_id;

  // Constructor ------------------------------------------
  public Frame(char[] content, boolean dirty, boolean pinned, int block_id) {
    // content size handling
    if (content.length > BLOCKSIZE) {
      throw new java.lang.Error("Error: content length exceeds 4KB!");
    }
    else {
      // attribute initialization
      this.content = content;
      this.dirty = dirty;
      this.pinned = pinned;
      this.block_id = block_id;
    }
  }

  // Getters ----------------------------------------------
  public char[] getContent() {
    return this.content;
  }

  public boolean getDirty() {
    return this.dirty;
  }

  public boolean getPinned() {
    return this.pinned;
  }

  public int getBlockId() {
    return this.block_id;
  }

  // Setters ----------------------------------------------
  public void setContent(char[] content) {
    // content size handling
    if (content.length > BLOCKSIZE) {
      throw new java.lang.Error("Error: content length exceeds 4KB!");
    }
    else {
      this.content = content;
    }
  }

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  public void setPinned(boolean pinned) {
    this.pinned = pinned;
  }

  public void setBlockId(int block_id) {
    this.block_id = block_id;
  }

  // Other methods ----------------------------------------

  // Get the content of the record in the current block given a record number
  // Argument: int record_num
  // Return: char[] record
  public char[] getRecord(int r_num) {
    // initialize starting and ending content index for the given record
    int start_idx = RECORDSIZE * (r_num-1);
    int end_idx = RECORDSIZE * r_num;
    // initialize variables for the return record
    int j = 0;
    char[] record = new char[RECORDSIZE];

    // from start_idx to end_idx, copy the block content onto the returning record
    for (int i = start_idx; i < end_idx; i++) {
      record[j] = this.content[i];
      j++;
    }

    return record;
  }
}
