package IF;


import java.util.Scanner;

public class Compare {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int num1, num2;

        num1 = scanner.nextInt();
        num2 = scanner.nextInt();

        if(num1>num2) {
            System.out.println(">");
        } else if(num1<num2) {
            System.out.println("<");
        } else if(num1==num2) {
            System.out.println("==");
        }
    }

}
