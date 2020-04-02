package IO;

import java.util.Scanner;

public class Division {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double num1;
        double num2;

        num1 = scanner.nextDouble();
        num2 = scanner.nextDouble();

        System.out.println(num1 / num2);
    }
}
