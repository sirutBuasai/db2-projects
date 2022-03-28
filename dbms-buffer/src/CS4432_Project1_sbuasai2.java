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

    // initialize the buffer pooland global variables
    BufferPool bp = new BufferPool();
    bp.initialize(buffer_size);
    int rr_num;
    int block_id;
    char[] record_content;
    boolean active = true;
    System.out.println("The program is ready for the next command");

    while(active){
      // prompts for user commands
      System.out.print("> ");
      // parse input
      String input = scanner.nextLine();
      String[] command = input.split("\\s+");

      // parse commands
      switch(command[0].toUpperCase()){
        case "GET":
          // get the record content given a record number
          rr_num = Integer.parseInt(command[1]);
          bp.get(rr_num);
          break;

        case "SET":
          // set the record content to the new content given a recrd number
          rr_num = Integer.parseInt(command[1]);
          record_content = command[2].toCharArray();
          // bp.set(rr_num, record_content);
          break;

        case "PIN":
          // pin the frame given a block id
          block_id = Integer.parseInt(command[1]);
          // bp.pin(block_id);
          break;

        case "UNPIN":
          // unpin the frame given a block id
          block_id = Integer.parseInt(command[1]);
          // bp.unpin(block_id);
          break;

        case "HELP":
          printHelp();
          break;

        case "EXIT":
          active = false;
          break;

        default:
          System.err.println("Program does not recognize the command.");
          printHelp();
          break;
      }
    }
    // Clean up
    scanner.close();
    System.out.println("Program terminated");
  }

  /*
   * ------------------------------------------------------
   * General help function
   */
  public static void printHelp() {
    System.err.println("List of commands:");
    System.err.println("                GET <record_num>");
    System.err.println("                SET <record_num> <40 byte string>");
    System.err.println("                PIN <block_id>");
    System.err.println("                UNPIN <block_id>");
    System.err.println("                HELP");
    System.err.println("                EXIT");
  }
}
