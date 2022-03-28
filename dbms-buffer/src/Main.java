import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    // initialize global variables
    int buffer_size = 0;
    Scanner scanner = new Scanner(System.in);

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
    System.out.println("The program is ready for the next command");
    String command = scanner.nextLine();
    readFile(command);

    // Clean up
    scanner.close();
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
