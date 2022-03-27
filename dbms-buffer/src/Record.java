public class Record {
  // Constant variables
  private int RECORDSIZE = 40;

  // Class attributes
  private char[] record_content;

  // Constructor ------------------------------------------
  public Record(char[] record_content, int record_num) {
    // content size handling
    if (record_content.length != RECORDSIZE) {
      throw new java.lang.Error("Error: record content is not equal to 40 bytes!");
    }

    this.record_content = record_content;
  }

  // Getters ----------------------------------------------
  public char[] getRecordContent() {
    return this.record_content;
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
