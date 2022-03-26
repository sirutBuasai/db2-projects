class Main {
  public static void main(String[] args) {
    char[] content = new char[80];
    for (int i = 0; i < 80; i++) {
      content[i] = (char) (i + '0');
    }
    System.out.println("content");
    printContent(content);
    Frame f = new Frame(content, true, true, 4);
    System.out.println("a");
    char[] a = f.getRecord(1);
    printContent(a);
    System.out.println("b");
    char[] b = f.getRecord(2);
    printContent(b);

  }
  public static void printContent(char[] arr) {
    for (int i = 0; i < arr.length; i++) {
      System.out.print(arr[i]);
    }
    System.out.println("");
  }

}
