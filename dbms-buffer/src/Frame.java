public class Frame {
  // Constant variables
  private int BLOCKSIZE = 100;

  // Class attributes
  private Record[] content;
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
    this.content = new Record[BLOCKSIZE] ;
    this.dirty = false;
    this.pinned = false;
    this.block_id = -1;
  }

  /*
   * Getters ----------------------------------------------
   */
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

  /*
   * Setters ----------------------------------------------
   */
  public void setContent(Record[] content) {
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
  public Record getRecord(int r_num) {
    // initialize record index in the given block
    int r_idx = (r_num - (100*(this.block_id-1)))-1;
    return this.content[r_idx];
  }

  /*
   * ------------------------------------------------------
   * Update the record to a new given content given a record number
   * Argument: int record_num, char[] new_content
   * Return: void
   */
  public void updateRecord(int r_num, char[] new_content) {
    // initialize record index in the given block
    int r_idx = (r_num - (100*(this.block_id-1)))-1;
    this.content[r_idx].setRecordContent(new_content);
  }
}
