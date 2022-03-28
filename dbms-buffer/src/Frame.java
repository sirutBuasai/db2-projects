public class Frame {
  // Constant variables
  private int BLOCKSIZE = 4096;
  private int RECORDSIZE = 40;
  private int NUMRECORD = 100;

  // Class attributes
  private char[] content;
  private boolean dirty;
  private boolean pinned;
  private int block_id;

  // Empty constructor
  public Frame() {}

  /*
   * Actual constructor -----------------------------------
   */
  public void initialize() {
    // attribute initialization
    this.content = new char[BLOCKSIZE] ;
    this.dirty = false;
    this.pinned = false;
    this.block_id = -1;
  }

  /*
   * Getters ----------------------------------------------
   */
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

  /*
   * Setters ----------------------------------------------
   */
  public void setContent(char[] content) {
    this.content = content;
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

  /*
   * Other methods ----------------------------------------
   *
   * Get the content of the record in the current block given a record number
   * Argument: int record_num
   * Return: char[] record
   */
  public char[] getRecord(int r_num) {
    // initialize record index in the given block
    int start_idx = ((r_num-(NUMRECORD*this.block_id-1))-1) * RECORDSIZE;
    int end_idx = start_idx + RECORDSIZE;
    // initialize resulting content
    char[] record_content = new char[RECORDSIZE];
    int j = 0;
    // copy the current content onto the record
    for (int i = start_idx; i < end_idx; i++) {
      record_content[j] = this.content[i];
      j++;
    }

    return record_content;
  }

  /*
   * ------------------------------------------------------
   * Update the record to a new given content given a record number
   * Argument: int record_num, char[] new_content
   * Return: void
   */
  public void updateRecord(int r_num, char[] new_content) {
    // initialize record index in the given block
    int start_idx = ((r_num-(NUMRECORD*this.block_id-1))-1) * RECORDSIZE;
    int end_idx = start_idx + RECORDSIZE;
    int j = 0;
    // copy the new content onto the existing record
    for (int i = start_idx; i < end_idx; i++) {
      this.content[i] = new_content[j];
      j++;
    }
    // update metadata for the dirty flag
    this.dirty = true;
  }
}
