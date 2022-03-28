import java.util.Scanner;

public class CS4432_Project1_sbuasai2 {
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
    // prompts for user commands
    System.out.println("The program is ready for the next command");
    String input = scanner.nextLine();
    String[] command = input.split("\\s+");

    switch (command[0]) {
      case "GET":
        System.out.println("MODE GET");
        System.out.println(command[1]);
        break;

      case "SET":
        System.out.println("MODE SET");
        System.out.println(command[1]);
        break;

      case "PIN":
        System.out.println("MODE PIN");
        System.out.println(command[1]);
        break;

      case "UNPIN":
        System.out.println("MODE UNPIN");
        System.out.println(command[1]);
        break;

      default:
        printHelp();
        break;
    }

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
