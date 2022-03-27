class Main {
  public static void main(String[] args) {
    char[] block = new char[80];
    for (int i = 0; i < 80; i++) {
      block[i] = (char) (i + '0');
    }
    System.out.println("content");
    printContent(block);
    Frame f = new Frame(block, true, true, 4);

    System.out.println("record_1");
    char[] r1 = f.getRecord(1);
    printContent(r1);
    System.out.println("record_2");
    char[] r2 = f.getRecord(2);
    printContent(r2);

    System.out.println("----------------------------------------");
    char[] new_content = new char[40];
    for (int i = 0; i < 40; i++) {
      new_content[i] = (char) (i + '0');
    }
    System.out.println("new_content");
    printContent(new_content);
    f.updateRecord(2, new_content);
    char[] new_r2 = f.getRecord(2);
    System.out.println("new record_2");
    printContent(new_r2);

  }
  public static void printContent(char[] arr) {
    for (int i = 0; i < arr.length; i++) {
      System.out.print(arr[i]);
    }
    System.out.println("");
  }

}
