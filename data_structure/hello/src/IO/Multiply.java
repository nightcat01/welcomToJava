package IO;

import java.util.Scanner;

public class Multiply {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int num1;
        int num2;

        num1 = scanner.nextInt();
        num2 = scanner.nextInt();

        System.out.println(num1 * num2);
    }
}
