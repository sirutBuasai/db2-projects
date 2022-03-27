public class Frame {
  // Constant variables
  private int BLOCKSIZE = 100;

  // Class attributes
  private Record[] content;
  private boolean dirty;
  private boolean pinned;
  private int block_id;

  // Constructor ------------------------------------------
  public Frame(boolean dirty, boolean pinned, int block_id) {
    // attribute initialization
    this.content = new Record[BLOCKSIZE] ;
    this.dirty = dirty;
    this.pinned = pinned;
    this.block_id = block_id;
  }

  public Frame(int block_id) {
    // attribute initialization
    this.content = new Record[BLOCKSIZE] ;
    this.dirty = false;
    this.pinned = false;
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

  // Other methods ----------------------------------------
  // Initialize the frame block
  public void initialize() {
    // initialize starting and ending block index for the given block id
    int start_idx = BLOCKSIZE * (this.block_id-1);
    int end_idx = BLOCKSIZE * this.block_id;
    // initialize the record array of the frame
    int j = 0;
    Record[] rec_arr = new Record[BLOCKSIZE];

    for (int r_num = start_idx; r_num < end_idx; r_num++) {
      // initialize the content of the current record
      char[] r_content = constructRecord(r_num+1);
      // construct the record object and add it to the record array
      Record rec = new Record(r_content, r_num+1);
      rec_arr[j] = rec;
      j++;
    }
    this.setContent(rec_arr);
  }

  // Construct a template record payload given a block id and a record number
  // Argument: int block_id, int record_num
  // Return: char[] record_content
  public char[] constructRecord(int r_num) {
    // block_id handling
    String str_b = String.valueOf(this.block_id);
    // append 0 in front if block_id is single digit
    if (str_b.length() == 1) {
        str_b = "0" + str_b;
    }
    // r_num handling
    String str_r = String.valueOf(r_num);
    switch (str_r.length()) {
      // append 00 in front if r_num is single digit
      case 1:
        str_r = "00" + str_r;
        break;
      // append 0 in front if r_num is double digit
      case 2:
        str_r = "0" + str_r;
        break;
    }
    String payload = "F" + str_b + "-Rec" + str_r + ", Name" + str_r + ", address" + str_r + ", age" + str_r + ".";
    return payload.toCharArray();
  }

  // Get the content of the record in the current block given a record number
  // Argument: int record_num
  // Return: char[] record
  public Record getRecord(int r_num) {
    // initialize record index in the given block
    int r_idx = (r_num - (100*(this.block_id-1)))-1;
    return this.content[r_idx];
  }

  // Update the record to a new given content given a record number
  // Argument: int record_num, char[] new_content
  // Return: void
  public void updateRecord(int r_num, char[] new_content) {
    // initialize record index in the given block
    int r_idx = (r_num - (100*(this.block_id-1)))-1;
    this.content[r_idx].setContent(new_content);
  }
}
