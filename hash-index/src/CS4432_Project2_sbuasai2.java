import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CS4432_Project2_sbuasai2 {
  public static void main(String[] args) throws IOException {
    // initialize user input reader and database object, and other global variables
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    Database db = new Database();
    boolean active = true;
    System.out.println("Program is ready and waiting for user command");
    System.out.println("-----------------------------------");

    while (active) {
      // clean input and set to upper case for standardized strings
      String input = reader.readLine().toUpperCase();

      // input parsing
      // create hash and arra indices
      if (input.contains("CREATE INDEX ON PROJECT2DATASET")) {
        db.createIndex();
      }
      // equality search
      else if (input.contains("SELECT * FROM PROJECT2DATASET WHERE RANDOMV =")) {
        db.search(0, Integer.parseInt(input.replace("SELECT * FROM PROJECT2DATASET WHERE RANDOMV = ", "")), 0);

      }
      // inequality search
      else if (input.contains("SELECT * FROM PROJECT2DATASET WHERE RANDOMV !=")) {
        db.search(2, Integer.parseInt(input.replace("SELECT * FROM PROJECT2DATASET WHERE RANDOMV != ", "")), 0);
      }
      // range search
      else if (input.contains("SELECT * FROM PROJECT2DATASET WHERE RANDOMV >") && input.contains("AND RANDOMV <")) {
        // parse input to get the range
        input = input.replace("SELECT * FROM PROJECT2DATASET WHERE RANDOMV > ", "");
        String[] range = input.split(" AND RANDOMV < ");
        // search given the specified range
        System.out.println("Starting range: " + range[0] + " Ending range: " + range[1]);
        int v1 = Integer.parseInt(range[0]);
        int v2 = Integer.parseInt(range[1]);
        db.search(1, v1, v2);
      }
      // exit the program
      else if (input.contains("EXIT")) {
        System.out.println("Database terminated.");
        break;
      }
      // help cases
      else {
        System.err.println("Error: Invalid input.");
        System.err.println("Commands are space-sensitive, please type in commands exactly as shown below.");
        System.err.println("List of commands:");
        System.err.println("                CREATE INDEX ON Project2Dataset (RandomV)");
        System.err.println("                SELECT * FROM Project2Dataset WHERE RandomV = <int>");
        System.err.println("                SELECT * FROM Project2Dataset WHERE RandomV > <int> AND RandomV < <int>");
        System.err.println("                SELECT * FROM Project2Dataset WHERE RandomV != <int>");
        System.err.println("                EXIT");
        System.out.println("-----------------------------------");
      }
    }
  }
}
