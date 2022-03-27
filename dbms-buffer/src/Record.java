public class Record {
  // Constant variables
  private int RECORDSIZE = 40;

  // Class attributes
  private char[] record_content;
  private int record_num;

  // Constructor ------------------------------------------
  public Record(char[] record_content, int record_num) {
    // content size handling
    if (record_content.length != RECORDSIZE) {
      throw new java.lang.Error("Error: record content exceeds 40 bytes!");
    }

    this.record_content = record_content;
    this.record_num = record_num;
  }

  // Getters ----------------------------------------------
  public char[] getRecordContent() {
    return this.record_content;
  }

  public int getRecordNum() {
    return this.record_num;
  }

  // Setters ----------------------------------------------
  public void setContent(char[] new_content) {
    // content size handling
    if (new_content.length != RECORDSIZE) {
      throw new java.lang.Error("Error: new record content exceeds 40 bytes!");
    }
    this.record_content = new_content;
  }
}
