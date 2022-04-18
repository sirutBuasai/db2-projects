import java.util.Iterator;
import java.util.concurrent.TimeUnit;

// implement Iterable<String> to allow for enhanced for loop with String
public class Database implements Iterable<String> {
  // Class attributes
  private HashIndex hash_idx;
  private ArrayIndex arr_idx;
  private boolean creation = false;

  /*
   * Constructor ------------------------------------------
   * Overide the iterator so that "this" keyword is used as Iterable<String> type
   */
  @Override
  public Iterator<String> iterator() {
    return new DBReader();
  }

  /*
   * ------------------------------------------------------
   * Initializes the has and array indices to store the RandomV as index when CREATE INDEX is called by the user
   * Argument: void
   * Return: void
   */
  public void createIndexes() {
    this.hash_idx = new HashIndex();
    this.arr_idx = new ArrayIndex();
    this.creation = true;

    // loop over the records read by the DBReader
    for (String record : this) {
      // extract K: randomV and V: record_loc from the record string
      int randomV = Integer.parseInt(record.substring(33, 37));
      String file_id = record.substring(1, 3);
      String record_id = record.substring(7, 10);
      String record_loc = file_id + ":" + record_id;

      // add randomV as key and record_loc as value to both hash and array indices
      this.hash_idx.add(randomV, record_loc);
      this.arr_idx.add(randomV, record_loc);
    }
    System.out.println("The hash-based and array-based indexes are built successfully.");
    System.out.println("Program is ready and waiting for user command.");
  }

  /*
   * Main search method -----------------------------------
   * Switch case for different search types
   * 0: equality search
   * 1: range search
   * 2: inequality table scan
   * 3: equality table scan
   * 4: range table scan
   * 5: inequality table scan
   * Argument: int type, int key1: always required, int key2: required for range
   * search
   * Return: void
   */
  public void search(int type, int key1, int key2) {
    // if the indices has not been created, use table scans
    type = creation ? type : type + 3;
    // start timer
    long start = System.nanoTime();
    // switch case according to type of searches
    switch (type) {
      case 0:
        equalitySearch(key1);
        break;
      case 1:
        rangeSearch(key1, key2);
        break;
      case 2:
        inequalityTableScan(key1);
        break;
      case 3:
        equalityTableScan(key1);
        break;
      case 4:
        rangeTableScan(key1, key2);
        break;
      case 5:
        inequalityTableScan(key1);
        break;
    }
    // calculate and print total time in ms
    long total = TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS);
    System.out.println("Query time: " + total + "ms");
  }

  /*
   * ------------------------------------------------------
   * Perform equality search using hash indexing
   * Argument: int key: randomV value
   * Return: void
   */
  public void equalitySearch(int key) {
    this.hash_idx.read(key);
    System.out.println("Used hash indexing");
  }

  /*
   * ------------------------------------------------------
   * Perform range search using array indexing
   * Argument: int m: greater than value, int n: less than value
   * Return: void
   */
  public void rangeSearch(int m, int n) {
    this.arr_idx.readRange(m, n);
    System.out.println("Used array indexing");
  }

  /*
   * ------------------------------------------------------
   * Perform equality search using table scan
   * Argument: int key: randomV value
   * Return: void
   */
  public void equalityTableScan(int key) {
    // loop over all the records in all the files
    double num_files = 0;
    for (String record : this) {
      num_files++;
      int randomV = Integer.parseInt(record.substring(33, 37));
      if (randomV == key) {
        System.out.println(record);
      }
    }
    System.out.println("Used full table scan");
    System.out.println("Number of files read: " + (int) Math.ceil(num_files / 100));
  }

  /*
   * ------------------------------------------------------
   * Perform range search using table scan
   * Argument: int m: greater than value, int n: less than value
   * Return: void
   */
  public void rangeTableScan(int m, int n) {
    // loop over all the records in all the files
    double num_files = 0;
    for (String record : this) {
      num_files++;
      int randomV = Integer.parseInt(record.substring(33, 37));
      if (randomV > m && randomV < n) {
        System.out.println(record);
      }
    }
    System.out.println("Used full table scan");
    System.out.println("Number of files read: " + (int) Math.ceil(num_files / 100));
  }

  /*
   * ------------------------------------------------------
   * Perform inequality search using table scan
   * Argument: int key: randomV value
   * Return: void
   */
  public void inequalityTableScan(int key) {
    // loop over all the records in all the files
    double num_files = 0;
    for (String record : this) {
      num_files++;
      int randomV = Integer.parseInt(record.substring(33, 37));
      if (randomV != key) {
        System.out.println(record);
      }
    }
    System.out.println("Used full table scan");
    System.out.println("Number of files read: " + (int) Math.ceil(num_files / 100));
  }
}
