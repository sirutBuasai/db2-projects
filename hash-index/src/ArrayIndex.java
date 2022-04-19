public class ArrayIndex {
  // Class attributes
  private String[] array;

  /*
   * Constructor ------------------------------------------
   */
  public ArrayIndex() {
    this.array = new String[5000];
  }

  /*
   * ----------------------------------------------------
   * Store the randomV as key and record_loc (in format = "file_id:record_id") as value
   * Argument: int randomV, String record_loc
   * Return: void
   */
  public void add(int randomV, String record_loc) {
    // calculate appropriate 0-indexed array
    randomV -= 1;
    // if the given randomV does not exist, add the new record
    if (this.array[randomV] == null) {
      this.array[randomV] = record_loc;
    }
    // otherwise, append the new record to the existing array
    else {
      String curr_idx = this.array[randomV];
      // if the current value already has the file_id, append only the record_id
      if (curr_idx.contains(record_loc.substring(0, 3))) {
        // find the index of the file that the current record is inserting to
        int file_idx = curr_idx.indexOf(record_loc.substring(0, 3));
        // append the curr_record to the correct file_id given by the file_idx
        // curr_key = original_data + curr_record + , + original_record
        String original_data = curr_idx.substring(0, file_idx + 3);
        String curr_record = record_loc.substring(3);
        String original_record = curr_idx.substring(file_idx + 3);
        this.array[randomV] = original_data + curr_record + "," + original_record;
      }
      // otherwise, append the both file_id and record_id
      else {
        this.array[randomV] = this.array[randomV] + ";" + record_loc;
      }
    }
  }

  /*
   * ------------------------------------------------------
   * Get all the records within the given key range
   * Argument: int m: greater than value, int n: less than value)
   * return: void
   */
  // Prints records with randomV value in given range
  public void readRange(int m, int n) {
    // cap the maximum range to 5000 and the minimum range to 0
    n = n > 5000 ? 5000 : n;
    m = m < 0 ? 0 : m;
    // Check if the given range is valid or not
    if (n < 0 || m > 5000 || m > n) {
      System.err.println("Given range is invalid.");
    }
    // initialize the file_record
    String output = "";
    // loop through the data file within the given range
    for (int i = m; i < n - 1; i++) {
      if (this.array[i] != null) {
        // split the files according to the given semi-colon
        String[] files = this.array[i].split(";");
        // loop over the values in each file
        for (String f : files) {
          // if the file is already in output, append the record to the output
          if (output.contains(f.substring(0, 3))) {
            int file_idx = output.indexOf(f.substring(0, 3));
            // append the curr_record to the correct file_id given by the file_idx
            // output = original_data + curr_record + , + original_record
            String original_data = output.substring(0, file_idx + 3);
            String curr_record = f.substring(3);
            String original_record = output.substring(file_idx + 3);
            output = original_data + curr_record + "," + original_record;
          }
          // otherwise, add both file and record to the output
          else {
            output = output + ";" + f;
          }
        }
      }
    }
    // if no record is found, print error
    if (output.length() < 1) {
      System.err.println("No records found");
    }
    // otherwise print the record
    else {
      output = output.substring(1);
      DBReader.printRecord(output);
    }
  }
}
