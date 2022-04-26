import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Main {
    // For hash based join we only need to keep a hash table for one dataset:
    // create hash table for the dataset, call it A
    static ArrayList<LinkedList<String>> tableA = new ArrayList<LinkedList<String>>(Collections.nCopies(500, new LinkedList<String>()));
    static ArrayList<LinkedList<Integer>> tableAInt = new ArrayList<LinkedList<Integer>>(Collections.nCopies(500, new LinkedList<Integer>()));
    // keep an array of the data, 9900 = 99 files * 100 records each
    static String[] dataA = new String[9900];
    // num buckets, want 500 in the table
    static final int numBuckets = 500;

    // keep global variable for number of bytes per record
    static final int numBytes = 40;


    public static void main(String args[]){
        handleInput();
    }



    // function to handle user input
    private static void handleInput(){
        System.out.print("Please enter a command: ");

        // get command
        Scanner s = new Scanner(System.in);
        // format command to avoid typos
        String command = s.nextLine().toLowerCase().replaceAll(" ", "");

        // variables for timer
        long start;
        long totalTime;

        //System.out.println("Entered command: " + command);

        // start timer
        start = System.currentTimeMillis();

        // switch case statements for handling different commands
        switch(command){

            case "selecta.col1,a.col2,b.col1,b.col2froma,bwherea.randomv=b.randomv":

                // construct hash table
                constructHashTable();
                // join tables
                joinTables();

                // quick manual check...
                /*
                A95 Rec 49 RandomV = 0099
                B99 Rec 100 RandomV = 0099
                Looks like it works!
                 */

                break;

            case "selectcount(*)froma,bwherea.randomv>b.randomv":

                // construct array
                constructArray();
                // select count
                selectCount();

                // Manual check: count seems reasonable considering the largest possible
                // join would be 9900^2 entries

                break;

            case "selectcol2,sum(randomv)fromagroupbycol2":

                // make hash table using names values
                constructNamesHashTable("A");
                // aggreagte (sum)
                aggregate(1);

                break;

            case "selectcol2,sum(randomv)frombgroupbycol2":

                // make hash table using names values
                constructNamesHashTable("B");
                // aggregate (sum)
                aggregate(1);

                break;

            case "selectcol2,avg(randomv)fromagroupbycol2":

                // make hash table using names values
                constructNamesHashTable("A");
                // aggregate (avg)
                aggregate(2);

                break;

            case "selectcol2,avg(randomv)frombgroupbycol2":

                // make hash table using names values
                constructNamesHashTable("B");
                // aggregate (avg)
                aggregate(2);

                break;

            case "exit":
                return;


            default:
                System.out.println("Command not recognized, check spelling.");
                break;

        }

        // stop timer
        totalTime = System.currentTimeMillis() - start;
        System.out.println("Command ran in " + totalTime + "ms");

        // handle input again
        handleInput();
    }


    // gets record from given file
    private static String getRecord(String data, int recordNum) {
        // Substring from record num to number of bytes + record num, giving one full record
        String record = data.substring(numBytes * recordNum, numBytes * recordNum + numBytes);
        return record;
    }


    // gets randomV value from given record
    // (making a function for this since last project was messy)
    private static int getRandomV(String record) {
        int randomV = Integer.parseInt(record.substring(33, 37));
        return randomV;
    }

    // gets name value from given record
    private static int getNameNum(String record) {
        int nameNum = Integer.parseInt(record.substring(16, 19));
        return nameNum;
    }


    // main hashing function, simply takes randomV mod numBuckets to get bucket value/address
    private static int hash(int randomV) {
        int bucketValue = randomV % numBuckets;
        return bucketValue;
    }


    // constructs hash table for A
    private static void constructHashTable(){
        // clear previous hash table
        tableA = new ArrayList<LinkedList<String>>(Collections.nCopies(500, new LinkedList<String>()));

        try {
            // loop over all files in dataset
            for(int i = 1; i < 100; i++){
                // get file name
                String fName = System.getProperty("user.dir") + "/Project3Dataset-A/A" + i + ".txt";



                // create new scanner and read the file
                Scanner s = new Scanner(new File(fName));
                String tempData = s.nextLine();
                s.close();

                // loop over all records in file
                for (int j = 0; j < 100; j++) {
                    // get record using above function
                    String record = getRecord(tempData, j);
                    // get randomV value
                    int randomV = getRandomV(record);

                    // add value to hash table
                    put(randomV, record);
                }
            }
        } catch(IOException e){
            System.out.println("Invalid file name, file not found");
        }
    }


    // constructs hash table over data set A or B for the names column
    private static void constructNamesHashTable(String dataset){
        //clear previous hash table
        tableA = new ArrayList<LinkedList<String>>(Collections.nCopies(500, new LinkedList<String>()));

        try {
            // loop over all files in dataset
            for(int i = 1; i < 100; i++){
                // get file name
                String fName = System.getProperty("user.dir") + "/Project3Dataset-" + dataset + "/" + dataset + i + ".txt";

                // create new scanner and read the file
                Scanner s = new Scanner(new File(fName));
                String tempData = s.nextLine();
                s.close();

                // loop over all records in file
                for (int j = 0; j < 100; j++) {
                    // get record using above function
                    String record = getRecord(tempData, j);
                    // get randomV value
                    int randomV = getRandomV(record);
                    // get name value
                    int nameNum = getNameNum(record);

                    // add value to hash table
                    putInt(nameNum, randomV);
                }
            }
        } catch(IOException e){
            System.out.println("Invalid file name, file not found");
        }
    }


    // puts value into hash table for A
    private static void put(int key, String value) {
        // hash the randomV (key = randomV)
        int bucketValue = hash(key);

        // if the bucket is empty, initialize it
        if (tableA.get(bucketValue) == null) {
            tableA.set(bucketValue, new LinkedList<>());
            System.out.println(bucketValue);
        }


        // add value to bucket
        tableA.get(bucketValue).add(value);
    }

    // puts value into hash table for A, same as above but for int
    private static void putInt(int key, int value) {
        // hash the randomV (key = randomV)
        int bucketValue = hash(key);

        // if the bucket is empty, initialize it
        if (tableAInt.get(bucketValue) == null) tableAInt.set(bucketValue, new LinkedList<Integer>());

        // add value to bucket
        tableAInt.get(bucketValue).add(value);
    }




    // constructs the array of records, dataA
    private static void constructArray() {
        // try catch for file not found exception
        try {
            // loop over all files in directory
            for (int i = 1; i < 100; i++) {
                // get file name
                String fName = System.getProperty("user.dir") + "/Project3Dataset-A/A" + i + ".txt";

                // create new scanner and read the file
                Scanner s = new Scanner(new File(fName));
                String tempData = s.nextLine();
                s.close();

                // loop over all records in file
                for (int j = 0; j < 100; j++) {
                    // get record using above function
                    String record = getRecord(tempData, j);

                    // set dataA value
                    dataA[(i - 1) * 100 + j] = record;
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }

    }


    // select range search (e.g. select count(*) from A, B, where a.randV > b.randV)
    // kind of a hack, but we don't need arguments since there is only one configuration we check for
    private static void selectCount() {
        // store count of valid records
        int count = 0;
        try {
            // loop over files, same thing as constructArray
            for (int i = 1; i < 100; i++) {
                // get file name
                String fName = System.getProperty("user.dir") + "/Project3Dataset-A/A" + i + ".txt";


                // create new scanner and read the file
                Scanner s = new Scanner(new File(fName));
                String tempData = s.nextLine();
                s.close();

                // loop over all records in file
                for (int j = 0; j < 100; j++) {
                    // get record (B)
                    String recordB = getRecord(tempData, j);
                    // get randomV
                    int randomVB = getRandomV(recordB);

                    // loop over array with data for table A and perform range search
                    for(int k = 0; k < dataA.length; k++){
                        String recordA = dataA[i];
                        int randomVA = getRandomV(recordA);

                        // if a.randomV > b.randomV, count that record
                        count += randomVA > randomVB ? 1 : 0;
                    }
                }

            }
            // print result of count
            System.out.println("Num qualifying records: " + count);
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }


    // joins the two tables on the randomV col
    private static void joinTables(){
        // print header
        System.out.println("A.Col1   A.Col2      B.Col1   B.Col2");
        try {
            // loop over files, same thing as constructArray
            for (int i = 1; i < 100; i++) {
                // get file name
                String fName = System.getProperty("user.dir") + "/Project3Dataset-B/B" + i + ".txt";

                // create new scanner and read the file
                Scanner s = new Scanner(new File(fName));
                String tempData = s.nextLine();
                s.close();

                // loop over all records in file
                for (int j = 0; j < 100; j++) {
                    // get record (B)
                    String recordB = getRecord(tempData, j);
                    // get randomV
                    int randomVB = getRandomV(recordB);
                    // get bucketvalue from hash function
                    int bucketValue = hash(randomVB);

                    // loop over records in tableA hash table
                    for(String recordA : tableA.get(bucketValue)) {
                        // get randomV for A
                        int randomVA = getRandomV(recordA);

                        // if the randomV values match, splot cols and print joined string
                        if(randomVA == randomVB){
                            String[] colsA = recordA.split(", ");
                            String[] colsB = recordB.split(", ");

                            // print output
                            System.out.println(colsA[0] + "   " + colsA[1] + "      " + colsB[0] + "   " + colsB[1]);
                        }
                    }


                }

            }
        } catch(FileNotFoundException e){
            System.out.println("File not found!");
        }
    }


    // performs aggregation operator, 1 for sum 2 for average
    private static void aggregate(int type){

        // print header
        System.out.println("Name      Aggregation");

        for(int i = 0; i < tableAInt.size(); i++){
            // i = hashed value, aka the number of the record
            // the array list associated with tableAInt[i] is the list we need to aggregate
            int aggregated = 0;
            // loop over list associated with bucket number
            if(tableAInt.get(i) != null) {
                for (int x : tableAInt.get(i)) {
                    aggregated += x;
                }

                // if we need to average, divide by bucket size
                if (type == 2) {
                    aggregated /= tableAInt.get(i).size();
                }

                // print aggregated values
                System.out.println("Name" + (i < 100 ? i < 10 ? "00" : 0 : "") + i + "   " + aggregated);
            }
        }
    }
}
