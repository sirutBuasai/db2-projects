import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

// implements Iterator<String> to allow for reading the files as Strings
public class DBReader implements Iterator<String> {
  // Global variables
  private static int RECORDSIZE = 40;
  private int NUMRECORD = 100;

  // Class attributes
  private int num_files;
  private int curr_file;
  private int curr_record;
  private char[] block;

  /*
   * Constructor ------------------------------------------
   */
  public DBReader() {
    this.num_files = new File("Project2Dataset").listFiles().length;
    this.curr_file = 1;
    this.curr_record = 1;
    this.block = readFile(1);
  }

  /*
   * ------------------------------------------------------
   * Override hasNext in Iterator<String>
   * Returns true if the current record is not the last record, false otherwise
   * Argument: void
   * Return: boolean hasnext
   */
  @Override
  public boolean hasNext() {
    boolean hasnext = (this.curr_record == NUMRECORD) ? (this.curr_file != this.num_files) : true;
    return hasnext;
  }

  /*
   * ------------------------------------------------------
   * Override next in Iterator<String>
   * Returns the next record, if we have reached the last record, go to the first record of the next block
   * Argument: void
   * Return: String result
   */
  @Override
  public String next() {
    // initialize record and copy the block content to the record
    char[] record = new char[RECORDSIZE];
    System.arraycopy(this.block, (RECORDSIZE * (this.curr_record - 1)), record, 0, RECORDSIZE);
    // if last record is read, reset the metadata to the next block
    if (this.curr_record == NUMRECORD) {
      this.curr_record = 1;
      this.curr_file++;
      this.block = readFile(this.curr_file);
    }
    // increment the record pointer and return the record content
    else {
      this.curr_record++;
    }
    String result = String.valueOf(record);
    return result;
  }

  /*
   * ------------------------------------------------------
   * Read the file and extract content
   * Argument: int file_id
   * Return: char[] content
   */
  private char[] readFile(int file_id) {
    // initialize file Scanner for reading a file
    try (Scanner scanner = new Scanner(new File("Project2Dataset/F" + file_id + ".txt")).useDelimiter("\\Z")) {
      // set data read from scanner to char array content and return the content
      char[] content = scanner.next().toCharArray();
      return content;
    }
    // if file not found, throw error and return null
    catch (FileNotFoundException e) {
      // reset metadata
      this.curr_file = this.num_files;
      this.curr_record = NUMRECORD - 1;
      // throw error message
      System.err.println("Error: File F" + file_id + ".txt is not found.");
      e.printStackTrace();
      return null;
    }
  }

  /*
   * Static methods ---------------------------------------
   * Given a file_id and array of record_id, print out all the record content
   * Argument: int file_id, int[] record_arr
   * Return: void
   */
  public static void printRecords(int file_id, int[] record_arr) {
    // initialize file Scanner for reading a file
    try (Scanner scanner = new Scanner(new File("Project2Dataset/F" + file_id + ".txt")).useDelimiter("\\Z")) {
      // initialize block_content
      String block_content = scanner.next();
      // loop over each record in the record array
      for (int record_id : record_arr) {
        int r_start = (record_id - 1) * RECORDSIZE;
        int r_end = record_id * RECORDSIZE;
        System.out.println(block_content.substring(r_start, r_end));
      }
    }
    // if file not found, throw error
    catch (FileNotFoundException e) {
      System.out.println("File " + file_id + " could not be found");
      e.printStackTrace();
    }
  }

  /*
   * ------------------------------------------------------
   * Overload printRecord() method
   * Given a string of file_id:record_id, print out the record content in the string
   * Argument: String record_arr
   * Return: void
   */
  public static void printRecord(String record_arr) {
    String[] record_loc = record_arr.split(";");

    for (String location : record_loc) {
      // extract file_id values
      int file_id = Integer.parseInt(location.substring(0, 2));
      // extract record_id values
      location = location.substring(3);
      String[] records = location.split(",");
      int[] record_ids = new int[records.length];
      for (int i = 0; i < records.length; i++) {
        record_ids[i] = Integer.parseInt(records[i]);
      }
      // print records normally given file_id and record_ids
      DBReader.printRecords(file_id, record_ids);
    }
    // print metadata on number of files accessed
    System.out.println("Number of files accessed: " + record_loc.length);
  }
}
