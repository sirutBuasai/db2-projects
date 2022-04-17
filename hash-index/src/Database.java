import java.util.Iterator;

// Implement string iterable so we can use "this" as a string type
public class Database implements Iterable<String> {
    private HashIndex hashIndex;
    private ArrayIndex arrayIndex;

    // Called when "CREATE INDEX" is called, initializes the hash and array indexes
    public void createIndexes(){
        hashIndex = new HashIndex();
        arrayIndex = new ArrayIndex();

        // Loop over records
        for(String r : this){
            int randomV = Integer.parseInt(r.substring(33, 37));
            String fileAndRecordID = r.substring(1, 3) + ":" + r.substring(7, 10);

            // Add randomv and fileandrecordID elements to both indexes
            hashIndex.add(randomV, fileAndRecordID);
            arrayIndex.add(randomV, fileAndRecordID);
        }
        System.out.println("The hash-based and array-based indexes are built successfully.\nProgram is ready and waiting for user command");
    }

    // Main search function! The motherboard of the cpu, the controller of all searches
    // 0: equality search
    // 1: range search
    // 2: inequality table scan
    // 3: equality table scan
    // 4: range table scan
    // 5: inequality table scan
    protected void search(int searchType, int randomV1, int randomV2){
        // If no indexes, use table scans
        if(hashIndex == null){
            searchType += 3;
        }

        // Keep track of how long it takes
        long start = System.currentTimeMillis();

        // Analyze search type and use corresponding search method
        switch(searchType) {
            case 0:
                equalitySearch(randomV1);
                break;
            case 1:
                rangeSearch(randomV1, randomV2);
                break;
            case 2:
                inequalityTableScan(randomV1);
                break;
            case 3:
                equalityTableScan(randomV1);
                break;
            case 4:
                rangeTableScan(randomV1, randomV2);
                break;
            case 5:
                System.out.println("No index available...");
                inequalityTableScan(randomV1);
                break;

        }

        // Calculate total time and print it
        long totalTime = System.currentTimeMillis() - start;
        System.out.println("Search time: " + totalTime + "ms");
    }

    public void equalitySearch(int randomV){
        hashIndex.read(randomV);
        System.out.println("Used hash-based indexing");
    }

    public void rangeSearch(int greaterThan, int lessThan){
        arrayIndex.readRange(greaterThan, lessThan);
        System.out.println("Used array-based indexing");
    }

    public void equalityTableScan(int randomV){
        // Loop over records
        for(String r : this){
            if(Integer.parseInt(r.substring(33, 37)) == randomV){
                System.out.println(r);
            }
        }
        System.out.println("Full table scan completed (no index available)");
        System.out.println("99 files read");
    }

    public void rangeTableScan(int greaterThan, int lessThan){
        // Loop over records
        for(String r : this){
            if(Integer.parseInt(r.substring(33, 37)) > greaterThan &&
                    Integer.parseInt(r.substring(33, 37)) < lessThan) System.out.println(r);
        }

        System.out.println("Full table scan completed (no index available");
        System.out.println("99 files read");
    }

    public void inequalityTableScan(int randomV){
        // Loop over records
        for(String r : this){
            if(Integer.parseInt(r.substring(33, 37)) != randomV){
                System.out.println(r);
            }
        }
        System.out.println("Table scan was used");
        System.out.println("99 files read");
    }

    // Override the iterator so we can use "this" in for loops to refer to self as string array type
    @Override
    public Iterator<String> iterator(){
        return new DBReader();
    }

}
