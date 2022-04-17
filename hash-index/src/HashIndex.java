import java.util.HashMap;

public class HashIndex {
    // Class attribute
    private HashMap<Integer, String> hash_map; // K: RandomV, V: record_id

    /*
     * Constructor ----------------------------------------
     */
    public HashIndex(){
      hash_map = new HashMap<Integer, String>();
    }

    /*
     * ----------------------------------------------------
     * Store the randomV as key and record_loc (in format = "file_id:record_id") as value
     * Argument: int randomV, String record_loc
     * Return: void
     */
    public void add(int randomV, String record_loc){
      // if the hash map already contains randomV as a key, append the record location
      if (hash_map.containsKey(randomV)) {
        // get the value of the current indexed randomV
        String curr_key = hash_map.get(randomV);
        // if the current value already as the file_id, append the record_id
        if (curr_key.contains(record_loc.substring(0, 3))) {
            int file_idx = curr_key.indexOf(record_loc.substring(0, 3));
            curr_key = curr_key.substring(0, file_idx + 3) + record_loc.substring(3) + "," + curr_key.substring(file_idx + 3);

            // Insert updated record
            hash_map.replace(randomV, curr_key);
        } else hash_map.replace(randomV, hash_map.get(randomV) + ";" + record_loc);
      } else hash_map.put(randomV, record_loc);
    }

    // Print records given a randomV value
    public void read(int randomV){
        // If hashmap does not contain key, say so
        if(!hash_map.containsKey(randomV)) System.out.println("No records found for randomV of " + randomV + "\nNo I/Os performed");
        else DBReader.printRecord(hash_map.get(randomV));
    }
}
