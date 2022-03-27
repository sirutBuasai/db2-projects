public class Frame {
  // Constant variables
  private int BLOCKSIZE = 100;

  // Class attributes
  private Record[] content;
  private boolean dirty;
  private boolean pinned;
  private int block_id;

  // Constructor ------------------------------------------
  public Frame(Record[] content, boolean dirty, boolean pinned, int block_id) {
    // content size handling
    if (content.length > BLOCKSIZE) {
      throw new java.lang.Error("Error: content length exceeds 100 records (4KB)!");
    }

    // attribute initialization
    this.content = content;
    this.dirty = dirty;
    this.pinned = pinned;
    this.block_id = block_id;
  }

  // Getters ----------------------------------------------
  public Record[] getContent() {
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
  // Initialize the frame block
  public void initialize(int block_id) {
    start_idx = RECORDSIZE * (block_id-1);
    end_idx = RECORDSIZE * block_id;

    for () {
      Record rec = new Record(content, record_num)
    }
  }

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

  // Update the record to a new given content give na record number
  // Argument: int record_num, char[] new_content
  // Return: void
  public void updateRecord(int r_num, char[] new_content) {
    // content size handling
    if (new_content.length != RECORDSIZE) {
      throw new java.lang.Error("Error: given content is not 40 bytes!");
    }

    // initialize starting and ending content index for the given record
    int start_idx = RECORDSIZE * (r_num-1);
    int end_idx = RECORDSIZE * r_num;
    // initialize variables for the new record
    int j = 0;

    // from start_idx to end_idx, copy the new content into the existing record
    for (int i = start_idx; i < end_idx; i++) {
      this.content[i] = new_content[j];
      j++;
    }
  }
}
