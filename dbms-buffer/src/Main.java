public class Main {
  public static void main(String[] args) {
    Frame f = new Frame(Integer.valueOf(args[0]));
    f.initialize();
    Record[] r_arr = f.getContent();
    System.out.println(String.valueOf(r_arr[0].getRecordContent()));
    String s = "~~~~ this is a newly updated record ~~~~";
    System.out.println(s.length());
    char[] c = s.toCharArray();
    System.out.println(c.length);
    f.updateRecord(101, s.toCharArray());
    System.out.println(String.valueOf(f.getRecord(101).getRecordContent()));
  }
}
