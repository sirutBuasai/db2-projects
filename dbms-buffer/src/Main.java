public class Main {
  public static void main(String[] args) {
    // initialize buffer size
    int buffer_size = 0;
    // argument handling
    switch (args.length) {
      case 1:
        buffer_size = Integer.parseInt(args[0]);
        break;

      default:
        printHelp();
        break;
    }
    // initialize the buffer pool
    BufferPool bp = new BufferPool();
    bp.initialize(buffer_size);
  }

  /*
   * ------------------------------------------------------
   * General help function
   */
  public static void printHelp() {
    System.err.println("Help message.");
    System.exit(1);
  }
}
