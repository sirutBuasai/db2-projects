public class ArrayIndex {

    private String[] array;

    protected ArrayIndex(){
        // Initialize the array with 5000 bytes
        array = new String[5000];
    }

    // Adds file and record ID string to array
    // Format: fileID:recordID
    protected void add(int randomV, String fileAndRecordID){
        // randomV starts at 1, so subtract 1 to make it 0-indexed for array
        randomV -= 1;
        // If entry doesn't exist
        if(array[randomV] == null) array[randomV] = fileAndRecordID;
        else {
            // Check if ":" is in string
            if(array[randomV].contains(fileAndRecordID.substring(0, 3))){
                int index = array[randomV].indexOf(fileAndRecordID.substring(0, 3));
                array[randomV] = array[randomV].substring(0, index + 3) + fileAndRecordID.substring(3) + ", " + array[randomV]
                        .substring(index + 3);

                // Otherwise, add semicolon to separate files
            } else array[randomV] = array[randomV] + ";" + fileAndRecordID;
        }
    }


    // Prints records with randomV value in given range
    public void readRange(int greaterThan, int lessThan){
        // Check edge cases
        if(greaterThan < 0 || lessThan < 0 || greaterThan > 5000){
            System.out.println("Invalid range, no records found.");
        }

        // Set less than equal to max if too high
        lessThan = lessThan > 5000 ? 5000 : lessThan;

        String fileRecord = "";

        for(int i = greaterThan; i < lessThan - 1; i++){
            if(array[i] != null){
                String[] files = array[i].split(";");

                // Loop over all files
                for(String s : files){
                    // If file is in record, add file index
                    if(fileRecord.contains(s.substring(0, 3))){
                        int fileIndex = fileRecord.indexOf(s.substring(0, 3));
                        fileRecord = fileRecord.substring(0, fileIndex + 3) + s.substring(3) + "," + fileRecord.substring
                                (fileIndex + 3);
                    } else fileRecord = fileRecord + ";" + s;
                }
            }
        }

        // Check if no records found
        if(fileRecord.length() < 1) System.out.println("No records found");

        // otherwise print the record
        else {
            fileRecord = fileRecord.substring(1);
            DBReader.printRecord(fileRecord);
        }


    }

}
