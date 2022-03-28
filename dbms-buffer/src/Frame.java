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
   *           eg: record num is in the format of record num within the block
   *               record 250 is the 50th record in block 3 as such record_num = 50
   * Return: char[] record
   */
  public char[] getRecord(int r_num) {
    // initialize resulting content
    char[] record_content = new char[RECORDSIZE];
    // copy the current content onto the record
    for (int i = 0; i < RECORDSIZE; i++) {
      record_content[i] = this.content[i + (RECORDSIZE * (r_num-1))];
    }
    return record_content;
  }

  /*
   * ------------------------------------------------------
   * Update the record to a new given content given a record number
   * Argument: int record_num, char[] new_content
   *           eg: record num is in the format of record num within the block
   *               record 250 is the 50th record in block 3 as such record_num = 50
   * Return: void
   */
  public void updateRecord(int r_num, char[] new_content) {
    // copy the new content onto the existing record
    for (int i = 0; i < RECORDSIZE; i++) {
      this.content[i + (RECORDSIZE * (r_num-1))] = new_content[i];
    }
    // update metadata for the dirty flag
    this.dirty = true;
  }
}
