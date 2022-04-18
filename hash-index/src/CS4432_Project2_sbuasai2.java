import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CS4342_Project2_sbuasai2 {

    /*
    Since input is limited to just a few commands, I have basically hardcoded them.
    To do this, I convert everything to lowercase and remove spaces then check equality.
    This of course assumes that the user inputs the commands correctly, meaning typos where
    spaces are omitted will be essentially ignored since I remove spaces before processing
    the string anyways. So in reality... it's a feature!
     */

    // Have to throw IOException for buffered reader shenanigans
    public static void main(String[] args) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
       Database db = new Database();
        System.out.println("Program is ready and waiting for user command");

        // Keep it running until quit
        while(true){
            String input = reader.readLine().toLowerCase();
            input = input.replace(" ", "");

            // Parse input, this was loads of fun
            if(input.contains("createindexonproject2dataset")) db.createIndexes();
            else if(input.contains("select*fromproject2datasetwhererandomv!=")) db.search(2, Integer.parseInt(input.replace
                    ("select*fromproject2datasetwhererandomv!=", "")), 0);
            else if(input.contains("select*fromproject2datasetwhererandomv=")) db.search(0, Integer.parseInt(input.replace
                    ("select*fromproject2datasetwhererandomv=", "")), 0);
            else if(input.contains("select*fromproject2datasetwhererandomv>") && input.contains("andrandomv<")){
                input = input.replace("select*fromproject2datasetwhererandomv>", "");
                String[] range = input.split("andrandomv<");
                System.out.println("First: x" + range[0] + "x Second: x" + range[1] + "x");
                db.search(1, Integer.parseInt(range[0]), Integer.parseInt(range[1]));
            } else if(input.contains("quit") || input.contains("exit")){
                System.out.println("Exiting");
                break;
            } else System.out.println("Unrecognized input, please try again");
        }

    }
} 
