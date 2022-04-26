import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class Main {
  public static void main(String args[]) throws IOException {
    // initialize forever loop for the program
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    boolean active = true;
    System.out.println("Program initiated, waiting for command...");

    while (active) {
      System.out.print(">> ");
      // initialize scanner class for input handling
      // clean up input for better argument handling
      String input = reader.readLine().toUpperCase().replace(" ", "");
      // initialize timer
      long start = System.nanoTime();

      // switch case statements for handling different commands
      switch (input) {
        case "SELECTA.COL1,A.COL2,B.COL1,B.COL2FROMA,BWHEREA.RANDOMV=B.RANDOMV":
          System.out.println("1");
          // construct hash table
          // constructHashTable();
          // join tables
          // joinTables();
          break;

        case "SELECTCOUNT(*)FROMA,BWHEREA.RANDOMV>B.RANDOMV":
          System.out.println("1");
          // construct array
          // constructArray();
          // select count
          // selectCount();

          // Manual check: count seems reasonable considering the largest possible
          // join would be 9900^2 entries

          break;

        case "SELECTCOL2,SUM(RANDOMV)FROMAGROUPBYCOL2":
          // make hash table using names values
          // constructNamesHashTable("A");
          // aggreagte (sum)
          // aggregate(1);

          break;

        case "SELECTCOL2,SUM(RANDOMV)FROMBGROUPBYCOL2":
          // make hash table using names values
          // constructNamesHashTable("B");
          // aggregate (sum)
          // aggregate(1);

          break;

        case "SELECTCOL2,AVG(RANDOMV)FROMAGROUPBYCOL2":
          // make hash table using names values
          // constructNamesHashTable("A");
          // aggregate (avg)
          // aggregate(2);

          break;

        case "SELECTCOL2,AVG(RANDOMV)FROMBGROUPBYCOL2":
          // make hash table using names values
          // constructNamesHashTable("B");
          // aggregate (avg)
          // aggregate(2);

          break;

        case "EXIT":
          active = false;

        case "HELP":
          System.err.println("List of commands:");
          System.err.println("                SELECT A.COL1, A.COL2, B.COL1, B.COL2 FROM A, B WHERE A.RANDOMV = B.RANDOMV");
          System.err.println("                SELECT COUNT(*) FROM A, B WHERE A.RANDOMV > B.RANDOMV");
          System.err.println("                EXIT");
          System.out.println("-----------------------------------");
          

        default:
          System.err.println("Error: Invalid input.");
          System.err.println("Please check command input for typos.");
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
