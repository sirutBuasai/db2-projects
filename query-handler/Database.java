import java.util.*;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;

public class Database {
  // Global variables
  private int NUMBUCKET = 50;
  private int NUMRECORD = 100;
  private int NUMFILE = 99;
  private int RECORDSIZE = 40;

  // Class attributes
  private ArrayList<LinkedList<String>> hash_table;
  private String[] data_array;

  /*
   * Constructor ------------------------------------------
   */
  public Database() {
    this.hash_table = new ArrayList<LinkedList<String>>(Collections.nCopies(NUMBUCKET, new LinkedList<String>()));
    this.data_array = new String[NUMFILE*NUMRECORD];
  }

  /*
   * ------------------------------------------------------
   * Hash function to convert randomV to bucket number
   * Argument: int randomV
   * Return: int bucket_num
   */
  private int hashFunc(int randomV) {
    // restrict the bucket_num value using mod
    int bucket_num = randomV % NUMBUCKET;
    return bucket_num;
  }

  /*
   * ------------------------------------------------------
   * Given a key:value pair, put the content into the hash table
   * Argument: int key, String value
   * Return: void
   */
  private void hashPut(int key, String value) {
    // pass randomV into the hash function to get the bucket number
    int bucket_num = hashFunc(key);
    // put the key:value pair into the hash table
    this.hash_table.get(bucket_num).add(value);
  }

  /*
   * ------------------------------------------------------
   * Get a record content given a file and the record number
   * Argument: String file_content, int r_num
   * Return: String record_content
   */
  private String getRecord(String file_content, int r_num) {
    // given that r_num is already 0-indexed, we can calculate for the starting and ending index of the record
    int start_idx = RECORDSIZE * r_num;
    int end_idx = start_idx + RECORDSIZE;
    // retrieve record content using substring
    String record_content = file_content.substring(start_idx, end_idx);
    return record_content;
  }

  /*
   * ------------------------------------------------------
   * Get a record randomV given a record content
   * Argument: String record_content
   * Return: String randomV
   */
  private int getRandomV(String record) {
    // randomV is always at position record_content[33:37]
    int randomV = Integer.parseInt(record.substring(33, 37));
    return randomV;
  }

  /*
   * ------------------------------------------------------
   * Construct a hash table for relation A
   * Argument: void
   * Return: void
   */
  public void buildTable() {
    // reinitialize the hast table
    this.hash_table = new ArrayList<LinkedList<String>>(Collections.nCopies(NUMBUCKET, new LinkedList<String>()));
    try {
      // loop over all files in dataset
      for (int i = 0; i < NUMFILE; i++) {
        // initialize file name
        String file_name = "Project3Dataset-A/A" + (i+1) + ".txt";
        // read file content
        Scanner scanner = new Scanner(new File(file_name));
        String file_content = scanner.nextLine();
        scanner.close();
        // loop over all records in the current file
        for (int j = 0; j < NUMRECORD; j++) {
          // retrieve record content and its randomV
          String curr_record = getRecord(file_content, j);
          int curr_randomV = getRandomV(curr_record);
          // add value to hash table
          hashPut(curr_randomV, curr_record);
        }
      }
    } catch (IOException e) {
      System.err.println("Error: Cannot open file.");
    }
  }

  /*
   * ------------------------------------------------------
   * Join relation A and B together on column randomV
   * Argument: void
   * Return: void
   */
  public void joinTable() {
    // print column fields
    System.out.println("A.Col1       A.Col2       B.Col1       B.Col2");
    try {
      // loop over all files in dataset
      for (int i = 0; i < NUMFILE; i++) {
        // initialize file name
        String file_name = "Project3Dataset-B/B" + (i+1) + ".txt";
        // read file content
        Scanner scanner = new Scanner(new File(file_name));
        String file_content = scanner.nextLine();
        scanner.close();
        // loop over all records in the current file
        for (int j = 0; j < NUMRECORD; j++) {
          // retrieve record content and its randomV
          String record_b = getRecord(file_content, j);
          int randomV_b = getRandomV(record_b);
          // get bucket_num from randomV
          int search_bucket = hashFunc(randomV_b);
          // loop over records in relation A hash table
          for (String record_a : this.hash_table.get(search_bucket)) {
            int randomV_a = getRandomV(record_a);
            if (randomV_a == randomV_b) {
              String[] colsA = record_a.split(", ");
              String[] colsB = record_b.split(", ");
              // print output
              System.out.println(colsA[0] + "   " + colsA[1] + "      " + colsB[0] + "   " + colsB[1]);
            }
          }
        }
      }
    } catch (FileNotFoundException e) {
      System.out.println("File not found!");
    }
  }

  /*
   * ------------------------------------------------------
   * Loops over relation A and store it in memory
   * Argument: void
   * Return: void
   */
  public void buildArray() {
    try {
      // loop over all files in dataset
      for (int i = 0; i < NUMFILE; i++) {
        // initialize file name
        String file_name = "Project3Dataset-A/A" + (i+1) + ".txt";
        // read file content
        Scanner scanner = new Scanner(new File(file_name));
        String file_content = scanner.nextLine();
        scanner.close();
        // loop over all records in the current file
        for (int j = 0; j < NUMRECORD; j++) {
          // retrieve record content and its randomV
          String curr_record = getRecord(file_content, j);
          int rr_num = (i*NUMRECORD)+j;
          // store record content in memory
          this.data_array[rr_num] = curr_record;
        }
      }
    } catch (FileNotFoundException e) {
      System.err.println("Error: Cannot find file");
    }
  }

  /*
   * ------------------------------------------------------
   * Count the number of tuples with the given condition A.randomV > B.randomV
   * Argument: void
   * Return: void
   */
  public void countStar() {
    // initialize total count variable
    int count = 0;
    try {
      // loop over all files in dataset
      for (int i = 0; i < NUMFILE; i++) {
        // initialize file name
        String file_name = "Project3Dataset-A/A" + (i+1) + ".txt";
        // read file content
        Scanner scanner = new Scanner(new File(file_name));
        String file_content = scanner.nextLine();
        scanner.close();
        // loop over all records in the current file
        for (int j = 0; j < NUMRECORD; j++) {
          // retrieve record content and its randomV
          String record_b = getRecord(file_content, j);
          int randomV_b = getRandomV(record_b);
          // loop over data in relation A stored in memory
          for (int k = 0; k < this.data_array.length; k++) {
            String record_a = this.data_array[k];
            int recordV_a = getRandomV(record_a);
            // increment the total count given the condition A.randomV > B.randomV
            if (recordV_a > randomV_b) {
              count++;
            }
          }
        }
      }
      // print output
      System.out.println("Count: " + count);
    } catch (FileNotFoundException e) {
      System.out.println("File not found!");
    }
  }
}
