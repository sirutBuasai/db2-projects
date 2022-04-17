import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

// By implementing the string iterator we have access to hasnext and next commands in different classes
public class DBReader implements Iterator<String> {
    private int numFiles;
    private int numRecords = 100;
    private int currFile;
    private int currRecord;
    private char[] block;
    private static int bytesPerRecord = 40;

    public DBReader(){
        numFiles = new File("Project2Dataset").listFiles().length;
        currFile = 1;
        currRecord = 1;
        block = readBlock(1);
    }

    // Returns true if current record is the last record, false otherwise
    // Override hasnext
    @Override
    public boolean hasNext(){
        if(currRecord == numRecords) return currFile != numFiles;
        return true;
    }

    // Override next
    @Override
    public String next(){
        char[] record = new char[bytesPerRecord];
        // Copy contents of one array to the other
        System.arraycopy(block, (bytesPerRecord * (currRecord - 1)), record, 0, bytesPerRecord);

        // If we reached last record, reset vars
        if(currRecord == numRecords){
            currRecord = 1;
            currFile++;
            block = readBlock(currFile);
        }

        // Increment current record and return the newly created record char array in string format
        currRecord++;
        return new String(record);
    }

    // Read fileID and return as char array
    private char[] readBlock(int fileID){
        Scanner s;
        // Try catch for file not found
        try{
            // Use delimeter to make sure it is end of input
            s = new Scanner(new File("Project2Dataset/F" + fileID + ".txt")).useDelimiter("\\Z");
        } catch  (FileNotFoundException e){
            // Print that a file could not be found and reset vars
            // (Basically just skip all the other files)
            System.out.println("File " + fileID + " could not be found");
            currFile = numFiles;
            currRecord = numRecords - 1;
            e.printStackTrace();
            return null;
        }

        // Otherwise, return char array as intended
        return s.next().toCharArray();
    }

    // Read array of record IDs
    protected static void printRecords(int fileID, int[] recordIDs){
        Scanner s;

        // Try catch for file not found
        try{
            s = new Scanner(new File("Project2Dataset/F" + fileID + ".txt")).useDelimiter("\\Z");
            String block = s.next();

            // Loop over record ids
            for(int rID : recordIDs) System.out.println(block.substring((rID - 1) * bytesPerRecord, rID * bytesPerRecord));

        } catch (FileNotFoundException e){
            System.out.println("File " + fileID + " could not be found");
            e.printStackTrace();
        }
    }

    // Prints record
    protected static void printRecord(String s){
        String[] fileAndRecords = s.split(";");

        for(String FAR : fileAndRecords){
            // Get file ID and do some string parsing to create an array of record IDs
            int fileID = Integer.parseInt(FAR.substring(0, 2));
            // Remove spaces, as they were causing some issues
            FAR = FAR.substring(3).replace(" ", "");
            String[] records = FAR.split(",");
            int[] recordIDs = new int[records.length];

            // Loop over records and add to array
            for(int i = 0; i < records.length; i++){
                recordIDs[i] = Integer.parseInt(records[i]);
            }

            // Print records
            DBReader.printRecords(fileID, recordIDs);
        }

        // Print number of files read
        System.out.println("Number of files read: " + fileAndRecords.length);
    }


}
