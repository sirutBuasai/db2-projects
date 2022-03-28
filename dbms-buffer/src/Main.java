import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

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

  /*
   * ------------------------------------------------------
   * Read file funciton
   */
  public static void readFile(String file_name) {
    try {
      // initialize file and scanner class
      File file = new File("./Project1/"+file_name+".txt");
      Scanner file_reader = new Scanner(file);
      String data = new String();
      // keep reading file until the end of the file
      while (file_reader.hasNextLine()) {
        // print out line by line
        data += file_reader.nextLine();
      }
      System.out.println(data);
      // close file reader
      file_reader.close();
    }
    catch (FileNotFoundException e) {
      System.err.println("Error: Cannot find or open file");
      e.printStackTrace();
    }
  }
}
