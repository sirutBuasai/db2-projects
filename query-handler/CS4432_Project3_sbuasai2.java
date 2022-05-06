import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class CS4432_Project3_sbuasai2 {
  public static void main(String args[]) throws IOException {
    // initialize variables required for the system
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    boolean active = true;
    Database db = new Database();
    System.out.println("Program initiated, waiting for command...");

    // initialize forever loop for the program
    while (active) {
      System.out.print(">> ");
      // initialize scanner class for input handling
      // clean up input for better argument handling
      String input = reader.readLine().toUpperCase();
      // initialize timer
      long start = System.nanoTime();

      // switch case statements for handling different commands
      switch (input) {
        case "SELECT A.COL1, A.COL2, B.COL1, B.COL2 FROM A, B WHERE A.RANDOMV = B.RANDOMV":
          db.buildTable();
          db.joinTable();
          break;

        case "SELECT COUNT(*) FROM A, B WHERE A.RANDOMV > B.RANDOMV":
          db.buildArray();
          db.countStar();
          break;

        case "EXIT":
          active = false;
          break;

        case "HELP":
          System.out.println("List of commands:");
          System.out.println("                SELECT A.COL1, A.COL2, B.COL1, B.COL2 FROM A, B WHERE A.RANDOMV = B.RANDOMV");
          System.out.println("                SELECT COUNT(*) FROM A, B WHERE A.RANDOMV > B.RANDOMV");
          System.out.println("                EXIT");
          System.out.println("-----------------------------------");
          break;

        default:
          System.err.println("Error: Invalid input.");
          System.err.println("Commands are space-sensitive, please type in commands exactly as shown below.");
          System.err.println("List of commands:");
          System.err.println("                SELECT A.COL1, A.COL2, B.COL1, B.COL2 FROM A, B WHERE A.RANDOMV = B.RANDOMV");
          System.err.println("                SELECT COUNT(*) FROM A, B WHERE A.RANDOMV > B.RANDOMV");
          System.err.println("                EXIT");
          System.out.println("-----------------------------------");
          break;
      }
      // calculate and print total time in ms
      long total = TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS);
      System.out.println("Query time: " + total + "ms");
      System.out.println("-----------------------------------");
    }
  }
}
