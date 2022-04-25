import java.util.HashMap;

public class HashIndex {
  // Class attribute
  private HashMap<Integer, String> hash_map; // K: RandomV, V: record_id

  /*
   * Constructor ----------------------------------------
   */
  public HashIndex() {
    hash_map = new HashMap<Integer, String>();
  }

  /*
   * ----------------------------------------------------
   * Store the randomV as key and record_loc (in format = "file_id:record_id") as value
   * Argument: int randomV, String record_loc
   * Return: void
   */
  public void add(int randomV, String record_loc) {
    // if the hash_map already contains randomV as a key, append the record location
    if (hash_map.containsKey(randomV)) {
      // get the value of the current indexed randomV
      String curr_key = hash_map.get(randomV);
      // if the current value already as the file_id, append the record_id
      if (curr_key.contains(record_loc.substring(0, 3))) {
        // find the index of the file that the inserting record is in
        int file_idx = curr_key.indexOf(record_loc.substring(0, 3));
        // append the curr_record to the correct file_id given by the file_idx
        // curr_key = original_data + curr_record + , + original_record
        String original_data = curr_key.substring(0, file_idx + 3);
        String curr_record = record_loc.substring(3);
        String original_record = curr_key.substring(file_idx + 3);
        curr_key = original_data + curr_record + "," + original_record;
        // update the key with the new value
        hash_map.replace(randomV, curr_key);
      }
      // if the file_id is not in the hash_map, append both the file_id and record_id
      else {
        hash_map.replace(randomV, curr_key + ";" + record_loc);
      }
    }
    // if the hash_map does not contain the given randomV as a key, add it to the map
    else {
      hash_map.put(randomV, record_loc);
    }
  }

  /*
   * ------------------------------------------------------
   * Get all the records with the given key of RandomV
   * Argument: int RandomV
   * Return: void
   */
  public void read(int randomV) {
    // if hashmap does not contain key, say so
    if (!hash_map.containsKey(randomV)) {
      System.err.println("Cannot find records with randomV of " + randomV);
      System.err.println("No I/O performed.");
    } else {
      DBReader.printRecord(hash_map.get(randomV));
    }
  }
}
