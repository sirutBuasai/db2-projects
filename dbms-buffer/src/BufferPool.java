public class BufferPool {
  // Class attributes
  Frame[] buffers;

  // Constructor ------------------------------------------
  public BufferPool() {
    this.buffers = new Frame[1];
  }

  // Other methods ----------------------------------------
  public void initialize(int buffer_size) {
    this.buffers = new Frame[buffer_size];

    for (int i = 0; i < buffer_size; i++) {
      Frame f = new Frame(i+1);
      f.initialize();
      this.buffers[i] = f;
    }
  }
}
