public class Frame {
  private char[] content;
  private boolean dirty;
  private boolean pinned;
  private int block_id;

  // Constructor
  public Frame(char[] content, boolean dirty, boolean pinned, int block_id) {
    // content size handling
    if (content.length > 4096) {
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

  public void print_content() {
    for (int i = 0; i < this.content.length; i++) {
      System.out.println(this.content[i]);
    }
  }
}
